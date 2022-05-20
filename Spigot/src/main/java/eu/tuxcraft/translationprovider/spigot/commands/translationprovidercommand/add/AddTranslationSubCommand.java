package eu.tuxcraft.translationprovider.spigot.commands.translationprovidercommand.add;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.model.Language;
import java.util.List;
import java.util.logging.Logger;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;

/**
 * Sub-command for adding a translation.
 *
 * @author thelooter
 * @since 2.1.0
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddTranslationSubCommand {

  CommandSender sender;
  Logger logger;
  TranslationProviderEngine engine;

  List<Language> availableLanguages = Language.getAvailableLanguages();

  /**
   * Creates a new instance of the {@link AddTranslationSubCommand} class.
   *
   * @param logger The {@link Logger} to use.
   * @param engine The {@link TranslationProviderEngine} to use.
   * @param args The arguments to use.
   * @param sender The {@link CommandSender} to use.
   * @since 2.1.0
   */
  public AddTranslationSubCommand(
      Logger logger, TranslationProviderEngine engine, String[] args, CommandSender sender) {
    this.logger = logger;
    this.engine = engine;
    this.sender = sender;

    executeAddTranslationSubCommand(args);
  }

  /**
   * Executes the sub-command.
   *
   * @param args The arguments to use.
   * @since 2.1.0
   */
  private void executeAddTranslationSubCommand(String[] args) {
    if (args.length != 5) {
      sender.sendMessage(
          LegacyComponentSerializer.legacyAmpersand()
              .deserialize("&cUsage: /tlp add translation <language> <key> <value>"));
      return;
    }

    String language = args[2];
    String key = args[3];
    String value = args[4];

    if (availableLanguages.stream().noneMatch(l -> l.getDisplayName().equals(language))) {
      sender.sendMessage(
          LegacyComponentSerializer.legacyAmpersand().deserialize("&c Language not found"));
      return;
    }

    if (engine.getAllRegisteredKeys().contains(key)) {

      engine.addTranslation(Language.fromDisplayName(language), key, value);
      sender.sendMessage(
          LegacyComponentSerializer.legacyAmpersand().deserialize("&a Translation added"));
    } else {
      sender.sendMessage(
          LegacyComponentSerializer.legacyAmpersand().deserialize("&c Key not found"));
    }
  }
}
