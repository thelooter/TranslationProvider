package eu.tuxcraft.translationprovider.engine.database;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.model.Language;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
   * Adds a new language to the database.
   *
   * @param language The {@link Language} to add.
   * @return True if the language was added, false otherwise.
   * @since 2.1.0
   */
  public boolean addLanguage(Language language) {
    try (PreparedStatement insertStatement =
        connection.prepareStatement(
            "INSERT INTO translation_languages (iso_code, display_name, is_enabled, is_default) VALUES (?,?,?,?)")) {
      insertStatement.setString(1, language.getIsoCode());
      insertStatement.setString(2, language.getDisplayName());
      insertStatement.setBoolean(3, language.isEnabled());
      insertStatement.setBoolean(4, language.isDefault());

      if (insertStatement.executeUpdate() == 0) {
        logger.warning("Language already exists, doing nothing!");
        return false;
      } else {
        return true;
      }

    } catch (SQLException exception) {
      logger.severe(ExceptionUtils.getStackTrace(exception));
    }
    return false;
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
}
