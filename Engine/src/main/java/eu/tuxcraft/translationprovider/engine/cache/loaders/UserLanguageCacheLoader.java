package eu.tuxcraft.translationprovider.engine.cache.loaders;

import com.google.common.cache.CacheLoader;
import eu.tuxcraft.translationprovider.engine.database.UserDatabaseHelper;
import eu.tuxcraft.translationprovider.engine.model.Language;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UserLanguageCacheLoader extends CacheLoader<UUID, Language> {

  @Override
  public @NotNull Language load(@NotNull UUID userUUID) {
    return new UserDatabaseHelper(userUUID).getUserLanguage();
  }
}
