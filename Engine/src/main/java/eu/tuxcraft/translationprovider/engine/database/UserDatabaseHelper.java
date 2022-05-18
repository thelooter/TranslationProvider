package eu.tuxcraft.translationprovider.engine.database;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.exceptions.LanguageException;
import eu.tuxcraft.translationprovider.engine.model.Language;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * This class is responsible for all database operations regarding users.
 *
 * @author thelooter
 * @since 2.0.0
 */
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDatabaseHelper {

  final Connection connection = TranslationProviderEngine.getInstance().getConnection();
  UUID userUUID = null;

  /**
   * Creates a new instance of the {@link UserDatabaseHelper} class.
   *
   * @param userUUID The {@link UUID} of the user.
   * @since 2.0.0
   */
  public UserDatabaseHelper(UUID userUUID) {
    this.userUUID = userUUID;
  }

  /**
   * Creates the Tables for the User Database.
   *
   * @since 2.1.0
   */
  public void createTables() {
    try (PreparedStatement statement =
        connection.prepareStatement(
            "CREATE TABLE IF NOT EXISTS translation_user (player_uuid VARCHAR(36) PRIMARY KEY, lang_id VARCHAR(6))")) {
      statement.executeUpdate();
    } catch (SQLException e) {
      TranslationProviderEngine.getInstance().getLogger().severe(ExceptionUtils.getStackTrace(e));
    }
  }

  /**
   * Returns the {@link Language} of the user.
   *
   * @return The {@link Language} of the user.
   * @since 2.0.0
   */
  public Language getUserLanguage() {

    List<Language> languages = Language.getAvailableLanguages();

    AtomicReference<Language> language = new AtomicReference<>();

    try (PreparedStatement statement =
        connection.prepareStatement("SELECT lang_id FROM translation_user WHERE player_uuid = ?")) {

      statement.setObject(1, userUUID);

      String langId = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          langId = resultSet.getString("lang_id");
        }
      }

      String finalLangId = langId;
      languages.stream()
          .filter(l -> l.getIsoCode().equals(finalLangId))
          .findFirst()
          .ifPresent(language::set);

    } catch (SQLException e) {
      TranslationProviderEngine.getInstance().getLogger().severe(ExceptionUtils.getStackTrace(e));
    }
    return language.get();
  }

  /**
   * Sets the {@link Language} of the user.
   *
   * @param language The {@link Language} of the user.
   * @since 2.0.0
   */
  public void setUserLanguage(Language language) {
    try (PreparedStatement statement =
        connection.prepareStatement(
            "UPDATE translation_user SET lang_id = ? WHERE player_uuid = ?")) {

      statement.setObject(1, language.getIsoCode());
      statement.setObject(2, userUUID);

      if (statement.executeUpdate() == 0) {
        throw new LanguageException(
            "Could not update language for user " + userUUID + "! Probably not inserted yet!");
      }

    } catch (SQLException e) {
      TranslationProviderEngine.getInstance().getLogger().severe(ExceptionUtils.getStackTrace(e));
    }
  }
}
