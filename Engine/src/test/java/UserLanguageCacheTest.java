import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UserLanguageCacheTest {
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
    void testInvalidateCaches() {
        engine.getUserLanguageCache().getCache().getUnchecked(UUID.fromString("82b9b78e-e807-478e-b212-1c53c4cd1cfd"));

        assertThat(engine.getUserLanguageCache().getCache().size(), is(equalTo(1L)));

        engine.getUserLanguageCache().invalidateUser(UUID.fromString("82b9b78e-e807-478e-b212-1c53c4cd1cfd"));

        assertThat(engine.getUserLanguageCache().getCache().size(), is(equalTo(0L)));
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

