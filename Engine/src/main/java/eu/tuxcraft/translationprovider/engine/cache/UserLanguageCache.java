package eu.tuxcraft.translationprovider.engine.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import eu.tuxcraft.translationprovider.engine.cache.loaders.UserLanguageCacheLoader;
import eu.tuxcraft.translationprovider.engine.model.Language;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Caches the users languages
 *
 * @author thelooter
 * @since 2.0.0
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserLanguageCache {
  @Getter LoadingCache<UUID, Language> cache;

  /**
   * Creates a new {@link UserLanguageCache}.
   *
   * @since 2.0.0
   */
  public UserLanguageCache() {
    this.cache =
        CacheBuilder.newBuilder()
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .build(new UserLanguageCacheLoader());
  }

  /**
   * Gets the {@link Language} of the user.
   *
   * @param uuid The user's {@link UUID}.
   * @return The {@link Language} of the user.
   *
   * @since 2.0.0
   */
  public Language getUserLanguage(UUID uuid) {
    return cache.getUnchecked(uuid);
  }

  /**
   * Invalidates all users.
   *
   * @since 2.0.0
   */
  public void invalidateUserLanguageCache() {
    cache.invalidateAll();
  }

  /**
   * Invalidates the cache for the given user.
   *
   * @param uuid The user's {@link UUID}.
   * @since 2.0.0
   */
  public void invalidateUser(UUID uuid) {
    cache.invalidate(uuid);
  }

  /**
   * Flushes the cache.
   *
   * @since 2.0.0
   */
  public void flush() {
    cache.invalidateAll();
    cache.cleanUp();
  }
}
