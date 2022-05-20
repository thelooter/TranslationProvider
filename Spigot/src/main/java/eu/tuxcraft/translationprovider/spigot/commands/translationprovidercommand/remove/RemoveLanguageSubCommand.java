package eu.tuxcraft.translationprovider.spigot.commands.translationprovidercommand.remove;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.model.Language;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;

/**
 * Sub-command to remove a language from the translation provider.
 *
 * @author thelooter
 * @since 2.1.0
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RemoveLanguageSubCommand {

  TranslationProviderEngine engine;
  CommandSender sender;

  /**
   * Creates a new instance of the {@link RemoveLanguageSubCommand} class.
   *
   * @param engine The {@link TranslationProviderEngine} instance.
   * @param args The arguments.
   * @param sender The {@link CommandSender} instance.
   * @since 2.1.0
   */
  public RemoveLanguageSubCommand(
      TranslationProviderEngine engine, String[] args, CommandSender sender) {
    this.engine = engine;
    this.sender = sender;

    executeRemoveLanguageSubCommand(args);
  }

  /**
   * Executes the sub-command to remove a language from the translation provider.
   *
   * @param args The arguments.
   * @since 2.1.0
   */
  private void executeRemoveLanguageSubCommand(String[] args) {
    if (args.length != 3) {
      sender.sendMessage(
          LegacyComponentSerializer.legacyAmpersand()
              .deserialize("&cUsage: /tp remove <language>"));
      return;
    }

    String languageString = args[2];

    if (Language.getAvailableLanguages().stream()
        .noneMatch(l -> l.getDisplayName().equals(languageString))) {
      sender.sendMessage(
          LegacyComponentSerializer.legacyAmpersand().deserialize("&cLanguage not found."));
      return;
    }

    Language lang =
        Language.getAvailableLanguages().stream()
            .filter(l -> l.getDisplayName().equals(languageString))
            .findFirst()
            .orElse(null);

    engine.removeLanguage(lang);
    sender.sendMessage(
        LegacyComponentSerializer.legacyAmpersand().deserialize("&aLanguage removed."));
  }
}
