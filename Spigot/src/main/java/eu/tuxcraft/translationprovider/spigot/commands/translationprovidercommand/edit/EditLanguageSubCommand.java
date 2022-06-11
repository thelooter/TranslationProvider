package eu.tuxcraft.translationprovider.spigot.commands.translationprovidercommand.edit;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.model.Language;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.logging.Logger;

/**
 * Sub-command for editing a language in the translation provider.
 *
 * @author thelooter
 * @since 2.1.0
 */
public class EditLanguageSubCommand {
  Logger logger;
  TranslationProviderEngine engine;

  CommandSender sender;

  List<Language> availableLanguages = Language.getAvailableLanguages();

    /**
     * Creates a new instance of the {@link EditLanguageSubCommand} class.
     *
     * @param logger The {@link Logger} to use.
     * @param engine The {@link TranslationProviderEngine} to use.
     * @param args The arguments to use.
     * @param sender The {@link CommandSender} to use.
     * @since 2.1.0
     */
  public EditLanguageSubCommand(
      Logger logger, TranslationProviderEngine engine, String[] args, CommandSender sender) {
    this.logger = logger;
    this.engine = engine;
    this.sender = sender;

    executeEditLanguageSubCommand(args);
  }

    /**
     * Executes the edit language sub-command.
     *
     * @param args The arguments to use.
     * @since 2.1.0
     */
  private void executeEditLanguageSubCommand(String[] args) {
    if (args.length != 5) {
      sender.sendMessage(LegacyComponentSerializer
                                 .legacyAmpersand()
                                 .deserialize("&cUsage: /tlp edit language <language_name> <property> <value>"));
      return;
    }

    String language = args[2];

    if (availableLanguages.stream().noneMatch(l -> l.getDisplayName().equals(language))) {
      sender.sendMessage(
          LegacyComponentSerializer.legacyAmpersand()
              .deserialize("&cLanguage " + language + " not found."));
      return;
    }

    Language lang =
        availableLanguages.stream()
            .filter(l -> l.getDisplayName().equals(language))
            .findFirst()
            .orElse(null);

    String property = args[3];


    switch (property) {
      case "enabled" -> {
        if (!List.of("true","false").contains(args[4])){
          sender.sendMessage(LegacyComponentSerializer
                                     .legacyAmpersand()
                                     .deserialize("&cValue must be true or false"));
          return;
        }

        boolean value = Boolean.parseBoolean(args[4]);

        engine.editLanguageEnabled(lang, value);
        sender.sendMessage(
                LegacyComponentSerializer.legacyAmpersand()
                        .deserialize("&aSuccessfully set the Enabled Value of language &b" + language + " &ato &b" + value));
      }
      case "default" -> {
        if (!List.of("true","false").contains(args[4])){
          sender.sendMessage(LegacyComponentSerializer
                                     .legacyAmpersand()
                                     .deserialize("&cValue must be true or false"));
          return;
        }

        boolean value = Boolean.parseBoolean(args[4]);

        if (value && availableLanguages.stream().anyMatch(Language::isDefault)) {
          sender.sendMessage(
                  LegacyComponentSerializer.legacyAmpersand()
                          .deserialize("&cThere is already a default language."));
          break;
        }
        engine.editLanguageDefault(lang, value);
        sender.sendMessage(
                LegacyComponentSerializer.legacyAmpersand()
                        .deserialize("&aSuccessfully set the Default Value of language &b" + language + " &ato &b" + value));
      }
      case "display_name" -> {

        engine.editLanguageDisplayName(lang, args[4]);
        sender.sendMessage(
                LegacyComponentSerializer.legacyAmpersand()
                        .deserialize("&aChanged Display Name of Language &b" + language + "&a to &b" + args[4]));
      }
      case "iso_code" -> {
        engine.editLanguageIsoCode(lang, args[4]);
        sender.sendMessage(
                LegacyComponentSerializer.legacyAmpersand()
                        .deserialize("&aChanged Iso Code of Language &b" + language + "&a to &b" + args[4]));
      }
      default -> sender.sendMessage(
                LegacyComponentSerializer.legacyAmpersand()
                        .deserialize("&cProperty " + property + " not found."));
    }
  }

}