package eu.tuxcraft.translationprovider.spigot.listener;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class JoinQuitListener implements Listener {



  TranslationProviderEngine engine;

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    engine.loadTranslationsForUser(event.getPlayer().getUniqueId());
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    engine.getUserLanguageCache().invalidateUser(event.getPlayer().getUniqueId());
  }
}
