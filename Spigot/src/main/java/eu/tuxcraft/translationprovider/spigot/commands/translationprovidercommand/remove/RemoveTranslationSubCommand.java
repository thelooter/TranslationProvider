package eu.tuxcraft.translationprovider.spigot.commands.translationprovidercommand.remove;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.model.Language;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;

/**
 * Subcommand for removing a translation.
 *
 * @author thelooter
 * @since 2.1.0
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RemoveTranslationSubCommand {

  TranslationProviderEngine engine;
  CommandSender sender;

  /**
   * Creates a new instance of the {@link RemoveTranslationSubCommand} class.
   *
   * @param engine The {@link TranslationProviderEngine} instance.
   * @param args The arguments.
   * @param sender The {@link CommandSender} instance.
   * @since 2.1.0
   */
  public RemoveTranslationSubCommand(
      TranslationProviderEngine engine, String[] args, CommandSender sender) {
    this.engine = engine;
    this.sender = sender;

    executeRemoveTranslationSubCommand(args);
  }

  /**
   * Executes the subcommand.
   *
   * @param args The arguments.
   * @since 2.1.0
   */
  private void executeRemoveTranslationSubCommand(String[] args) {
    if (args.length != 4) {
      sender.sendMessage(
          LegacyComponentSerializer.legacyAmpersand()
              .deserialize("&cUsage: /tlp remove <language> <key> <value>"));
      return;
    }

    String language = args[2];
    String key = args[3];

    if (Language.getAvailableLanguages().stream()
        .map(Language::getDisplayName)
        .noneMatch(language::equals)) {
      sender.sendMessage(
          LegacyComponentSerializer.legacyAmpersand().deserialize("&cLanguage not found."));
      return;
    }

    if (engine.getAllRegisteredKeys().stream().noneMatch(key::equals)) {
      sender.sendMessage(
          LegacyComponentSerializer.legacyAmpersand().deserialize("&cKey not found."));
      return;
    }

    if (engine.removeTranslation(Language.fromDisplayName(language), key)) {
      sender.sendMessage(
          LegacyComponentSerializer.legacyAmpersand().deserialize("&aTranslation removed."));
    } else {
      sender.sendMessage(
          LegacyComponentSerializer.legacyAmpersand()
              .deserialize("&cFailed to remove Translation."));
    }
  }
}
