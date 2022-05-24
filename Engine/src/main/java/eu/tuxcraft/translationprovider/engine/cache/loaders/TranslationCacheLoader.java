package eu.tuxcraft.translationprovider.engine.cache.loaders;

import com.google.common.cache.CacheLoader;
import eu.tuxcraft.translationprovider.engine.cache.TranslationCache;
import eu.tuxcraft.translationprovider.engine.database.TranslationDatabaseHelper;
import eu.tuxcraft.translationprovider.engine.model.Language;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * {@link CacheLoader} for the {@link TranslationCache}
 *
 * @author thelooter
 * @since 2.0.0
 */
public class TranslationCacheLoader extends CacheLoader<Language, Map<String, String>> {

  /**
   * Creates a new {@link TranslationCacheLoader}
   *
   * @since 2.0.0
   */
  public TranslationCacheLoader() {
    super();
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0.0
   */
  @Override
  public @NotNull Map<String, String> load(Language key) throws Exception {
    return new TranslationDatabaseHelper(key).getTranslations();
  }
}
