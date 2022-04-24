package eu.tuxcraft.translationprovider.spigot.commands;

import eu.tuxcraft.translationprovider.spigot.TranslationProvider;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * The main command of the plugin.
 *
 * @author thelooter
 * @since 2.0.0
 */
public class TranslationProviderCommand implements CommandExecutor {
  @Override
  public boolean onCommand(
      @NotNull CommandSender commandSender,
      @NotNull Command command,
      @NotNull String s,
      @NotNull String[] args) {
    if (args.length == 0) {
      commandSender.sendMessage("Possible Options: reload, stats,clear");
      return true;
    }

    switch (args[0]) {
      case "reload":
        TranslationProvider.getEngine().performReload();
        commandSender.sendMessage("Reloading translations...");
        break;
      case "stats":
        commandSender.sendMessage(
            "TranslationCache: "
                + TranslationProvider.getEngine().getTranslationCache().getCache().size());
        commandSender.sendMessage(
            "LanguageCache: "
                + TranslationProvider.getEngine().getUserLanguageCache().getCache().size());

        break;
      case "clear":
        TranslationProvider.getEngine().getTranslationCache().getCache().invalidateAll();
        TranslationProvider.getEngine().getUserLanguageCache().getCache().invalidateAll();

        commandSender.sendMessage("Cleared Cache");
        break;
      default:
        commandSender.sendMessage("Possible Options: reload, stats,clear");
        break;
    }
    return true;
  }
}
