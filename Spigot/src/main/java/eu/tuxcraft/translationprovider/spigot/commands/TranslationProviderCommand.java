package eu.tuxcraft.translationprovider.spigot.commands;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.spigot.TranslationProvider;
import eu.tuxcraft.translationprovider.spigot.commands.translationprovidercommand.add.AddLanguageSubCommand;
import eu.tuxcraft.translationprovider.spigot.commands.translationprovidercommand.add.AddTranslationSubCommand;
import eu.tuxcraft.translationprovider.spigot.commands.translationprovidercommand.edit.EditLanguageSubCommand;
import eu.tuxcraft.translationprovider.spigot.commands.translationprovidercommand.edit.EditTranslationSubCommand;
import eu.tuxcraft.translationprovider.spigot.commands.translationprovidercommand.help.HelpSubCommand;
import eu.tuxcraft.translationprovider.spigot.commands.translationprovidercommand.remove.RemoveLanguageSubCommand;
import eu.tuxcraft.translationprovider.spigot.commands.translationprovidercommand.remove.RemoveTranslationSubCommand;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * The main command of the plugin.
 *
 * @author thelooter
 * @since 2.0.0
 */
public class TranslationProviderCommand implements CommandExecutor {

  Logger logger;
  TranslationProviderEngine engine;

  /**
   * Creates a new instance of the command.
   *
   * @param logger The {@link Logger} to use.
   * @param engine The {@link TranslationProviderEngine} to use.
   * @since 2.0.0
   */
  public TranslationProviderCommand(Logger logger, TranslationProviderEngine engine) {
    this.logger = logger;
    this.engine = engine;
  }

  @Override
  public boolean onCommand(
      @NotNull CommandSender commandSender,
      @NotNull Command command,
      @NotNull String s,
      @NotNull String[] args) {

    if (args.length == 0) {
      commandSender.sendMessage("Possible Options: reload, stats,clear, add, edit");
      return true;
    }

    switch (args[0]) {
      case "reload" -> {
        TranslationProvider.getEngine().performReload();
        commandSender.sendMessage("Reloading translations...");
      }
      case "stats" -> {
        commandSender.sendMessage(
            "TranslationCache: "
                + TranslationProvider.getEngine().getTranslationCache().getCache().size());
        commandSender.sendMessage(
            "LanguageCache: "
                + TranslationProvider.getEngine().getUserLanguageCache().getCache().size());
      }
      case "clear" -> {
        TranslationProvider.getEngine().getTranslationCache().getCache().invalidateAll();
        TranslationProvider.getEngine().getUserLanguageCache().getCache().invalidateAll();
        commandSender.sendMessage("Cleared Cache");
      }
      case "add" -> {
        if (args.length == 1) {
          commandSender.sendMessage("Possible Options: language, translation");
          return true;
        }
        if (args[1].equals("language")) {
          new AddLanguageSubCommand(logger, engine, args, commandSender);
        }
        if (args[1].equals("translation")) {
          new AddTranslationSubCommand(logger, engine, args, commandSender);
        }
      }
      case "help" -> new HelpSubCommand(commandSender,args);
      case "remove" -> {
        if (args.length == 1) {
          commandSender.sendMessage(LegacyComponentSerializer.legacyAmpersand()
              .deserialize("&cPossible Options: language, translation"));
          return true;
        }
        if (args[1].equals("language")) {
          new RemoveLanguageSubCommand(engine, args, commandSender);
          return true;
        }
        if (args[1].equals("translation")) {
          new RemoveTranslationSubCommand(engine, args, commandSender);
          return true;
        }
      }
      case "version" -> {
        commandSender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(
            "&eTranslationProvider Version: &b" + TranslationProvider.getInstance().getDescription()
                .getVersion()));
      }
      case "edit" -> {
        if (args.length == 1) {
          commandSender.sendMessage(LegacyComponentSerializer.legacyAmpersand()
              .deserialize("&cPossible Options: language, translation"));
          return true;
        }
        if (args[1].equals("language")) {
          new EditLanguageSubCommand(logger,engine, args, commandSender);
          return true;
        }

        if (args[1].equals("translation")){
          new EditTranslationSubCommand(logger,engine,args,commandSender);
          return true;
        }
      }
      default -> commandSender.sendMessage("Possible Options: reload, stats, clear, add, remove, help, version");
    }
    return true;
  }
}
