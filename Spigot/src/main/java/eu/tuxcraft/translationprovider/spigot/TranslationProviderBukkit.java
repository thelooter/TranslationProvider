package eu.tuxcraft.translationprovider.spigot;

import eu.tuxcraft.databaseprovider.paper.DatabaseProvider;
import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.spigot.model.LazyLoadingMessage;
import eu.tuxcraft.translationprovider.spigot.model.Message;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class TranslationProviderBukkit extends JavaPlugin {

  @Getter static TranslationProviderEngine engine;

  @Override
  @SneakyThrows
  public void onEnable() {
    engine = new TranslationProviderEngine(getLogger(), DatabaseProvider.getConnection());

    engine.performReload();

    getLogger().info("TranslationProviderBukkit enabled");
  }

  @Override
  public void onDisable() {
    getLogger().info("TranslationProviderBukkit disabled");
  }

  public static void mapAllTranslations(Class<?> messageClass, String prefix)
      throws IllegalAccessException {
    for (Field field : messageClass.getDeclaredFields()) {
      if (field.getType() == Message.class) continue;

      if (!field.isAccessible()) field.setAccessible(true);

      String prefixKey = prefix + ".prefix";

      field.set(null, new LazyLoadingMessage(prefix + field.getName(), prefixKey));
    }
  }
}
