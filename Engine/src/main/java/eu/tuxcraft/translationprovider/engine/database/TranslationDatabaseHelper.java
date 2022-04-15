package eu.tuxcraft.translationprovider.engine.database;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.model.Language;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TranslationDatabaseHelper {

  Connection connection = TranslationProviderEngine.getInstance().getConnection();
  Language language;

  public Map<String, String> getTranslationsForProjectKey(String projectKey) {
    AtomicReference<Map<String, String>> translations = new AtomicReference<>();
    TranslationProviderEngine.getInstance()
        .getThreadPool()
        .execute(
            () -> {
              try (PreparedStatement preparedStatement =
                  connection.prepareStatement(
                      "SELECT translation_key,translation FROM translation_entries WHERE lang_id = ? AND translation_key LIKE ?")) {
                preparedStatement.setString(1, language.getIsoCode());
                preparedStatement.setString(2, projectKey + ".%");

                execute(translations, preparedStatement);

              } catch (SQLException e) {
                TranslationProviderEngine.getInstance()
                    .getLogger()
                    .severe(ExceptionUtils.getStackTrace(e));
              }
            });
    return translations.get();
  }

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

  public Map<String, String> getTranslations() {
    AtomicReference<Map<String, String>> translations = new AtomicReference<>();
    TranslationProviderEngine.getInstance()
        .getThreadPool()
        .execute(
            () -> {
              try (PreparedStatement preparedStatement =
                  connection.prepareStatement(
                      "SELECT translation_key,translation FROM translation_entries WHERE lang_id = ?")) {
                preparedStatement.setString(1, language.getIsoCode());

                execute(translations, preparedStatement);

              } catch (SQLException e) {
                TranslationProviderEngine.getInstance()
                    .getLogger()
                    .severe(ExceptionUtils.getStackTrace(e));
              }
            });
    return translations.get();
  }
}
