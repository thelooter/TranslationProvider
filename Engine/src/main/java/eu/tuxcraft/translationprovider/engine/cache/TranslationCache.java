package eu.tuxcraft.translationprovider.engine.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import eu.tuxcraft.translationprovider.engine.cache.loaders.TranslationCacheLoader;
import eu.tuxcraft.translationprovider.engine.model.Language;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Cache for translations.
 *
 * @author thelooter
 * @since 2.0.0
 */
public class TranslationCache {

  @Getter private LoadingCache<Language, Map<String, String>> cache;

  /**
   * Creates a new {@link TranslationCache} with the given {@link TranslationCacheLoader}.
   *
   * @since 2.0.0
   */
  public TranslationCache() {
    this.cache =
        CacheBuilder.newBuilder()
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .build(new TranslationCacheLoader());
  }

  /**
   * Gets the translation for the given key and {@link Language}.
   * @param language The {@link Language} to get the translation for.
   * @param key The key to get the translation for.
   * @return The translation for the given key and language.
   *
   * @since 2.0.0
   */
  public String getTranslation(Language language, String key) {
    return cache.getUnchecked(language).get(key);
  }

  /**
   * Invalidates all Translations.
   *
   * @since 2.0.0
   */
  public void invalidateTranslationCache() {
    cache.invalidateAll();
  }

  /**
   * Invalidates the Translations for the given {@link Language}.
   * @param language The {@link Language} to invalidate the Translations for.
   *
   * @since 2.0.0
   */
  public void invalidateLanguage(Language language) {
    cache.invalidate(language);
  }

  /**
   * Flushes the Translations for the given {@link Language}.
   *
   * @since 2.0.0
   */
  public void flush() {
    cache.invalidateAll();
    cache.cleanUp();
  }
}
