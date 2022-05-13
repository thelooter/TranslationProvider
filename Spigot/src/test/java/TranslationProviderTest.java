import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.model.Language;
import eu.tuxcraft.translationprovider.spigot.TranslationProvider;
import java.util.UUID;
import lombok.SneakyThrows;
import messages.Messages;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TranslationProviderTest {

  static ServerMock server;
  static TranslationProvider plugin;

  @BeforeAll
  static void setup() {
    server = MockBukkit.mock();
    plugin = MockBukkit.load(TranslationProvider.class);
  }

  @Test
  void test() {
    assertThat(plugin, notNullValue());
    assertThat(TranslationProvider.getEngine(), instanceOf(TranslationProviderEngine.class));
    assertThat(TranslationProvider.getInstance(), instanceOf(TranslationProvider.class));
  }

  @Test
  void testOnDisable() {
    plugin.onDisable();
  }

  @Test
  void testGetPlayerLanguage() {
    PlayerMock player = new PlayerMock(server, "thelooter2204",
        UUID.fromString("08fbc97b-93cd-4f2a-9369-29e025136b08"));

    TranslationProvider.playerLanguage(player, Language.fromDisplayName("English"));

    assertThat(TranslationProvider.playerLanguage(player), equalTo(Language.fromDisplayName("English")));
    
  }

  @SneakyThrows
  @Test
  void testMapTranslation() {
    TranslationProvider.mapAllTranslations(Messages.class, "test");
  }

  @SneakyThrows
  @Test
  void testInvalidKeyPrefix() {
    TranslationProvider.mapAllTranslations(Messages.class, "test.");
  }

  @AfterAll
  static void tearDown() {
    PlayerMock player = new PlayerMock(server, "thelooter2204",
        UUID.fromString("08fbc97b-93cd-4f2a-9369-29e025136b08"));

    TranslationProvider.playerLanguage(player, Language.fromDisplayName("Deutsch"));
    MockBukkit.unmock();
  }

}
