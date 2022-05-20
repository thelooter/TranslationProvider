package eu.tuxcraft.translationprovider.engine.database;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.model.Language;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * General helper class for the language database.
 *
 * @author thelooter
 * @since 2.1.0
 */
public class LanguageDatabaseHelper {

  Connection connection = TranslationProviderEngine.getInstance().getConnection();
  Logger logger;

  /**
   * Creates a new LanguageDatabaseHelper instance.
   *
   * @param logger The logger to use.
   * @since 2.1.0
   */
  public LanguageDatabaseHelper(Logger logger) {
    this.logger = logger;
  }

  /**
   * Creates the tables for the language database.
   *
   * @since 2.1.0
   */
  public void createTables() {
    try (PreparedStatement createStatement =
        connection.prepareStatement(
            "CREATE TABLE IF NOT EXISTS translation_languages (iso_code VARCHAR(2) PRIMARY KEY,"
                + " display_name VARCHAR(255), is_enabled BOOLEAN, is_default BOOLEAN)")) {
      createStatement.executeUpdate();
    } catch (SQLException exception) {
      logger.severe(ExceptionUtils.getStackTrace(exception));
    }
  }

  /**
   * Adds a new language to the database.
   *
   * @param language The {@link Language} to add.
   * @since 2.1.0
   */
  public void addLanguage(Language language) {
    try (PreparedStatement insertStatement =
        connection.prepareStatement(
            "INSERT INTO translation_languages (iso_code, display_name, is_enabled, is_default) VALUES (?,?,?,?)")) {
      insertStatement.setString(1, language.getIsoCode());
      insertStatement.setString(2, language.getDisplayName());
      insertStatement.setBoolean(3, language.isEnabled());
      insertStatement.setBoolean(4, language.isDefault());

      if (insertStatement.executeUpdate() == 0) {
        logger.warning("Language already exists, doing nothing!");
      }

    } catch (SQLException exception) {
      logger.severe(ExceptionUtils.getStackTrace(exception));
    }
  }

  /**
   * Deletes a language from the database.
   *
   * @param language The {@link Language} to delete.
   * @return True if the language was deleted, false otherwise.
   * @since 2.1.0
   */
  public boolean deleteLanguage(Language language) {
    try (PreparedStatement deleteStatement =
        connection.prepareStatement("DELETE FROM translation_languages WHERE iso_code = ?")) {

      deleteStatement.setString(1, language.getIsoCode());

      if (deleteStatement.executeUpdate() == 0) {
        logger.warning("No matching Language found, doing nothing!");
        return false;
      }

      return true;

    } catch (SQLException exception) {
      logger.severe(ExceptionUtils.getStackTrace(exception));
    }
    return false;
  }

  /**
   * Removes a language from the database.
   *
   * @param lang uage The {@link Language} to remove.
   * @since 2.1.0
   */
  public void removeLanguage(Language lang) {
    try (PreparedStatement deleteStatement =
        connection.prepareStatement("DELETE FROM translation_languages WHERE iso_code = ?")) {

      deleteStatement.setString(1, lang.getIsoCode());

      deleteStatement.executeUpdate();

    } catch (SQLException e) {
      TranslationProviderEngine.getInstance().getLogger().severe(ExceptionUtils.getStackTrace(e));
    }
  }

  /**
   * Gets the default language.
   *
   * @return The default {@link Language} or null if none is set.
   * @since 2.1.0
   */
  public Language getDefaultLanguage() {
    try (PreparedStatement selectStatement =
        connection.prepareStatement(
            "SELECT * FROM translation_languages WHERE is_default = true")) {

      try (ResultSet resultSet = selectStatement.executeQuery()) {
        if (resultSet.next()) {
          return Language.fromResultSet(resultSet);
        }
      }
    } catch (SQLException e) {
      TranslationProviderEngine.getInstance().getLogger().severe(ExceptionUtils.getStackTrace(e));
    }
    return null;
  }
}
