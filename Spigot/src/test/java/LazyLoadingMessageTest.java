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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LazyLoadingMessageTest {

  static ServerMock server;
  static TranslationProvider plugin;

  static LazyLoadingMessage message;


  @BeforeAll
  public static void setUp() {

    server = MockBukkit.mock();
    plugin = MockBukkit.load(TranslationProvider.class);

    message = new LazyLoadingMessage("lobby.navigator.navigatorTitle", "lobby");

  }

  @Test
  void testLazyLoadingMessage() {
    assertThat(message, instanceOf(LazyLoadingMessage.class));
  }

  @Test
  void testGetFor() {
    PlayerMock player = new PlayerMock(server, "thelooter2204",
        UUID.fromString("08fbc97b-93cd-4f2a-9369-29e025136b08"));

    TranslationProvider.playerLanguage(player, Language.fromDisplayName("English"));

    assertThat(message.getFor(player), instanceOf(String.class));
    assertThat(message.getFor(player), equalToIgnoringCase("Navigator"));
  }
}
