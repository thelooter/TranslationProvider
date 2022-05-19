package eu.tuxcraft.translationprovider.engine.model;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.exceptions.LanguageException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Represents a Language.
 *
 * @author thelooter
 * @since 2.0.0
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Language {

  String isoCode;
  String displayName;
  boolean enabled;
  boolean isDefault;

  /**
   * Constructs a new Language.
   *
   * @param isoCode the ISO code of the language
   * @param displayName the display name of the language
   * @param enabled whether the language is enabled
   * @param defaultLanguage whether the language is the default language
   * @since 2.0.0
   */
  public Language(String isoCode, String displayName, boolean enabled, boolean defaultLanguage) {
    this.isoCode = isoCode;
    this.displayName = displayName;
    this.enabled = enabled;
    this.isDefault = defaultLanguage;
  }

  /**
   * Returns a list of all available languages.
   *
   * @return A {@link List} of all available Languages.
   * @since 2.0.0
   */
  public static List<Language> getAvailableLanguages() {

    List<Language> languages = new ArrayList<>();
    try (PreparedStatement statement =
        TranslationProviderEngine.getInstance()
            .getConnection()
            .prepareStatement(
                "SELECT iso_code, display_name, is_enabled, is_default FROM translation_languages ORDER BY iso_code ")) {

      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        languages.add(Language.fromResultSet(resultSet));
      }
    } catch (SQLException e) {
      TranslationProviderEngine.getInstance().getLogger().severe(ExceptionUtils.getStackTrace(e));
    }
    return languages;
  }

  /**
   * Returns the default language.
   *
   * @return The default {@link Language}.
   * @since 2.0.0
   */
  public static Language getDefaultLanguage() {
    return getAvailableLanguages().stream()
        .filter(Language::isDefault)
        .findFirst()
        .orElseThrow(() -> new LanguageException("No default language found"));
  }

  /**
   * Creates a {@link Language} from a {@link ResultSet}.
   *
   * @param resultSet The {@link ResultSet} to create the {@link Language} from.
   * @return The created {@link Language}.
   * @throws SQLException If an error occurs while creating the {@link Language}.
   * @since 2.0.0
   */
  public static Language fromResultSet(ResultSet resultSet) throws SQLException {

    return new Language(
        resultSet.getString("iso_code"),
        resultSet.getString("display_name"),
        resultSet.getBoolean("is_enabled"),
        resultSet.getBoolean("is_default"));
  }

  /**
   * Returns the {@link Language} with the given Display Name.
   *
   * @param displayName The Display Name of the {@link Language}.
   * @return The {@link Language} with the given Display Name.
   * @since 2.1.0
   */
  public static Language fromDisplayName(String displayName) {
    return getAvailableLanguages().stream()
        .filter(language -> language.getDisplayName().equals(displayName))
        .findFirst()
        .orElseThrow(
            () -> new LanguageException("No language found with display name " + displayName));
  }

  /**
   * Converts a {@link Language} to a {@link String}.
   *
   * @return The {@link Language} as a {@link String}.
   * @since 2.0.0
   */
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

  /**
   * Checks if two {@link Language}s are equal.
   *
   * @return True if the two {@link Language}s are equal, false otherwise.
   * @since 2.0.0
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Language language = (Language) o;
    return enabled == language.enabled
        && isDefault == language.isDefault
        && isoCode.equals(language.isoCode)
        && displayName.equals(language.displayName);
  }

  /**
   * Returns the hash code of the {@link Language}.
   *
   * @return The hash code of the {@link Language}.
   * @since 2.0.0
   */
  @Override
  public int hashCode() {
    return Objects.hash(isoCode, displayName, enabled, isDefault);
  }
}
