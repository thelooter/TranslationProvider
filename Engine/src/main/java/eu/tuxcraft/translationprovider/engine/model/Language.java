package eu.tuxcraft.translationprovider.engine.model;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.exceptions.LanguageException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Language {

  String isoCode;
  String displayName;
  boolean enabled;
  boolean isDefault;

  public Language(String isoCode, String displayName, boolean enabled, boolean defaultLanguage) {
    this.isoCode = isoCode;
    this.displayName = displayName;
    this.enabled = enabled;
    this.isDefault = defaultLanguage;
  }

  static Language fromResultSet(ResultSet resultSet) {
    try {
      return new Language(
          resultSet.getString("iso_code"),
          resultSet.getString("display_name"),
          resultSet.getBoolean("enabled"),
          resultSet.getBoolean("default_language"));
    } catch (Exception e) {
      throw new LanguageException("Error while creating language from result set", e);
    }
  }

  public static List<Language> getAvailableLanguages() {

    List<Language> languages = new ArrayList<>();

    TranslationProviderEngine.getInstance()
        .getThreadPool()
        .execute(
            () -> {
              try (PreparedStatement statement =
                  TranslationProviderEngine.getInstance()
                      .getConnection()
                      .prepareStatement(
                          "SELECT iso_code, display_name, is_enabled, is_default FROM translation_languages ORDER BY iso_code ASC")) {

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                  languages.add(Language.fromResultSet(resultSet));
                }
              } catch (SQLException e) {
                TranslationProviderEngine.getInstance()
                    .getLogger()
                    .severe(ExceptionUtils.getStackTrace(e));
              }
            });
    return languages;
  }

  public static Language getDefaultLanguage() {
    return getAvailableLanguages().stream()
        .filter(Language::isDefault)
        .findFirst()
        .orElseThrow(() -> new LanguageException("No default language found"));
  }

  @Override
  public String toString() {
    return "Language{"
        + "isoCode='"
        + isoCode
        + '\''
        + ", displayName='"
        + displayName
        + '\''
        + ", enabled="
        + enabled
        + ", defaultLanguage="
        + isDefault
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Language language = (Language) o;
    return enabled == language.enabled
        && isDefault == language.isDefault
        && isoCode.equals(language.isoCode)
        && displayName.equals(language.displayName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isoCode, displayName, enabled, isDefault);
  }
}
