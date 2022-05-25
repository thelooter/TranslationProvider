import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.model.Language;
import eu.tuxcraft.translationprovider.engine.translation.TranslationUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TranslationUtilTest {

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
  void testTranslateWithNonExistingKey() {
    String translation =
        new TranslationUtil().translate("NotExisting", Language.getDefaultLanguage(), null);

    assertThat(translation, equalTo("NotExisting"));
  }

  @Test
  void testTranslateWithNonExistingKeysAndParameters() {
    String translation =
        new TranslationUtil().translate(
                "NotExisting", Language.getDefaultLanguage(), Map.of("param", "value"));

    System.out.println(translation);

    assertThat(translation, equalTo("NotExisting {param: value}"));
  }

  @Test
  void testTranslateWithNullParameters() {
    String key = "test.testKeyWithNullParams";

    String translationForUser =
            new TranslationUtil().translate(key, Language.fromDisplayName("Deutsch"), null);


    assertThat(translationForUser, equalTo("TestKey: ♥"));
  }

  @Test
  void testTranslateWithEmptyParametersAndNonExistingKey() {
    String translation =
        new TranslationUtil().translate(
                "NotExisting", Language.getDefaultLanguage(), Collections.emptyMap());

    assertThat(translation, equalTo("NotExisting"));
  }

  @Test
  void testTranslateWithMultipleParametersAndNonExistingKey() {

    LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    parameters.put("param1", "value1");
    parameters.put("param2", "value2");

    String translation =
        new TranslationUtil().translate(
                "NotExisting", Language.getDefaultLanguage(),parameters );

    assertThat(translation, equalTo("NotExisting {param1: value1, param2: value2}"));
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
