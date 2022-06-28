package eu.tuxcraft.translationprovider.engine;

import eu.tuxcraft.translationprovider.engine.cache.TranslationCache;
import eu.tuxcraft.translationprovider.engine.cache.UserLanguageCache;
import eu.tuxcraft.translationprovider.engine.database.KeyDatabaseHelper;
import eu.tuxcraft.translationprovider.engine.database.LanguageDatabaseHelper;
import eu.tuxcraft.translationprovider.engine.database.TranslationDatabaseHelper;
import eu.tuxcraft.translationprovider.engine.database.UserDatabaseHelper;
import eu.tuxcraft.translationprovider.engine.model.Language;
import eu.tuxcraft.translationprovider.engine.translation.TranslationUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Engine for the translation provider. Provides methods for the Implementations to use.
 *
 * @author thelooter
 * @since 2.0.0
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TranslationProviderEngine {

  @Getter Logger logger;
  @Getter Connection connection;

  @Getter UserLanguageCache userLanguageCache = new UserLanguageCache();
  @Getter TranslationCache translationCache = new TranslationCache();

  @Getter static TranslationProviderEngine instance;

  /**
   * Gets the {@link TranslationProviderEngine} instance
   *
   * @param logger The {@link Logger} instance
   * @param connection The {@link Connection} instance
   * @since 2.0.0
   */
  public TranslationProviderEngine(Logger logger, Connection connection) {
    this.logger = logger;
    this.connection = connection;

    instance = this;

    new LanguageDatabaseHelper(logger).createTables();
    new TranslationDatabaseHelper().createTables();
    new UserDatabaseHelper().createTables();
    new KeyDatabaseHelper(logger).createTables();
  }

  /**
   * Gets a specific translation for the Player
   *
   * @param uuid The {@link UUID} of the Player
   * @param key The key of the translation
   * @param parameters The parameters for the translation
   * @return The translation
   * @since 2.0.0
   */
  public String getTranslationForUser(UUID uuid, String key, Map<String, String> parameters) {

    if (uuid == null) {
      return new TranslationUtil().translate(key, Language.getDefaultLanguage(), parameters);
    }

    if (key == null || key.isEmpty() || key.isBlank()) {
      return "";
    }

    Language language = userLanguageCache.getUserLanguage(uuid);

    if (language == null) {
      return "";
    }

    return new TranslationUtil().translate(key, language, parameters);
  }

  /**
   * Reloads all the Caches
   *
   * @since 2.0.0
   */
  public void performReload() {
    getLogger().info("Starting TranslationProvider reload");

    getTranslationCache().flush();
    getUserLanguageCache().flush();

    getLogger().info("Caches flushed");

    List<Language> availableLanguages = Language.getAvailableLanguages();


    if (availableLanguages.stream().noneMatch(Language::isDefault)) {
      throw new IllegalStateException("No default language found");
    }

    try {

      getTranslationCache().invalidateTranslationCache();
      getUserLanguageCache().invalidateUserLanguageCache();

    } catch (Exception e) {
      getLogger().warning(ExceptionUtils.getStackTrace(e));
      getLogger().severe("Error while reloading TranslationProvider");
    }
  }

  /**
   * Loads the {@link Language} for the Player
   *
   * @param uuid The {@link UUID} of the Player
   * @since 2.0.0
   */
  public void loadTranslationsForUser(UUID uuid) {
    translationCache.getCache().getUnchecked(userLanguageCache.getUserLanguage(uuid));
  }

  /**
   * Sets the {@link Language} for the Player
   *
   * @param uuid The player's {@link UUID}
   * @param language The new {@link Language}
   * @since 2.0.0
   */
  public void playerLanguage(UUID uuid, Language language) {
    if (uuid == null) {
      throw new IllegalArgumentException("Cannot change language of ConsoleCommandSender");
    }
    userLanguageCache.getCache().put(uuid, language);
    new UserDatabaseHelper(uuid).setUserLanguage(language);
  }

  /**
   * Gets the @{@link Language} of the Player
   *
   * @param uuid The Player's {@link UUID}
   * @return The {@link Language} of the Player
   * @since 2.0.0
   */
  public Language playerLanguage(UUID uuid) {
    if (uuid == null) {
      return Language.getDefaultLanguage();
    }
    return userLanguageCache.getUserLanguage(uuid);
  }

  /**
   * Adds a new {@link Language}
   *
   * @param language The new {@link Language}
   * @since 2.1.0
   */
  public void addLanguage(Language language) {
    new LanguageDatabaseHelper(logger).addLanguage(language);
  }

  /**
   * Registers a new Translation Key
   *
   * @param key The new Translation Key
   * @since 2.1.0
   */
  public void registerKey(String key) {
    if (key == null || key.isEmpty() || key.isBlank()) {
      throw new IllegalArgumentException("Cannot register an empty key");
    }
    new KeyDatabaseHelper(logger).registerKey(key);
  }

  /**
   * Unregisters a Translation Key
   *
   * @param key The Translation Key to unregister
   * @since 2.1.0
   */
  public void unregisterKey(String key) {
    if (key == null || key.isEmpty() || key.isBlank()) {
      throw new IllegalArgumentException("Cannot register an empty key");
    }
    new KeyDatabaseHelper(logger).unregisterKey(key);
  }

  /**
   * Gets all the registered Translation Keys
   *
   * @return A {@link List} of all the registered Translation Keys
   * @since 2.1.0
   */
  public List<String> getAllRegisteredKeys() {
    return new KeyDatabaseHelper(logger).getAllRegisteredKeys();
  }

  /**
   * Adds a new Translation in a given {@link Language} for a given Translation Key
   *
   * @param language The {@link Language} to add the Translation to
   * @param key The Translation Key
   * @param value The Translation
   * @since 2.1.0
   */
  public void addTranslation(Language language, String key, String value) {
    new TranslationDatabaseHelper(language).addTranslation(key, value);
  }

  /**
   * Removes a Language from the {@link TranslationProviderEngine}
   *
   * @param lang The {@link Language} to remove
   * @since 2.1.0
   */
  public void removeLanguage(Language lang) {
    if (lang == null) {
      throw new IllegalArgumentException("Language cannot be null");
    }
    new LanguageDatabaseHelper(logger).removeLanguage(lang);
  }

  /**
   * Sets the default {@link Language} for the given User
   *
   * @param uniqueId The {@link UUID} of the User
   * @since 2.1.0
   */
  public void setDefaultLanguage(UUID uniqueId) {
    new UserDatabaseHelper(uniqueId)
        .setUserLanguage(new LanguageDatabaseHelper(logger).getDefaultLanguage());
  }

  /**
   * Removes a Translation from a given {@link Language} for a given Translation Key
   *
   * @param language The {@link Language} to remove the Translation from
   * @param key The Translation Key
   * @since 2.1.0
   */
  public void removeTranslation(Language language, String key) {
    new TranslationDatabaseHelper(language).removeTranslation(key);
  }

  /**
   * Edits the Enabled Status of a Language
   *
   * @param lang The {@link Language} to edit
   * @param newState The new Enabled Status
   * @since 2.1.0
   */
  public void editLanguageEnabled(Language lang, boolean newState) {
    new LanguageDatabaseHelper(logger).editLanguageEnabled(lang, newState);
  }

  /**
   * Edits the Default Status of a Translation
   *
   * @param lang The {@link Language} to edit
   * @param value The new Default Status
   * @since 2.1.0
   */
  public void editLanguageDefault(Language lang, boolean value) {
    new LanguageDatabaseHelper(logger).editLanguageDefault(lang, value);
  }

  /**
   * Edits the Name of a Language
   *
   * @param lang The {@link Language} to edit
   * @param newDisplayName The new Name
   * @since 2.1.0
   */
  public void editLanguageDisplayName(Language lang, String newDisplayName) {
    new LanguageDatabaseHelper(logger).editLanguageDisplayName(lang, newDisplayName);
  }

  /**
   * Edits the Language Code of a Language
   *
   * @param lang The {@link Language} to edit
   * @param newIsoCode The new Language Code
   * @since 2.1.0
   */
  public void editLanguageIsoCode(Language lang, String newIsoCode) {
    new LanguageDatabaseHelper(logger).editLanguageIsoCode(lang, newIsoCode);
  }

  /**
   * Edits a given Translation
   * @param lang The {@link Language} of the Key
   * @param key The Key
   * @param newValue The new Value of the Key
   */
  public void editTranslation(Language lang, String key, String newValue) {
    new TranslationDatabaseHelper(lang).editTranslation(key, newValue);
  }
}
