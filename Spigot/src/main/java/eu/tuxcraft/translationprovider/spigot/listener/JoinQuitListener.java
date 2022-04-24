package eu.tuxcraft.translationprovider.spigot.listener;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listener for player join and quit events.
 *
 * @author thelooter
 * @since 2.0.0
 */
@AllArgsConstructor
public class JoinQuitListener implements Listener {



  TranslationProviderEngine engine;

  /**
   * Called when a player joins the server.
   *
   * @param event the event that occurred
   *
   * @since 2.0.0
   */
  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    engine.loadTranslationsForUser(event.getPlayer().getUniqueId());
  }

  /**
   * Called when a player leaves the server.
   *
   * @param event the event that occurred
   *
   * @since 2.0.0
   */
  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    engine.getUserLanguageCache().invalidateUser(event.getPlayer().getUniqueId());
  }
}
