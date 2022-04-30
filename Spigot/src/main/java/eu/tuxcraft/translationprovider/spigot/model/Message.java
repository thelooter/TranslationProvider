package eu.tuxcraft.translationprovider.spigot.model;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * Interface for a message.
 *
 * @author thelooter
 * @since 2.0.0
 */
public interface Message {

  /**
   * Sends the translation to the given {@link CommandSender}
   *
   * @param cs the {@link CommandSender} to send the translation to
   * @since 2.0.0
   */
  void sendTo(CommandSender cs);

  /**
   * Sends the translation to the given {@link CommandSender}
   *
   * @param cs the {@link CommandSender} to send the translation to
   * @param params the replacements to use
   * @since 2.0.0
   */
  void sendTo(CommandSender cs, Map<String, String> params);

  /**
   * Gets the translation for the given {@link CommandSender}
   *
   * @param cs the {@link CommandSender} to get the translation for
   * @return the translation
   */
  String getFor(CommandSender cs);

  /**
   * Gets the translation for the given {@link CommandSender}
   *
   * @param cs the {@link CommandSender} to get the translation for
   * @param params the replacements to use
   * @return the translation
   */
  String getFor(CommandSender cs, Map<String, String> params);

  /**
   * Gets the translation as a {@link Component} for the given {@link CommandSender}
   *
   * @param cs the {@link CommandSender} to get the translation for
   * @param isMiniMessage whether the Translation should be parsed as a {@link MiniMessage}
   * @return the translation as a {@link Component}
   */
  Component getComponentFor(CommandSender cs, boolean isMiniMessage);

  /**
   * Gets the translation as a {@link Component} for the given {@link CommandSender}
   *
   * @param cs the {@link CommandSender} to get the translation for
   * @param params the replacements to use
   * @param isMiniMessage whether the Translation should be parsed as a {@link MiniMessage}
   * @return the translation as a {@link Component}
   */
  Component getComponentFor(CommandSender cs, Map<String, String> params, boolean isMiniMessage);
}
