package eu.tuxcraft.translationprovider.engine.cache.loaders;

import com.google.common.cache.CacheLoader;
import eu.tuxcraft.translationprovider.engine.cache.TranslationCache;
import eu.tuxcraft.translationprovider.engine.database.TranslationDatabaseHelper;
import eu.tuxcraft.translationprovider.engine.model.Language;

import java.util.Map;

/**
 * {@link CacheLoader} for the {@link TranslationCache}
 *
 * @author thelooter
 * @since 2.0.0
 */
public class TranslationCacheLoader extends CacheLoader<Language, Map<String, String>> {

  /**
   * {@inheritDoc}
   *
   * @since 2.0.0
   */
  @Override
  public Map<String, String> load(Language key) throws Exception {
    return new TranslationDatabaseHelper(key).getTranslations();
  }
}
