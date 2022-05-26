import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.database.UserDatabaseHelper;
import eu.tuxcraft.translationprovider.engine.model.Language;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class UserDatabaseHelperTest {
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
  void testSetUserLanguageFirstTime() {
    UUID uuid = UUID.randomUUID();
    UserDatabaseHelper helper = new UserDatabaseHelper(uuid);
    helper.setUserLanguage(Language.fromDisplayName("English"));

    assertThat(helper.getUserLanguage(), equalTo(Language.fromDisplayName("English")));

    try (PreparedStatement deleteStatement =
        connection.prepareStatement("DELETE FROM translation_user WHERE player_uuid = ?")) {
      deleteStatement.setObject(1, uuid);
      deleteStatement.executeUpdate();
    } catch (SQLException e) {
      logger.severe(ExceptionUtils.getStackTrace(e));
    }

    assertThat(helper.getUserLanguage(), nullValue());
  }

  @AfterEach
  void tearDown() {
    try {
      if (!connection.isClosed()) {
        connection.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
