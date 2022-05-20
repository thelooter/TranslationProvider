import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.command.ConsoleCommandSenderMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import eu.tuxcraft.translationprovider.engine.model.Language;
import eu.tuxcraft.translationprovider.spigot.TranslationProvider;
import java.util.UUID;
import org.bukkit.Bukkit;
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

  @Test
  void testInvalidFirstArg() {

    player.performCommand("tlp invalid");

    player.assertSaid("Possible Options: reload, stats, clear, add, remove, help, version");
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

    @Nested
    class AddTranslation {

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
      void testCommandAddTranslationTwoArgs() {

        player.performCommand("tlp add translation");

        player.assertSaid("§cUsage: /tlp add translation <language> <key> <value>");
      }

      @Test
      void testCommandAddTranslationThreeArgs() {

        player.performCommand("tlp add translation en_US");

        player.assertSaid("§cUsage: /tlp add translation <language> <key> <value>");
      }

      @Test
      void testCommandAddTranslationFourArgs() {

        player.performCommand("tlp add translation en_US key");

        player.assertSaid("§cUsage: /tlp add translation <language> <key> <value>");
      }

      @Test
      void testCommandAddTranslation() {

        String translation =
            TranslationProvider.getEngine()
                .getTranslationCache()
                .getTranslation(Language.fromDisplayName("English"), "test.command.testChange");

        System.out.println(translation);

        TranslationProvider.getEngine()
            .removeTranslation(Language.fromDisplayName("English"), "test.command.testChange");

        player.performCommand("tlp add translation English test.command.testChange After");

        player.assertSaid("§a Translation added");
        TranslationProvider.getEngine().performReload();

        assertThat(
            TranslationProvider.getEngine()
                .getTranslationCache()
                .getTranslation(Language.fromDisplayName("English"), "test.command.testChange"),
            equalTo("After"));

        TranslationProvider.getEngine()
            .removeTranslation(Language.fromDisplayName("English"), "test.command.testChange");

        TranslationProvider.getEngine()
            .addTranslation(
                Language.fromDisplayName("English"), "test.command.testChange", translation);
      }

      @Test
      void testCommandAddTranslationWithLanguageNotExists() {

        player.performCommand("tlp add translation DE test.command.testChange After");

        player.assertSaid("§c Language not found");
      }

      @Test
      void testCommandAddTranslationWithNonExistingKey() {

        player.performCommand("tlp add translation English test.command.notExisting After");

        player.assertSaid("§c Key not found");
      }

      @AfterEach
      void tearDown() {
        MockBukkit.unmock();
      }
    }

    @AfterEach
    void tearDown() {
      MockBukkit.unmock();
    }
  }

  @Nested
  class Help {
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
    void testCommandHelp() {
      player.performCommand("tlp help");

      player.assertSaid(
          "§e---------§7[§bTranslationProvider Help§7]§e---------\n"
              + "/tlp help §8- §7Show this help§e\n"
              + "/tlp reload §8- §7Performs an Engine reload§e\n"
              + "/tlp clear §8- §7Invalidates all translations§e\n"
              + "/tlp stats §8- §7Shows the current engine stats§e\n"
              + "/tlp version §8- §7Shows the current engine version§e\n"
              + "/tlp add language <iso_code> <display_name> <enabled> <default> §8- §7Adds a new language§e\n"
              + "/tlp add translation <language> <key> <translation> §8- §7Adds a new Translation§e\n"
              + "----------------------------------------");
    }

    @Test
    void testCommandHelpWithInvalidCommand() {
      player.performCommand("tlp help test");

      player.assertSaid("§cUsage: /tlp help");
    }

    @Test
    void testCommandHelpToCommandSender() {
      ConsoleCommandSenderMock console = new ConsoleCommandSenderMock();

      Bukkit.dispatchCommand(console, "tlp help");

      console.assertSaid(
          "---------[TranslationProvider Help]---------\n"
              + "/tlp help - Show this help\n"
              + "/tlp reload - Performs an Engine reload\n"
              + "/tlp clear - Invalidates all translations\n"
              + "/tlp stats - Shows the current engine stats\n"
              + "/tlp version - Shows the current engine version\n"
              + "/tlp add language <iso_code> <display_name> <enabled> <default> - Adds a new language\n"
              + "/tlp add translation <language> <key> <translation> - Adds a new Translation\n"
              + "----------------------------------------");
    }

    @AfterEach
    void tearDown() {
      MockBukkit.unmock();
    }
  }

  @Nested
  class Version {
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
    void testCommandVersion() {
      player.performCommand("tlp version");

      player.assertSaid(
          "§eTranslationProvider Version: §b"
              + TranslationProvider.getInstance().getDescription().getVersion());
    }
  }

  @Nested
  class Remove {
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
    void testCommandRemoveOneArg() {
      player.performCommand("tlp remove");

      player.assertSaid("§cPossible Options: language, translation");
    }

    @Nested
    class RemoveLanguage {

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
      void testCommandRemoveLanguage() {
        player.performCommand("tlp remove language");

        player.assertSaid("§cUsage: /tp remove <language>");
      }

      @Test
      void testCommandRemoveLanguageNotExisting() {
        player.performCommand("tlp remove language notExisting");

        player.assertSaid("§cLanguage not found.");
      }

      @Test
      void testCommandRemoveLanguageWithLanguageExisting() {

        Language language = new Language("ET", "Eesti", true, true);

        TranslationProvider.getEngine().addLanguage(language);
        player.performCommand("tlp remove language Eesti");

        player.assertSaid("§aLanguage removed.");

        assertThat(Language.getAvailableLanguages().contains(language), equalTo(false));
      }
    }
  }

  @AfterEach
  public void tearDown() {
    MockBukkit.unmock();
  }
}
