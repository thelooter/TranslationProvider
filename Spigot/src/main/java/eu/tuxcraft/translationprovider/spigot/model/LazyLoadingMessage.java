package eu.tuxcraft.translationprovider.spigot.model;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.spigot.TranslationProvider;
import java.util.Map;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Implementation of {@link Message} interface that lazily loads the message from the engine.
 *
 * @author thelooter
 * @see Message
 * @since 2.0.0
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LazyLoadingMessage implements Message {

  TranslationProviderEngine engine = TranslationProvider.getEngine();

  String key;


  /**
   * Constructs a new {@link LazyLoadingMessage} instance.
   *
   * @param key the key of the message
   * @since 2.0.0
   */
  public LazyLoadingMessage(String key) {
    this.key = key;
  }

  /**
   * {@inheritDoc}
   *
   * @param cs {@inheritDoc}
   * @since 2.0.0
   */
  @Override
  public void sendTo(CommandSender cs) {
    sendTo(cs, null);
  }


  /**
   * Sends the translation to the given {@link CommandSender}
   *
   * @param cs     the {@link CommandSender} to send the translation to
   * @param params the {@link Map} with replacements to apply
   * @since 2.0.0
   */
  @Override
  public void sendTo(CommandSender cs, Map<String, String> params) {

    String translation;

    if (cs instanceof Player player) {
      translation = engine.getTranslationForUser(player.getUniqueId(), key, params);

    } else {
      translation = engine.getTranslationForUser(null, key, params);
    }
    if (translation.contains("\n")) {
      translation.lines().forEach((line) -> {
        Component miniMessage = MiniMessage.miniMessage().deserialize(line);
        String convertedLine = LegacyComponentSerializer.legacySection().serialize(miniMessage);
        cs.sendMessage(convertedLine);
      });
    } else {
      Component miniMessage = MiniMessage.miniMessage().deserialize(translation);
      String convertedMessage = LegacyComponentSerializer.legacySection().serialize(miniMessage);
      cs.sendMessage(convertedMessage);
    }

  }


  /**
   * Gets the translation for the given {@link CommandSender}
   *
   * @param cs the {@link CommandSender} to get the translation for
   * @return the translation
   */
  @Override
  public String getFor(CommandSender cs) {

    return getFor(cs, null);
  }

  /**
   * Gets the translation for the given {@link CommandSender}
   *
   * @param cs     the {@link CommandSender} to get the translation for
   * @param params the replacements to use
   * @return the translation
   */
  @Override
  public String getFor(CommandSender cs, Map<String, String> params) {
    if (cs instanceof Player player) {
      return engine.getTranslationForUser(player.getUniqueId(), key, params);
    } else {
      return engine.getTranslationForUser(null, key, params);
    }
  }

  /**
   * Gets the translation as a {@link Component} for the given {@link CommandSender}
   *
   * @param cs            the {@link CommandSender} to get the translation for
   * @param isMiniMessage whether the Translation should be parsed as a {@link MiniMessage}
   * @return the translation as a {@link Component}
   */
  @Override
  public Component getComponentFor(CommandSender cs, boolean isMiniMessage) {
    if (isMiniMessage) {
      return MiniMessage.miniMessage().deserialize(getFor(cs, null));
    } else {
      return Component.text(getFor(cs, null));
    }
  }

  /**
   * Gets the translation as a {@link Component} for the given {@link CommandSender}
   *
   * @param cs            the {@link CommandSender} to get the translation for
   * @param params        the replacements to use
   * @param isMiniMessage whether the Translation should be parsed as a {@link MiniMessage}
   * @return the translation as a {@link Component}
   */
  @Override
  public Component getComponentFor(CommandSender cs, Map<String, String> params,
      boolean isMiniMessage) {
    if (cs instanceof Player player) {
      if (isMiniMessage) {
        return MiniMessage.miniMessage()
            .deserialize(engine.getTranslationForUser(player.getUniqueId(), key, params));
      } else {
        return Component.text(engine.getTranslationForUser(player.getUniqueId(), key, params));
      }
    } else {
      if (isMiniMessage) {
        return MiniMessage.miniMessage()
            .deserialize(engine.getTranslationForUser(null, key, params));
      } else {
        return Component.text(engine.getTranslationForUser(null, key, params));
      }
    }
  }

}
