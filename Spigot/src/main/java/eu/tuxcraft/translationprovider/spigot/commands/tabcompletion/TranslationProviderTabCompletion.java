package eu.tuxcraft.translationprovider.spigot.commands.tabcompletion;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.model.Language;
import eu.tuxcraft.translationprovider.spigot.commands.TranslationProviderCommand;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * TabCompleter for the @{@link TranslationProviderCommand}
 *
 * @author thelooter
 * @since 2.0.0
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TranslationProviderTabCompletion implements TabCompleter {

  TranslationProviderEngine engine;

  String ADD_KEY = "add";
  String REMOVE_KEY = "remove";
  String EDIT_KEY = "edit";

  String LANGUAGE_KEY = "language";
  String TRANSLATION_KEY = "translation";

  /**
   * Creates a new instance of the {@link TranslationProviderTabCompletion}
   *
   * @param engine the {@link TranslationProviderEngine}
   * @since 2.0.0
   */
  public TranslationProviderTabCompletion(TranslationProviderEngine engine) {
    this.engine = engine;
  }

  /**
   * {@inheritDoc}
   *
   * @param commandSender The {@link CommandSender}
   * @param command The {@link Command}
   * @param s The String
   * @param args The arguments
   * @return The list of possible completions
   * @since 2.0.0
   */
  @Override
  public @Nullable List<String> onTabComplete(
      @NotNull CommandSender commandSender,
      @NotNull Command command,
      @NotNull String s,
      @NotNull String[] args) {
    if (args.length == 1) {
      return List.of("reload", "stats", "clear", "add", "version", "help", "remove", "edit");
    }
    if (args.length == 2) {
      if (args[0].equalsIgnoreCase(ADD_KEY)) {
        return List.of(LANGUAGE_KEY, TRANSLATION_KEY);
      } else if (args[0].equalsIgnoreCase(REMOVE_KEY)) {
        return List.of(LANGUAGE_KEY, TRANSLATION_KEY);
      } else if (args[0].equalsIgnoreCase(EDIT_KEY)) {
        return List.of(LANGUAGE_KEY, TRANSLATION_KEY);
      } else {
        return Collections.singletonList("Invalid amount of Arguments");
      }
    }

    if (args.length == 3) {
      if (args[0].equalsIgnoreCase(ADD_KEY)) {
        if (args[1].equalsIgnoreCase(TRANSLATION_KEY)) {
          return Language.getAvailableLanguages().stream().map(Language::getDisplayName).toList();
        }
        if (args[1].equalsIgnoreCase(LANGUAGE_KEY)) {
          return Collections.singletonList("<iso-code>");
        }
      }
      if (args[0].equalsIgnoreCase(REMOVE_KEY)) {
        return Language.getAvailableLanguages().stream().map(Language::getDisplayName).toList();
      }
      if (args[0].equalsIgnoreCase(EDIT_KEY)) {
        if (args[1].equalsIgnoreCase(TRANSLATION_KEY)) {
          return Language.getAvailableLanguages().stream().map(Language::getDisplayName).toList();
        }
        if (args[1].equalsIgnoreCase(LANGUAGE_KEY)) {
          return Language.getAvailableLanguages().stream().map(Language::getDisplayName).toList();
        }
      }
    }

    if (args.length == 4) {
      if (args[0].equalsIgnoreCase(ADD_KEY)) {
        if (args[1].equalsIgnoreCase(TRANSLATION_KEY)
            && Language.getAvailableLanguages().stream()
                .anyMatch(l -> l.getDisplayName().equalsIgnoreCase(args[2]))) {
          return engine.getAllRegisteredKeys();
        }
        if (args[1].equalsIgnoreCase(LANGUAGE_KEY)) {
          return Collections.singletonList("<display-name>");
        }
      }
      if (args[0].equalsIgnoreCase(REMOVE_KEY) && args[1].equalsIgnoreCase(TRANSLATION_KEY)) {
        return engine.getAllRegisteredKeys();
      }

      if (args[0].equalsIgnoreCase(EDIT_KEY)) {
        if (args[1].equalsIgnoreCase(TRANSLATION_KEY)) {
          return engine.getAllRegisteredKeys();
        }
        if (args[1].equalsIgnoreCase(LANGUAGE_KEY)) {
          return List.of("display_name", "iso_code", "default", "enabled");
        }
      }
    }

    if (args.length == 5) {
      if (args[0].equalsIgnoreCase(ADD_KEY)) {
        if (args[1].equalsIgnoreCase(TRANSLATION_KEY)
            && Language.getAvailableLanguages().stream()
                .anyMatch(l -> l.getDisplayName().equalsIgnoreCase(args[2]))
            && engine.getAllRegisteredKeys().stream().anyMatch(k -> k.equalsIgnoreCase(args[3]))) {
          return Collections.singletonList("Add Translation here");
        }
        if (args[1].equalsIgnoreCase(LANGUAGE_KEY)) {
          return Collections.singletonList("<enabled>");
        }
      }
      if (args[0].equalsIgnoreCase(EDIT_KEY)) {
        if (args[1].equalsIgnoreCase(TRANSLATION_KEY)
            && Language.getAvailableLanguages().stream()
                .anyMatch(l -> l.getDisplayName().equalsIgnoreCase(args[2]))
            && engine.getAllRegisteredKeys().stream().anyMatch(k -> k.equalsIgnoreCase(args[3]))) {
          return Collections.singletonList("Edit Translation here");
        }
        if (args[1].equalsIgnoreCase(LANGUAGE_KEY)) {
          if (List.of("default", "enabled").contains(args[3])) {
            return List.of("true", "false");
          } else if (args[3].equalsIgnoreCase("iso_code")) {
            return Collections.singletonList("Enter new ISO Code");
          } else if (args[3].equalsIgnoreCase("display_name")) {
            return Collections.singletonList("Enter new Display Name");
          }
          return Collections.singletonList("Invalid Argument");
        }
      }
    }

    if (args.length == 6
        && args[0].equalsIgnoreCase(ADD_KEY)
        && args[1].equalsIgnoreCase(LANGUAGE_KEY)) {
      return Collections.singletonList("<default>");
    }

    return Collections.singletonList("Invalid amount of Arguments");
  }
}
