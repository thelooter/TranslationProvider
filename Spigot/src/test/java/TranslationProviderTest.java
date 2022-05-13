import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import eu.tuxcraft.translationprovider.spigot.TranslationProvider;
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
  }

  @Test
  void testOnDisable() {
    plugin.onDisable();
  }
}
