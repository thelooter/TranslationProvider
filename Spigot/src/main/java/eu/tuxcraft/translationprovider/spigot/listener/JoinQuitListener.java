package eu.tuxcraft.translationprovider.spigot.listener;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinQuitListener implements Listener {

  TranslationProviderEngine engine;

	public JoinQuitListener(TranslationProviderEngine engine) {
		this.engine = engine;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		//TODO Implement getting the Player Translations
	}
}

