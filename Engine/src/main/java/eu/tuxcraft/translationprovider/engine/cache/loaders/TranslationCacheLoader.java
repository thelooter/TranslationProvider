package eu.tuxcraft.translationprovider.engine.cache.loaders;

import com.google.common.cache.CacheLoader;
import eu.tuxcraft.translationprovider.engine.database.TranslationDatabaseHelper;
import eu.tuxcraft.translationprovider.engine.model.Language;

import java.util.Map;

public class TranslationCacheLoader extends CacheLoader<Language, Map<String, String>> {
	@Override public Map<String, String> load(Language key) throws Exception {
    return new TranslationDatabaseHelper(key).getTranslations();
	}
}

