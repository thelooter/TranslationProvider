package eu.tuxcraft.translationprovider.spigot.commands.translationprovidercommand.edit;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.model.Language;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.logging.Logger;

/**
 * Subcommand to edit Translations
 *
 * @since 2.1.0
 */
public class EditTranslationSubCommand {

  Logger logger;
  TranslationProviderEngine engine;

  CommandSender sender;

  List<Language> availableLanguages = Language.getAvailableLanguages();

  /**
   * Constructor for the Edit Translation SubCommand Also executes it
   *
   * @param logger The Logger
   * @param engine The {@link TranslationProviderEngine} instance
   * @param args The Arguments
   * @param sender The {@link CommandSender} to send Messages too.
   */
  public EditTranslationSubCommand(
      Logger logger, TranslationProviderEngine engine, String[] args, CommandSender sender) {
    System.out.println("yaa");
    this.logger = logger;
    this.engine = engine;
    this.sender = sender;

    executeEditTranslationSubCommand(args);
  }

  private void executeEditTranslationSubCommand(String[] args) {
    if (args.length != 5) {
      sender.sendMessage("&cUsage: /tlp edit translation <language_name> <key> <value>");
      return;
    }

    String language = args[2];
    String unverifiedKey = args[3];
    String newValue = args[4];

    Language lang =
        availableLanguages.stream()
            .filter(l -> l.getDisplayName().equals(language))
            .findFirst()
            .orElse(null);

    String key =
        engine.getAllRegisteredKeys().stream()
            .filter(streamKey -> streamKey.equals(unverifiedKey))
            .findFirst()
            .orElse(null);

    if (lang == null) {
      sender.sendMessage("&cLanguage not found");
      return;
    }

    if (key == null) {
      sender.sendMessage("&cKey not found");
      return;
    }

    engine.editTranslation(lang, key, newValue);
    sender.sendMessage("&aSuccessfully set the Translation of Key &b"+ key + " &ato &b" + newValue);
  }
}
