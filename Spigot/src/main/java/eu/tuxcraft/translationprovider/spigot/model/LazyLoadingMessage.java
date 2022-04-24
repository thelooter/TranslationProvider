package eu.tuxcraft.translationprovider.spigot.model;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.spigot.TranslationProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Implementation of {@link Message} interface that lazily loads the message from the engine.
 *
 * @author thelooter
 * @see Message
 * @since 2.0.0
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class LazyLoadingMessage implements Message {

  TranslationProviderEngine engine = TranslationProvider.getEngine();

  String key;
  String prefixKey;

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

    }else{
      translation = engine.getTranslationForUser(null, key, params);
      prefix = engine.getTranslationForUser(null, prefixKey, params);
    }

    if (prefix == null) {
      prefix = "";
    }

    if (translation.contains("\n")){
      for (String line : translation.split("\n")) {
        cs.sendMessage(prefix + line);
      }
    }else{
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
    }else{
      return engine.getTranslationForUser(null, key, params);
    }
  }
}
