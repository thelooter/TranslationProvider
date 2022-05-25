import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.model.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class TranslationCacheTest {

  TranslationProviderEngine engine;

  private static final String DB_URL = "jdbc:postgresql://10.1.3.120:5432/tuxcraft-test";
  private static final String DB_USER = "tuxcraft-dev";
  private static final String DB_PASSWORD = "tuxcraft-dev";

  Logger logger = Logger.getLogger(TranslationCacheTest.class.getName());

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
  void testInvalidateCaches() {
    engine.getTranslationCache().getCache().getUnchecked(Language.fromDisplayName("English"));

    assertThat(engine.getTranslationCache().getCache().size(), is(equalTo(1L)));

    engine.getTranslationCache().invalidateLanguage(Language.fromDisplayName("English"));

    assertThat(engine.getTranslationCache().getCache().size(), is(equalTo(0L)));
  }
}