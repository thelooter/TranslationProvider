import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToObject;
import static org.hamcrest.Matchers.nullValue;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import eu.tuxcraft.translationprovider.engine.model.Language;
import eu.tuxcraft.translationprovider.spigot.TranslationProvider;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JoinQuitListenerTest {

  ServerMock server;
  TranslationProvider plugin;

  @BeforeEach
  public void setUp() {

    server = MockBukkit.mock();
    plugin = MockBukkit.load(TranslationProvider.class);
  }

  @Test
  void testNewJoin() {
    PlayerMock playerMock = new PlayerMock(server, "Player0", UUID.randomUUID());

    PlayerJoinEvent playerJoinEvent =
        new PlayerJoinEvent(
            playerMock, String.format("%s has joined the server.", playerMock.getDisplayName()));

    Bukkit.getPluginManager().callEvent(playerJoinEvent);

    assertThat(
        TranslationProvider.playerLanguage(playerMock),
        equalToObject(Language.getDefaultLanguage()));
  }

  @Test
  void testExistingJoin() {
    PlayerMock playerMock =
        new PlayerMock(
            server, "thelooter2204", UUID.fromString("08fbc97b-93cd-4f2a-9369-29e025136b08"));

    playerMock.setLastPlayed(System.currentTimeMillis());

    server.addPlayer(playerMock);

    assertThat(
        TranslationProvider.playerLanguage(playerMock),
        equalToObject(Language.getDefaultLanguage()));
  }

  @Test
  void testPlayerQuit() {
    PlayerMock playerMock =
        new PlayerMock(
            server, "thelooter2204", UUID.fromString("08fbc97b-93cd-4f2a-9369-29e025136b08"));

    playerMock.setLastPlayed(System.currentTimeMillis());

    server.addPlayer(playerMock);

    playerMock.setLastPlayed(System.currentTimeMillis());

    PlayerQuitEvent playerJoinEvent =
        new PlayerQuitEvent(
            playerMock, String.format("%s has left the server.", playerMock.getDisplayName()));

    Bukkit.getPluginManager().callEvent(playerJoinEvent);

    assertThat(
        TranslationProvider.getEngine()
            .getUserLanguageCache()
            .getCache()
            .getIfPresent(playerMock.getUniqueId()),
        nullValue());
  }

  @AfterEach
  public void tearDown() {
    MockBukkit.unmock();
  }
}
