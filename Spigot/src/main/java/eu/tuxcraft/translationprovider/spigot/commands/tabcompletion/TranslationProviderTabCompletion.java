package eu.tuxcraft.translationprovider.spigot.commands.tabcompletion;

import eu.tuxcraft.translationprovider.spigot.commands.TranslationProviderCommand;
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
public class TranslationProviderTabCompletion implements TabCompleter {
  @Override
  public @Nullable List<String> onTabComplete(
      @NotNull CommandSender commandSender,
      @NotNull Command command,
      @NotNull String s,
      @NotNull String[] args) {
    if (args.length == 0) {
      return List.of("reload", "stats", "clear");
    } else {
		return Collections.singletonList("Invalid amount of Arguments");
    }
  }
}
