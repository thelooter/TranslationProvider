package eu.tuxcraft.translationprovider.engine;

import eu.tuxcraft.translationprovider.engine.cache.TranslationCache;
import eu.tuxcraft.translationprovider.engine.cache.UserLanguageCache;
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

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TranslationProviderEngine {
  @Getter Logger logger;
  @Getter Connection connection;

  @Getter UserLanguageCache userLanguageCache = new UserLanguageCache();
  @Getter TranslationCache translationCache = new TranslationCache();

  @Getter static TranslationProviderEngine instance;

  public TranslationProviderEngine(Logger logger, Connection connection) {
    this.logger = logger;
    this.connection = connection;

    instance = this;
  }

  public String getTranslationForUser(UUID uuid, String key, Map<String, String> parameters) {
    Language language = userLanguageCache.getUserLanguage(uuid);

    if (uuid == null) {
      return new TranslationUtil().translate(key, Language.getDefaultLanguage(), parameters);
    }
    if (language == null) return "";
    if (key == null || key.isEmpty() || key.isBlank()) return "";

    return new TranslationUtil().translate(key, language, parameters);
  }

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

  public void loadTranslationsForUser(UUID uuid) {
    translationCache.getCache().getUnchecked(userLanguageCache.getUserLanguage(uuid));
  }

  public void playerLanguage(UUID player, Language language) {
    userLanguageCache.getCache().put(player, language);
    new UserDatabaseHelper(player).setUserLanguage(language);
  }

  public Language playerLanguage(UUID player) {
    return userLanguageCache.getUserLanguage(player);
  }
}
