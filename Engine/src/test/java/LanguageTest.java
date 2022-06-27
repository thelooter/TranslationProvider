import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.database.LanguageDatabaseHelper;
import eu.tuxcraft.translationprovider.engine.exceptions.LanguageException;
import eu.tuxcraft.translationprovider.engine.model.Language;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class LanguageTest {

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
  void testGetLanguageFromDisplayNameWith() {
    assertThrows(
        LanguageException.class,
        () -> {
          Language.fromDisplayName("NotExisting");
        });
  }

  @Test
  void testGetDefaultLanguageWithNoDefaultLanguage() {
    LanguageDatabaseHelper languageDatabaseHelper =
        new LanguageDatabaseHelper(Logger.getLogger("LanguageDatabaseHelperTest"));

    languageDatabaseHelper.editLanguageDefault(Language.getDefaultLanguage(), false);

    assertThrows(LanguageException.class, Language::getDefaultLanguage);

    languageDatabaseHelper.editLanguageDefault(Language.fromDisplayName("Deutsch"), true);
  }

  @AfterEach
  void tearDown() {
    try {
      connection.close();
    } catch (SQLException e) {
      logger.severe(ExceptionUtils.getStackTrace(e));
    }
  }
}
