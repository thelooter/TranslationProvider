package eu.tuxcraft.translationprovider.engine;

import eu.tuxcraft.translationprovider.engine.cache.TranslationCache;
import eu.tuxcraft.translationprovider.engine.cache.UserLanguageCache;
import eu.tuxcraft.translationprovider.engine.model.Language;
import eu.tuxcraft.translationprovider.engine.translation.TranslationUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TranslationProviderEngine {
  @Getter Logger logger;
  @Getter Connection connection;

  @Getter UserLanguageCache userLanguageCache = new UserLanguageCache();
  @Getter TranslationCache translationCache = new TranslationCache();

  @Getter ExecutorService threadPool = Executors.newCachedThreadPool();

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

      getThreadPool()
          .execute(
              () -> {
                getTranslationCache().flush();
                getUserLanguageCache().flush();

                getLogger().info("Caches flushed");

                List<Language> availableLanguages = Language.getAvailableLanguages();

                if (availableLanguages.stream().noneMatch(Language::isDefault)) {
                  throw new IllegalStateException("No default language found");
                }

                getTranslationCache().invalidateTranslationCache();
                getUserLanguageCache().invalidateUserLanguageCache();
              });

    } catch (Exception e) {
      getLogger().severe("Error while reloading TranslationProvider");
    }
  }
}
