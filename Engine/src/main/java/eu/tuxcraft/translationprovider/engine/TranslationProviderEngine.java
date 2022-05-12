package eu.tuxcraft.translationprovider.engine;

import eu.tuxcraft.translationprovider.engine.cache.TranslationCache;
import eu.tuxcraft.translationprovider.engine.cache.UserLanguageCache;
import eu.tuxcraft.translationprovider.engine.database.KeyDatabaseHelper;
import eu.tuxcraft.translationprovider.engine.database.LanguageDatabaseHelper;
import eu.tuxcraft.translationprovider.engine.database.TranslationDatabaseHelper;
import eu.tuxcraft.translationprovider.engine.database.UserDatabaseHelper;
import eu.tuxcraft.translationprovider.engine.model.Language;
import eu.tuxcraft.translationprovider.engine.translation.TranslationUtil;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Engine for the translation provider. Provides methods for the Implementations to use.
 *
 * @author thelooter
 * @since 2.0.0
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TranslationProviderEngine {

  @Getter
  Logger logger;
  @Getter
  Connection connection;

  @Getter
  UserLanguageCache userLanguageCache = new UserLanguageCache();
  @Getter
  TranslationCache translationCache = new TranslationCache();

  @Getter
  static TranslationProviderEngine instance;

  /**
   * Gets the {@link TranslationProviderEngine} instance
   *
   * @param logger     The {@link Logger} instance
   * @param connection The {@link Connection} instance
   * @since 2.0.0
   */
  public TranslationProviderEngine(Logger logger, Connection connection) {
    this.logger = logger;
    this.connection = connection;

    instance = this;
  }

  /**
   * Gets a specific translation for the Player
   *
   * @param uuid       The {@link UUID} of the Player
   * @param key        The key of the translation
   * @param parameters The parameters for the translation
   * @return The translation
   * @since 2.0.0
   */
  public String getTranslationForUser(UUID uuid, String key, Map<String, String> parameters) {
    Language language = userLanguageCache.getUserLanguage(uuid);

    if (uuid == null) {
      return new TranslationUtil().translate(key, Language.getDefaultLanguage(), parameters);
    }
    if (language == null) {
      return "";
    }
    if (key == null || key.isEmpty() || key.isBlank()) {
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
    try {
      getLogger().info("Starting TranslationProvider reload");

      getTranslationCache().flush();
      getUserLanguageCache().flush();

      getLogger().info("Caches flushed");

      List<Language> availableLanguages = Language.getAvailableLanguages();

      getLogger().info("Loading translations for " + availableLanguages.size() + " languages");
      for (Language language : availableLanguages) {
        getLogger().info("Loading translations for " + language.getDisplayName());
      }

      if (availableLanguages.stream().noneMatch(Language::isDefault)) {
        throw new IllegalStateException("No default language found");
      }

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
   * @param player   The player's {@link UUID}
   * @param language The new {@link Language}
   * @since 2.0.0
   */
  public void playerLanguage(UUID player, Language language) {
    userLanguageCache.getCache().put(player, language);
    new UserDatabaseHelper(player).setUserLanguage(language);
  }

  /**
   * Gets the @{@link Language} of the Player
   *
   * @param player The Player's {@link UUID}
   * @return The {@link Language} of the Player
   * @since 2.0.0
   */
  public Language playerLanguage(UUID player) {
    return userLanguageCache.getUserLanguage(player);
  }

  /**
   * Adds a new {@link Language}
   *
   * @param language The new {@link Language}
   * @return True if the {@link Language} was added, false otherwise
   * @since 2.1.0
   */
  public boolean addLanguage(Language language) {
    return new LanguageDatabaseHelper(logger).addLanguage(language);
  }

  /**
   * Registers a new Translation Key
   *
   * @param key The new Translation Key
   * @return True if the Translation Key was registered, false otherwise
   * @since 2.1.0
   */
  public boolean registerKey(String key) {
    return new KeyDatabaseHelper(logger).registerKey(key);
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
   * @param key      The Translation Key
   * @param value    The Translation
   * @return True if the Translation was added, false otherwise
   * @since 2.1.0
   */
  public boolean addTranslation(Language language, String key, String value) {
    return new TranslationDatabaseHelper(language).addTranslation(key, value);
  }

  /**
   * Removes a Language from the {@link TranslationProviderEngine}
   *
   * @param lang The {@link Language} to remove
   * @return True if the {@link Language} was removed, false otherwise
   * @since 2.1.0
   */
  public boolean removeLanguage(Language lang) {
    return new LanguageDatabaseHelper(logger).removeLanguage(lang);
  }

  /**
   * Sets the default {@link Language} for the given User
   *
   * @param uniqueId The {@link UUID} of the User
   * @since 2.1.0
   */
  public void setDefaultLanguage(UUID uniqueId) {
    new UserDatabaseHelper(uniqueId).setUserLanguage(
        new LanguageDatabaseHelper(logger).getDefaultLanguage());
  }

  /**
   * Removes a Translation from a given {@link Language} for a given Translation Key
   *
   * @param language The {@link Language} to remove the Translation from
   * @param key      The Translation Key
   * @return True if the Translation was removed, false otherwise
   * @since 2.1.0
   */
  public boolean removeTranslation(Language language, String key) {
    return new TranslationDatabaseHelper(language).removeTranslation(key);
  }
}
