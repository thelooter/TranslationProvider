package eu.tuxcraft.translationprovider.engine.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import eu.tuxcraft.translationprovider.engine.cache.loaders.UserLanguageCacheLoader;
import eu.tuxcraft.translationprovider.engine.database.UserDatabaseHelper;
import eu.tuxcraft.translationprovider.engine.model.Language;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserLanguageCache {

  @Getter LoadingCache<UUID, Language> cache;

  public UserLanguageCache() {
    this.cache =
        CacheBuilder.newBuilder()
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .build(new UserLanguageCacheLoader());
  }

  public Language getUserLanguage(UUID uuid) {
    return cache.getUnchecked(uuid);
  }

  public void invalidateUserLanguageCache() {
    cache.invalidateAll();
  }

  public void invalidateUser(UUID uuid) {
    cache.invalidate(uuid);
  }

  public void flush() {
    cache.invalidateAll();
    cache.cleanUp();
  }

}
