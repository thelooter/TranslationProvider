import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.database.LanguageDatabaseHelper;
import eu.tuxcraft.translationprovider.engine.model.Language;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LanguageDatabaseHelperTest {
  TranslationProviderEngine engine;
  private static final String DB_URL = "jdbc:postgresql://10.1.3.120:5432/tuxcraft-test";
  private static final String DB_USER = "tuxcraft-dev";
  private static final String DB_PASSWORD = "tuxcraft-dev";

  Logger logger = Logger.getLogger(TranslationProviderEngineTest.class.getName());

  Connection connection = null;

  @BeforeEach
  void setUp() {

    try {
      Class.forName("org.postgresql.Driver");
      connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }

    engine = new TranslationProviderEngine(logger, connection);
  }

  @Test
  void testDeleteLanguage() {
    LanguageDatabaseHelper languageDatabaseHelper =
        new LanguageDatabaseHelper(Logger.getLogger("LanguageDatabaseHelperTest"));

    languageDatabaseHelper.addLanguage(new Language("Lang", "TL", true, false));

    assertThat(Language.getAvailableLanguages(), hasItem(new Language("Lang", "TL", true, false)));

    languageDatabaseHelper.deleteLanguage(new Language("Lang", "TL", true, false));

    assertThat(
        Language.getAvailableLanguages(), not(hasItem(new Language("Lang", "TL", true, false))));
  }

  @Test
  void testDeleteLanguageWithNonExistingLanguage() {
    LanguageDatabaseHelper languageDatabaseHelper =
        new LanguageDatabaseHelper(Logger.getLogger("LanguageDatabaseHelperTest"));

    languageDatabaseHelper.deleteLanguage(new Language("Lang", "TL", true, false));

    assertThat(
        Language.getAvailableLanguages(), not(hasItem(new Language("Lang", "TL", true, false))));
  }

  @Test
  void testAddLanguageWithExistingLanguage() {
    Language language = Language.getDefaultLanguage();

    LanguageDatabaseHelper languageDatabaseHelper =
        new LanguageDatabaseHelper(Logger.getLogger("LanguageDatabaseHelperTest"));

    languageDatabaseHelper.addLanguage(language);

    assertThat(Language.getAvailableLanguages(), hasItem(language));
  }

  @Test
  void testGetDefaultLanguageWithNoDefaultLanguage() {
    LanguageDatabaseHelper languageDatabaseHelper =
        new LanguageDatabaseHelper(Logger.getLogger("LanguageDatabaseHelperTest"));

    languageDatabaseHelper.editLanguageDefault(Language.getDefaultLanguage(), false);

    assertThat(languageDatabaseHelper.getDefaultLanguage(), nullValue());

    languageDatabaseHelper.editLanguageDefault(Language.fromDisplayName("Deutsch"), true);
  }

  @Test
  void testEditLanguageDisplayName() {
    LanguageDatabaseHelper languageDatabaseHelper =
            new LanguageDatabaseHelper(Logger.getLogger("LanguageDatabaseHelperTest"));

    languageDatabaseHelper.editLanguageDisplayName(Language.fromDisplayName("English"), "American");

    assertThat(Language.fromDisplayName("American").getDisplayName(), equalTo("American"));

    languageDatabaseHelper.editLanguageDisplayName(Language.fromDisplayName("American"), "English");
  }

  @Test
  void testEditLanguageIsoCode() {
    LanguageDatabaseHelper languageDatabaseHelper =
            new LanguageDatabaseHelper(Logger.getLogger("LanguageDatabaseHelperTest"));

    languageDatabaseHelper.editLanguageIsoCode(Language.fromDisplayName("English"), "US");

    assertThat(Language.fromDisplayName("English").getIsoCode(), equalTo("US"));

    languageDatabaseHelper.editLanguageIsoCode( Language.fromDisplayName("English"), "EN");
  }

  @Test
  void testEditLanguageEnabled() {
    assertThat(Language.fromDisplayName("English").isEnabled(), equalTo(true));
    LanguageDatabaseHelper languageDatabaseHelper =
            new LanguageDatabaseHelper(Logger.getLogger("LanguageDatabaseHelperTest"));

    languageDatabaseHelper.editLanguageEnabled(Language.fromDisplayName("English"), false);

    assertThat(Language.fromDisplayName("English").isEnabled(), equalTo(false));

    languageDatabaseHelper.editLanguageEnabled(Language.fromDisplayName("English"), true);
  }

  @AfterEach
  void tearDown() {
    try {
      if (!connection.isClosed()) {
        connection.close();
      }
    } catch (SQLException e) {
      logger.severe(ExceptionUtils.getStackTrace(e));
    }
  }
}
