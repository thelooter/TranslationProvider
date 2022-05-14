package eu.tuxcraft.translationprovider.spigot.model;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.spigot.TranslationProvider;
import java.util.Map;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
  String prefixKey;


  /**
   * Constructs a new {@link LazyLoadingMessage} instance.
   *
   * @param key       the key of the message
   * @param prefixKey the key of the prefix
   * @since 2.0.0
   */
  public LazyLoadingMessage(String key, String prefixKey) {
    this.key = key;
    this.prefixKey = prefixKey;
  }

  /**
   * {@inheritDoc}
   *
   * @param cs {@inheritDoc}
   * @since 2.0.0
   */
  @Override
  public void sendTo(CommandSender cs) {
    sendTo(cs, Map.of("", ""));
  }

  @Override
  public void sendTo(CommandSender cs, Map<String, String> params) {

    String translation;
    String prefix;

    if (cs instanceof Player player) {
      translation = engine.getTranslationForUser(player.getUniqueId(), key, params);
      prefix = engine.getTranslationForUser(player.getUniqueId(), prefixKey, params);

    } else {
      translation = engine.getTranslationForUser(null, key, params);
      prefix = engine.getTranslationForUser(null, prefixKey, params);
    }

    if (prefix == null) {
      prefix = "";
    }

    if (translation.contains("\n")) {
      for (String line : translation.split("\n")) {
        cs.sendMessage(prefix + line);
      }
    } else {
      cs.sendMessage(prefix + translation);
    }

  }

  @Override
  public String getFor(CommandSender cs) {

    return getFor(cs, null);
  }

  @Override
  public String getFor(CommandSender cs, Map<String, String> params) {
    if (cs instanceof Player player) {
      return engine.getTranslationForUser(player.getUniqueId(), key, params);
    } else {
      return engine.getTranslationForUser(null, key, params);
    }
  }

  @Override
  public Component getComponentFor(CommandSender cs, boolean isMiniMessage) {
    if (isMiniMessage) {
      return MiniMessage.miniMessage().deserialize(getFor(cs, null));
    } else {
      return Component.text(getFor(cs, null));
    }
  }

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
