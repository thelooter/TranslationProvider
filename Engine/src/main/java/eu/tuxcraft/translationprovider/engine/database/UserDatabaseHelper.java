package eu.tuxcraft.translationprovider.engine.database;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.exceptions.LanguageException;
import eu.tuxcraft.translationprovider.engine.model.Language;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDatabaseHelper {

  Connection connection = TranslationProviderEngine.getInstance().getConnection();
  UUID userUUID;

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
