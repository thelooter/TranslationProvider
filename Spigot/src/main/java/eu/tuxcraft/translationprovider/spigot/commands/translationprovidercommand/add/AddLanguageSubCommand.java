package eu.tuxcraft.translationprovider.spigot.commands.translationprovidercommand.add;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.model.Language;
import java.util.List;
import java.util.logging.Logger;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;

/**
 * Sub-command for adding a language to the translation provider.
 *
 * @author thelooter
 * @since 2.1.0
 */
public class AddLanguageSubCommand {

  Logger logger;
  TranslationProviderEngine engine;

  CommandSender sender;

  List<Language> availableLanguages = Language.getAvailableLanguages();

  /**
   * Creates a new instance of the {@link AddLanguageSubCommand} class.
   *
   * @param logger The {@link Logger} to use.
   * @param engine The {@link TranslationProviderEngine} to use.
   * @param args The arguments to use.
   * @param sender The {@link CommandSender} to use.
   * @since 2.1.0
   */
  public AddLanguageSubCommand(
      Logger logger, TranslationProviderEngine engine, String[] args, CommandSender sender) {
    this.logger = logger;
    this.engine = engine;
    this.sender = sender;

    executeAddLanguageSubCommand(args);
  }

  /**
   * Executes the add language sub-command.
   *
   * @param args The arguments to use.
   */
  private void executeAddLanguageSubCommand(String[] args) {
    if (args.length != 6) {
      sender.sendMessage(
          LegacyComponentSerializer.legacyAmpersand()
              .deserialize("&cUsage: /tlp add language iso_code display_name enabled default"));
      return;
    }

    String isoCode = args[2];
    String displayName = args[3];
    boolean enabled = Boolean.parseBoolean(args[4]);
    boolean defaultLanguage = Boolean.parseBoolean(args[5]);

    if (availableLanguages.stream().anyMatch(l -> l.getIsoCode().equals(isoCode))) {
      sender.sendMessage(
          LegacyComponentSerializer.legacyAmpersand()
              .deserialize("&cLanguage with iso code " + isoCode + " already exists"));
      return;
    }
    if (availableLanguages.stream().anyMatch(l -> l.getDisplayName().equals(displayName))) {
      sender.sendMessage(
          LegacyComponentSerializer.legacyAmpersand()
              .deserialize("&cLanguage with display name " + args[3] + " already exists"));
      return;
    }
    if (defaultLanguage && availableLanguages.stream().anyMatch(Language::isDefault)) {
      sender.sendMessage(
          LegacyComponentSerializer.legacyAmpersand()
              .deserialize("&cThere is already a default language"));
      return;
    }

    Language language = new Language(isoCode, displayName, enabled, defaultLanguage);

    // Add language to engine
    if (engine.addLanguage(language)) {
      sender.sendMessage(
          LegacyComponentSerializer.legacyAmpersand().deserialize("&aLanguage added"));
    } else {
      sender.sendMessage(
          LegacyComponentSerializer.legacyAmpersand().deserialize("&cFailed to add language"));
      logger.warning("Failed to add language " + language.getIsoCode());
    }
  }
}
