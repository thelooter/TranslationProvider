package eu.tuxcraft.translationprovider.engine.database;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Database helper class for the key database.
 *
 * @author thelooter
 * @since 2.1.0
 */
public class KeyDatabaseHelper {

  Connection connection = TranslationProviderEngine.getInstance().getConnection();
  Logger logger;

  /**
   * Creates a new KeyDatabaseHelper instance.
   *
   * @param logger The {@link Logger} to use.
   * @since 2.1.0
   */
  public KeyDatabaseHelper(Logger logger) {
    this.logger = logger;
  }

  /**
   * Creates the tables for the key database.
   *
   * @since 2.1.0
   */
  public void createTables() {
    try (PreparedStatement preparedStatement =
        connection.prepareStatement(
            "CREATE TABLE IF NOT EXISTS translation_keys (key VARCHAR PRIMARY KEY)")) {

      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      logger.severe(ExceptionUtils.getStackTrace(e));
    }
  }

  /**
   * Inserts a new Key into the database.
   *
   * @param key The Key to insert.
   * @return True if the Key was inserted successfully, false otherwise.
   * @since 2.1.0
   */
  public boolean registerKey(String key) {
    try (PreparedStatement preparedStatement =
        connection.prepareStatement(
            "INSERT INTO translation_keys (key) VALUES (?) ON CONFLICT DO NOTHING")) {

      preparedStatement.setString(1, key);

      return preparedStatement.executeUpdate() > 0;
    } catch (SQLException e) {
      logger.severe(ExceptionUtils.getStackTrace(e));
    }
    return false;
  }

  /**
   * Returns all keys in the database.
   *
   * @return A {@link List} of all keys in the database.
   * @since 2.1.0
   */
  public List<String> getAllRegisteredKeys() {
    try (PreparedStatement preparedStatement =
        connection.prepareStatement("SELECT * FROM translation_keys")) {

      try (var resultSet = preparedStatement.executeQuery()) {
        List<String> keys = new java.util.ArrayList<>();

        while (resultSet.next()) {
          keys.add(resultSet.getString("key"));
        }

        return keys;
      }

    } catch (SQLException e) {
      TranslationProviderEngine.getInstance().getLogger().severe(ExceptionUtils.getStackTrace(e));
    }

    return Collections.emptyList();
  }
}
