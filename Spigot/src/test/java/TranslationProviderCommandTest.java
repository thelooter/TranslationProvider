import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import eu.tuxcraft.translationprovider.spigot.TranslationProvider;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TranslationProviderCommandTest {

  ServerMock server;
  TranslationProvider plugin;
  PlayerMock player;

  @BeforeEach
  public void setUp() {

    server = MockBukkit.mock();
    plugin = MockBukkit.load(TranslationProvider.class);

    player =
        new PlayerMock(
            server, "thelooter2204", UUID.fromString("08fbc97b-93cd-4f2a-9369-29e025136b08"));

    server.addPlayer(player);
  }

  @Test
  void testCommandWithoutArguments() {
    player.performCommand("tlp");

    player.assertSaid("Possible Options: reload, stats,clear, add");
  }

  @Test
  void testCommandReload() {

    player.performCommand("tlp reload");

    player.assertSaid("Reloading translations...");
    assertThat(
        TranslationProvider.getEngine().getTranslationCache().getCache().size(), equalTo(0L));
    assertThat(
        TranslationProvider.getEngine().getUserLanguageCache().getCache().size(), equalTo(0L));
  }

  @Test
  void testCommandStats() {

    player.performCommand("tlp stats");

    long translationCacheSize =
        TranslationProvider.getEngine().getTranslationCache().getCache().size();
    long languageCacheSize =
        TranslationProvider.getEngine().getUserLanguageCache().getCache().size();

    player.assertSaid("TranslationCache: " + translationCacheSize);
    player.assertSaid("LanguageCache: " + languageCacheSize);
  }

  @Test
  void testCommandClear() {

    player.performCommand("tlp clear");

    long translationCacheSize =
        TranslationProvider.getEngine().getTranslationCache().getCache().size();
    long languageCacheSize =
        TranslationProvider.getEngine().getUserLanguageCache().getCache().size();

    player.assertSaid("Cleared Cache");
  }

  @Nested
  class Add {

    @BeforeEach
    public void setUp() {

      if (MockBukkit.isMocked()) {
        MockBukkit.unmock();
      }

      server = MockBukkit.mock();
      plugin = MockBukkit.load(TranslationProvider.class);

      player =
          new PlayerMock(
              server, "thelooter2204", UUID.fromString("08fbc97b-93cd-4f2a-9369-29e025136b08"));

      server.addPlayer(player);
    }

    @Test
    void testCommandAdd() {

      player.performCommand("tlp add");

      player.assertSaid("Possible Options: language, translation");
    }

    @Nested
    class AddLanguage {

      @BeforeEach
      void setUp() {

        if (MockBukkit.isMocked()) {
          MockBukkit.unmock();
        }

        server = MockBukkit.mock();
        plugin = MockBukkit.load(TranslationProvider.class);

        player =
            new PlayerMock(
                server, "thelooter2204", UUID.fromString("08fbc97b-93cd-4f2a-9369-29e025136b08"));

        server.addPlayer(player);
      }

      @Test
      void testCommandAddLanguageTwoArgs() {

        player.performCommand("tlp add language");

        player.assertSaid("§cUsage: /tlp add language iso_code display_name enabled default");
      }

      @Test
      void testCommandAddLanguageThreeArgs() {

        player.performCommand("tlp add language en_US");

        player.assertSaid("§cUsage: /tlp add language iso_code display_name enabled default");
      }

      @Test
      void testCommandAddLanguageFourArgs() {

        player.performCommand("tlp add language en_US English");

        player.assertSaid("§cUsage: /tlp add language iso_code display_name enabled default");
      }

      @Test
      void testCommandAddLanguageFiveArgs() {

        player.performCommand("tlp add language en_US English true");

        player.assertSaid("§cUsage: /tlp add language iso_code display_name enabled default");
      }

      @Test
      void testCommandAddLanguage() {

        player.performCommand("tlp add language GB British true false");

        player.assertSaid("§aLanguage added");

        TranslationProvider.getEngine()
            .removeLanguage(
                eu.tuxcraft.translationprovider.engine.model.Language.fromDisplayName("British"));
      }

      @Test
      void testCommandAddLanguageWithIsoAlreadyExists() {

        player.performCommand("tlp add language DE English true false");

        player.assertSaid("§cLanguage with iso code DE already exists");
      }

      @Test
      void testCommandAddLanguageWithDisplayNameAlreadyExists() {

        player.performCommand("tlp add language en_US English true false");

        player.assertSaid("§cLanguage with display name English already exists");
      }

      @Test
      void testCommandAddLanguageWithDefaultAlreadyExists() {

        player.performCommand("tlp add language ET Estonian true true");

        player.assertSaid("§cThere is already a default language");
      }
    }

    @AfterEach
    void tearDown() {
      MockBukkit.unmock();
    }
  }

  @AfterEach
  public void tearDown() {
    MockBukkit.unmock();
  }
}
