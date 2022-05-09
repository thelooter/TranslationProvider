package eu.tuxcraft.translationprovider.spigot.commands.tabcompletion;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.model.Language;
import eu.tuxcraft.translationprovider.spigot.commands.TranslationProviderCommand;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * TabCompleter for the @{@link TranslationProviderCommand}
 *
 * @author thelooter
 * @since 2.0.0
 */
public class TranslationProviderTabCompletion implements TabCompleter {

  TranslationProviderEngine engine;

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
   * @param command       The {@link Command}
   * @param s             The String
   * @param args          The arguments
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
      return List.of("reload", "stats", "clear", "add", "version", "help");
    }
    if (args.length == 2) {
      if (args[0].equalsIgnoreCase("add")) {
        return List.of("language", "translation");
      } else {
        return Collections.singletonList("Invalid amount of Arguments");
      }
    }
    if (args.length == 3 && args[0].equalsIgnoreCase("add") && args[1].equalsIgnoreCase(
        "translation")) {
      return Language.getAvailableLanguages().stream().map(Language::getDisplayName).toList();
    }
    if (args.length == 4 && args[0].equalsIgnoreCase("add") && args[1].equalsIgnoreCase(
        "translation") && Language.getAvailableLanguages().stream()
        .anyMatch(l -> l.getDisplayName().equalsIgnoreCase(args[2]))) {
      return engine.getAllRegisteredKeys();
    }
    if (args.length == 5 && args[0].equalsIgnoreCase("add") && args[1].equalsIgnoreCase(
        "translation") && Language.getAvailableLanguages().stream()
        .anyMatch(l -> l.getDisplayName().equalsIgnoreCase(args[2]))
        && engine.getAllRegisteredKeys().stream().anyMatch(k -> k.equalsIgnoreCase(args[3]))) {

      return Collections.singletonList("Add Translation here");
    }

    if (args.length == 3 && args[0].equalsIgnoreCase("add") && args[1].equalsIgnoreCase(
        "language")) {
      commandSender.sendMessage("3");
      return Collections.singletonList("<iso-code>");
    }
    if (args.length == 4 && args[0].equalsIgnoreCase("add") && args[1].equalsIgnoreCase(
        "language")) {
      commandSender.sendMessage("4");
      return Collections.singletonList("<display-name>");
    }
    if (args.length == 5 && args[0].equalsIgnoreCase("add") && args[1].equalsIgnoreCase(
        "language")) {
      commandSender.sendMessage("5");
      return Collections.singletonList("<enabled>");
    }

    if (args.length == 6 && args[0].equalsIgnoreCase("add") && args[1].equalsIgnoreCase(
        "language")) {
      commandSender.sendMessage("6");

      return Collections.singletonList("<default>");
    }

    return Collections.singletonList("Invalid amount of Arguments");


  }
}
