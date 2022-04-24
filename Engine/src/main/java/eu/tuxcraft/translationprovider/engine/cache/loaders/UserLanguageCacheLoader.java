package eu.tuxcraft.translationprovider.engine.cache.loaders;

import com.google.common.cache.CacheLoader;
import eu.tuxcraft.translationprovider.engine.cache.UserLanguageCache;
import eu.tuxcraft.translationprovider.engine.database.UserDatabaseHelper;
import eu.tuxcraft.translationprovider.engine.model.Language;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * {@link CacheLoader} for the {@link UserLanguageCache}.
 *
 * @author thelooter
 * @since 2.0.0
 */
public class UserLanguageCacheLoader extends CacheLoader<UUID, Language> {

  /**
   * {@inheritDoc}
   *
   * @param userUUID The {@link UUID} of the user.
   * @since 2.0.0
   */
  @Override
  public @NotNull Language load(@NotNull UUID userUUID) {
    return new UserDatabaseHelper(userUUID).getUserLanguage();
  }
}
