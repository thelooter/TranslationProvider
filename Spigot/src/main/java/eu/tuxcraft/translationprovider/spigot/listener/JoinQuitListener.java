package eu.tuxcraft.translationprovider.spigot.listener;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
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
public class JoinQuitListener implements Listener {

  TranslationProviderEngine engine;

  /**
   * Creates a new instance of the JoinQuitListener.
   *
   * @param engine The {@link TranslationProviderEngine} to use.
   * @since 2.0.0
   */
  public JoinQuitListener(TranslationProviderEngine engine) {
    this.engine = engine;
  }

  /**
   * Called when a player joins the server.
   *
   * @param event the event that occurred
   * @since 2.0.0
   */
  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    if (event.getPlayer().hasPlayedBefore()) {
      engine.loadTranslationsForUser(event.getPlayer().getUniqueId());
    } else {
      engine.setDefaultLanguage(event.getPlayer().getUniqueId());
    }
  }

  /**
   * Called when a player leaves the server.
   *
   * @param event the event that occurred
   * @since 2.0.0
   */
  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    engine.getUserLanguageCache().invalidateUser(event.getPlayer().getUniqueId());
  }
}
