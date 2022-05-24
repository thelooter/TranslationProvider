import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.model.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TranslationProviderEngineTest {

  TranslationProviderEngine engine;
  private static final String DB_URL = "jdbc:postgresql://10.1.3.120:5432/tuxcraft-test";
  private static final String DB_USER = "tuxcraft-dev";
  private static final String DB_PASSWORD = "tuxcraft-dev";

  Logger logger = Logger.getLogger(TranslationProviderEngineTest.class.getName());

  @BeforeEach
  void setUp() {

    Connection connection = null;

    try {
      Class.forName("org.postgresql.Driver");
      connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }

    engine = new TranslationProviderEngine(logger, connection);
  }

  @Test
  void testPerformReload() {
    engine.performReload();

    assertThat(engine.getTranslationCache().getCache().size(), equalTo(0L));
    assertThat(engine.getUserLanguageCache().getCache().size(), equalTo(0L));
  }

  @Test
  @Disabled("Disabled until edit Language Feature is added")
  void testReloadWithNoDefaultLanguage() {
    engine.removeLanguage(Language.getDefaultLanguage());
    engine.performReload();

    assertThrows(IllegalStateException.class, () -> engine.performReload());
  }

  @Test
  void testGetTranslationForUser() {
    UUID uuid = UUID.fromString("82b9b78e-e807-478e-b212-1c53c4cd1cfd");
    String key = "test.testKey";

    String translationForUser = engine.getTranslationForUser(uuid, key, null);

    assertThat(translationForUser, equalTo("TestKey"));
  }

  @Test
  void testGetTranslationForConsoleSender() {
    String key = "test.testKey";

    String translationForUser = engine.getTranslationForUser(null, key, null);

    assertThat(translationForUser, equalTo("TestKey"));
  }

  @Test
  void testGetTranslationWithNullKey() {
    UUID uuid = UUID.fromString("82b9b78e-e807-478e-b212-1c53c4cd1cfd");

    String translationForUser = engine.getTranslationForUser(uuid, null, null);

    assertThat(translationForUser, equalTo(""));
  }

  @Test
  void testGetTranslationWithEmptyKey() {
    UUID uuid = UUID.fromString("82b9b78e-e807-478e-b212-1c53c4cd1cfd");

    String translationForUser = engine.getTranslationForUser(uuid, "", null);

    assertThat(translationForUser, equalTo(""));
  }

  @Test
  void testGetTranslationWithBlankKey() {
    String translationForUser =
        engine.getTranslationForUser(
            UUID.fromString("82b9b78e-e807-478e-b212-1c53c4cd1cfd"), " ", null);

    assertThat(translationForUser, equalTo(""));
  }

  @Test
  void testGetTranslationWithNullLanguage() {
    String translationForUser =
        engine.getTranslationForUser(UUID.randomUUID(), "test.testKey", null);

    assertThat(translationForUser, equalTo(""));
  }

  @Test
  void testGetLanguageForUser() {
    UUID uuid = UUID.fromString("82b9b78e-e807-478e-b212-1c53c4cd1cfd");

    Language languageForUser = engine.playerLanguage(uuid);

    assertThat(languageForUser, equalToObject(Language.fromDisplayName("Deutsch")));
  }

  @Test
  void testGetLanguageForConsoleSender() {
    Language languageForUser = engine.playerLanguage(null);

    assertThat(languageForUser, equalToObject(Language.getDefaultLanguage()));
  }

  @Test
  void testSetLanguageForUser() {
    UUID uuid = UUID.fromString("82b9b78e-e807-478e-b212-1c53c4cd1cfd");
    Language language = Language.fromDisplayName("English");

    engine.playerLanguage(uuid, language);

    Language languageForUser = engine.playerLanguage(uuid);

    assertThat(languageForUser, equalToObject(language));
  }

  @Test
  void testSetLanguageForConsoleSender() {
    Language language = Language.fromDisplayName("English");
    assertThrows(IllegalStateException.class, () -> engine.playerLanguage(null, language));
  }

  @Test
  void testAddTranslation() {
    String key = "test.testAddTranslation";
    String translation = "TestAddTranslation";

    try {
      Map<String, String> translations =
          engine.getTranslationCache().getCache().get(Language.fromDisplayName("Deutsch"));

      assertThat(translations.get(key), equalTo(null));

      engine.addTranslation(Language.fromDisplayName("Deutsch"), key, translation);
      engine.performReload();
      translations =
          engine.getTranslationCache().getCache().get(Language.fromDisplayName("Deutsch"));

      assertThat(translations.get(key), equalTo(translation));
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }

    engine.removeTranslation(Language.fromDisplayName("Deutsch"), key);
  }

  @Test
  void testAddLanguage() {
    Language newLanguage = new Language("TL", "TestLanguage", true, false);

    engine.addLanguage(newLanguage);

    assertThat(Language.getAvailableLanguages(), hasItem(newLanguage));
  }

  @Test
  void testLoadTranslationsForUser() {
    UUID uuid = UUID.fromString("82b9b78e-e807-478e-b212-1c53c4cd1cfd");

    engine.performReload();
    engine.loadTranslationsForUser(uuid);

    assertThat(engine.getTranslationCache().getCache().size(), equalTo(1L));
  }
}
