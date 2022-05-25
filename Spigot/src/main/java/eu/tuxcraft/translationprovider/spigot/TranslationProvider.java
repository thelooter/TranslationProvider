package eu.tuxcraft.translationprovider.spigot;

import eu.tuxcraft.databaseprovider.paper.DatabaseProvider;
import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.model.Language;
import eu.tuxcraft.translationprovider.spigot.commands.TranslationProviderCommand;
import eu.tuxcraft.translationprovider.spigot.commands.tabcompletion.TranslationProviderTabCompletion;
import eu.tuxcraft.translationprovider.spigot.listener.JoinQuitListener;
import eu.tuxcraft.translationprovider.spigot.model.LazyLoadingMessage;
import eu.tuxcraft.translationprovider.spigot.model.Message;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;

/**
 * The main class of the plugin.
 *
 * @author thelooter
 * @since 2.0.0
 */
public class TranslationProvider extends JavaPlugin {

  Connection connection;

  /**
   * Constructor for Bukkit
   *
   * @since 2.0.0
   */
  public TranslationProvider() {
    super();
  }

  /**
   * Creates a new Instance of the {@link TranslationProvider} for MockBukkit.
   *
   * @param loader The {@link JavaPluginLoader}.
   * @param descriptionFile The {@link PluginDescriptionFile}.
   * @param dataFolder The Datafolder of the {@link JavaPlugin}.
   * @param file The File of the {@link JavaPlugin}.
   * @since 2.0.0
   */
  protected TranslationProvider(
      JavaPluginLoader loader, PluginDescriptionFile descriptionFile, File dataFolder, File file) {
    super(loader, descriptionFile, dataFolder, file);
  }

  @Getter static TranslationProviderEngine engine;

  @Getter private static TranslationProvider instance;

  @Override
  @SneakyThrows
  public void onEnable() {
    instance = this;

    // Load Config
    loadConfig();

    try {
      connection = DatabaseProvider.getConnection();
    } catch (Exception e) {
      getLogger().info("Could not connect to the database with DatabaseProvider, using fallback");
      connection =
          DriverManager.getConnection(
              Objects.requireNonNull(getConfig().getString("fallback.sql.jdbcUrl")),
              getConfig().getString("fallback.sql.username"),
              getConfig().getString("fallback.sql.password"));
    }

    engine = new TranslationProviderEngine(getLogger(), connection);

    engine.performReload();

    // Register Commands
    getCommand("translationprovider")
        .setExecutor(new TranslationProviderCommand(getLogger(), engine));

    // Register TabCompleter
    getCommand("translationprovider").setTabCompleter(new TranslationProviderTabCompletion(engine));

    // Register Events
    getServer().getPluginManager().registerEvents(new JoinQuitListener(engine), this);

    getLogger().info("TranslationProviderBukkit enabled");
  }

  @Override
  public void onDisable() {
    getLogger().info("TranslationProviderBukkit disabled");
  }

  /**
   * Maps all translations in the given {@link Class} to the engine.
   *
   * @param clazz The {@link Class} to map.
   * @param keyPrefix The prefix for the keys.
   * @throws IllegalAccessException If the {@link Field} is not accessible.
   */
  public static void mapAllTranslations(Class<?> clazz, String keyPrefix)
      throws IllegalAccessException {

    if (keyPrefix.endsWith(".")) {
      keyPrefix = keyPrefix.substring(0, keyPrefix.length() - 1);
    }

    for (Field field : clazz.getFields()) {
      if (field.getType() != Message.class) {
        continue;
      }

      if (!field.isAccessible()) {
        field.setAccessible(true);
      }

      field.set(null, new LazyLoadingMessage(keyPrefix + "." + field.getName()));

      engine.registerKey(keyPrefix + "." + field.getName());
    }

    for (Class<?> subClazz : clazz.getClasses()) {
      String clazzName = subClazz.getSimpleName();
      clazzName = clazzName.substring(0, 1).toLowerCase() + clazzName.substring(1);

      mapAllTranslations(subClazz, keyPrefix + "." + clazzName);
    }
  }

  /**
   * Sets the {@link Language} of the given {@link Player}.
   *
   * @param player The {@link Player}.
   * @param language The {@link Language}.
   */
  public static void playerLanguage(Player player, Language language) {
    engine.playerLanguage(player.getUniqueId(), language);
  }

  /**
   * Gets the {@link Language} of the given {@link Player}.
   *
   * @param player The {@link Player}.
   * @return The {@link Language}.
   */
  public static Language playerLanguage(Player player) {
    return engine.playerLanguage(player.getUniqueId());
  }

  private void loadConfig() {
    getConfig().options().copyDefaults(true);
    saveConfig();
  }
}
