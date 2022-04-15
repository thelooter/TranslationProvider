package eu.tuxcraft.translationprovider.engine.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import eu.tuxcraft.translationprovider.engine.cache.loaders.TranslationCacheLoader;
import eu.tuxcraft.translationprovider.engine.model.Language;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TranslationCache {

  @Getter private LoadingCache<Language, Map<String, String>> cache;

  public TranslationCache() {
    this.cache =
        CacheBuilder.newBuilder()
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .build(new TranslationCacheLoader());
  }

  public String getTranslation(Language language, String key) {
    return cache.getUnchecked(language).get(key);
  }

  public void invalidateTranslationCache() {
    cache.invalidateAll();
  }

  public void invalidateLanguage(Language language) {
    cache.invalidate(language);
  }

  public void flush() {
    cache.invalidateAll();
    cache.cleanUp();
  }
}
