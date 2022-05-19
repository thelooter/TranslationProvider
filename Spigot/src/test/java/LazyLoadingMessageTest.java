import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.instanceOf;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import eu.tuxcraft.translationprovider.engine.model.Language;
import eu.tuxcraft.translationprovider.spigot.TranslationProvider;
import eu.tuxcraft.translationprovider.spigot.model.LazyLoadingMessage;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LazyLoadingMessageTest {

  ServerMock server;
  TranslationProvider plugin;

  LazyLoadingMessage message;

  @BeforeEach
  public void setUp() {

    server = MockBukkit.mock();
    plugin = MockBukkit.load(TranslationProvider.class);

    message = new LazyLoadingMessage("lobby.scoreBoard.rankTitle");
  }

  @Test
  void testLazyLoadingMessage() {
    assertThat(message, instanceOf(LazyLoadingMessage.class));
  }

  @Test
  void testGetFor() {
    PlayerMock player =
        new PlayerMock(
            server, "thelooter2204", UUID.fromString("08fbc97b-93cd-4f2a-9369-29e025136b08"));

    TranslationProvider.playerLanguage(player, Language.fromDisplayName("English"));

    assertThat(message.getFor(player), instanceOf(String.class));
    assertThat(message.getFor(player), equalToIgnoringCase("Rank:"));
  }

  @Test
  void testGetForConsoleSender() {

    assertThat(message.getFor(Bukkit.getConsoleSender()),equalToIgnoringCase("Rang:"));
  }

  @Test
  void testSendTo() {
    PlayerMock player =
        new PlayerMock(
            server, "thelooter2204", UUID.fromString("08fbc97b-93cd-4f2a-9369-29e025136b08"));

    server.addPlayer(player);

    TranslationProvider.playerLanguage(player, Language.fromDisplayName("English"));

    message.sendTo(player);

    player.assertSaid("Rank:");
  }

  @AfterEach
  public void tearDown() {

    PlayerMock player =
        new PlayerMock(
            server, "thelooter2204", UUID.fromString("08fbc97b-93cd-4f2a-9369-29e025136b08"));

    server.addPlayer(player);

    TranslationProvider.playerLanguage(player, Language.fromDisplayName("Deutsch"));

    MockBukkit.unmock();

  }
}
