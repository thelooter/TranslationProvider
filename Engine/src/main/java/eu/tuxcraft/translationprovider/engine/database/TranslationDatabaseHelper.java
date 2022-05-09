package eu.tuxcraft.translationprovider.engine.database;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.model.Language;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Utility class for database operations.
 *
 * @author thelooter
 * @since 2.0.0
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TranslationDatabaseHelper {

  Connection connection = TranslationProviderEngine.getInstance().getConnection();
  Language language;

  /**
   * Creates a new {@link TranslationDatabaseHelper} instance.
   *
   * @param language The {@link Language} to use.
   * @since 2.0.0
   */
  public TranslationDatabaseHelper(Language language) {
    this.language = language;
  }

  /**
   * Returns the translation for the given Project Key and the {@link Language} specified in the
   * constructor.
   *
   * @param projectKey The Project Key.
   * @return The translation for the given Project Key.
   * @since 2.0.0
   */
  public Map<String, String> getTranslationsForProjectKey(String projectKey) {
    AtomicReference<Map<String, String>> translations = new AtomicReference<>();
    try (PreparedStatement preparedStatement =
        connection.prepareStatement(
            "SELECT translation_key,translation FROM translation_entries WHERE lang_id = ? AND translation_key LIKE ?")) {
      preparedStatement.setString(1, language.getIsoCode());
      preparedStatement.setString(2, projectKey + ".%");

      execute(translations, preparedStatement);

    } catch (SQLException e) {
      TranslationProviderEngine.getInstance().getLogger().severe(ExceptionUtils.getStackTrace(e));
    }
    return translations.get();
  }

  /**
   * Utility method to execute the prepared statement and return the result.
   *
   * @param translations      The {@link AtomicReference} to store the result.
   * @param preparedStatement The {@link PreparedStatement} to execute.
   * @since 2.0.0
   */
  private void execute(
      AtomicReference<Map<String, String>> translations, PreparedStatement preparedStatement) {
    try (ResultSet resultSet = preparedStatement.executeQuery()) {
      Map<String, String> translationMap = new HashMap<>();
      while (resultSet.next()) {
        String key = resultSet.getString("translation_key");
        String value = resultSet.getString("translation");

        translationMap.put(key, value);
      }

      translations.set(translationMap);
    } catch (SQLException e) {
      translations.set(Collections.emptyMap());
    }
  }

  /**
   * Returns the translation for the {@link Language} specified in the constructor.
   *
   * @return The translation for the {@link Language} specified in the constructor.
   * @since 2.0.0
   */
  public Map<String, String> getTranslations() {
    AtomicReference<Map<String, String>> translations = new AtomicReference<>();
    try (PreparedStatement preparedStatement =
        connection.prepareStatement(
            "SELECT translation_key,translation FROM translation_entries WHERE lang_id = ?")) {
      preparedStatement.setString(1, language.getIsoCode());

      execute(translations, preparedStatement);

    } catch (SQLException e) {
      TranslationProviderEngine.getInstance().getLogger().severe(ExceptionUtils.getStackTrace(e));
    }
    return translations.get();
  }

  /**
   * Adds the given translation to the database.
   * @param key The key to add.
   * @param value The value to add.
   * @return True if the translation was added, false otherwise.
   *
   * @since 2.1.0
   */
  public boolean addTranslation(String key, String value) {
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        "INSERT INTO translation_entries (lang_id, translation_key, translation) VALUES (?, ?, ?)")) {

      preparedStatement.setString(1, language.getIsoCode());
      preparedStatement.setString(2, key);
      preparedStatement.setString(3, value);

      return preparedStatement.executeUpdate() > 0;

    } catch (SQLException e) {
      TranslationProviderEngine.getInstance().getLogger().severe(ExceptionUtils.getStackTrace(e));
    }
    return false;
  }

}
