package eu.tuxcraft.translationprovider.spigot.commands.translationprovidercommand.help;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Sub-command for the help command
 *
 * @author thelooter
 * @since 2.1.0
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HelpSubCommand {

  CommandSender commandSender;

  Component helpComponent = Component.text("---------", NamedTextColor.YELLOW)
      .append(Component.text("[", NamedTextColor.GRAY))
      .append(Component.text("TranslationProvider Help", NamedTextColor.AQUA))
      .append(Component.text("]", NamedTextColor.GRAY))
      .append(Component.text("---------", NamedTextColor.YELLOW))
      .append(Component.newline())
      .append(LegacyComponentSerializer.legacyAmpersand()
          .deserialize("&e/tlp help &8- &7Show this help"))
      .append(Component.newline())
      .append(LegacyComponentSerializer.legacyAmpersand()
          .deserialize("&e/tlp reload &8- &7Performs an Engine reload"))
      .append(Component.newline())
      .append(LegacyComponentSerializer.legacyAmpersand()
          .deserialize("&e/tlp clear &8- &7Invalidates all translations"))
      .append(Component.newline())
      .append(LegacyComponentSerializer.legacyAmpersand()
          .deserialize("&e/tlp stats &8- &7Shows the current engine stats"))
      .append(Component.newline())
      .append(LegacyComponentSerializer.legacyAmpersand()
          .deserialize("&e/tlp version &8- &7Shows the current engine version"))
      .append(Component.newline())
      .append(LegacyComponentSerializer.legacyAmpersand().deserialize(
          "&e/tlp add language <iso_code> <display_name> <enabled> <default> &8- &7Adds a new language"))
      .append(Component.newline())
      .append(LegacyComponentSerializer.legacyAmpersand().deserialize(
          "&e/tlp add translation <language> <key> <translation> &8- &7Adds a new Translation"))
      .append(Component.newline())
      .append(
          Component.text("----------------------------------------",
              NamedTextColor.YELLOW));


  /**
   * Creates a new instance of the help sub-command
   * @param commandSender The {@link CommandSender} to send the help to
   * @param args The arguments
   * @since 2.1.0
   */
  public HelpSubCommand(CommandSender commandSender,String[] args) {
    this.commandSender = commandSender;

    executeHelpSubCommand(args);
  }

  /**
   * Executes the help sub-command
   * @param args The arguments
   * @since 2.1.0
   */
  private void executeHelpSubCommand(String[] args) {
    if (args.length != 1) {
      commandSender.sendMessage(
          LegacyComponentSerializer.legacyAmpersand()
              .deserialize("&cUsage: /tlp help"));
      return;
    }
    if (commandSender instanceof Player player) {

      player.sendMessage(helpComponent);

      return;
    }

    commandSender.sendMessage(PlainTextComponentSerializer.plainText().serialize(helpComponent));
  }
}
