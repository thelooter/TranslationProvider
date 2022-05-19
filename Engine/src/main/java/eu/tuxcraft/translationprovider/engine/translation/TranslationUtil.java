package eu.tuxcraft.translationprovider.engine.translation;

import eu.tuxcraft.translationprovider.engine.TranslationProviderEngine;
import eu.tuxcraft.translationprovider.engine.model.Language;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * Utility class for translation. Takes care of translation
 *
 * @author thelooter
 * @since 2.0.0
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TranslationUtil {

  TranslationProviderEngine engine = TranslationProviderEngine.getInstance();
  Logger logger = engine.getLogger();

  /**
   * Creates an Instance of the {@link TranslationUtil}.
   *
   * @since 2.0.0
   */
  public TranslationUtil() {}

  /**
   * Translates a string.
   *
   * @param key The key of the string to translate
   * @param language The language to translate to
   * @param parameters The parameters to replace in the string
   * @return The translated string
   * @since 2.0.0
   */
  public String translate(String key, Language language, Map<String, String> parameters) {

    String translation =
        engine.getTranslationCache().getTranslation(language, key); // Get Translation from Cache

    if (translation == null) {
      logger.warning(
          "Translation for key " + key + " not found in language " + language.getDisplayName());
      return getFallBackString(
          key, parameters); // Return Fallback String if no Translation was found
    }
    if (translation.contains("%")) { // Handle Parameters

      if (parameters == null) { // Handle Default Parameters for Unicode Symbols
        parameters = new HashMap<>(getDefaultParameters());
      } else {
        parameters.putAll(getDefaultParameters());
      }

      for (Map.Entry<String, String> parameter :
          parameters.entrySet()) { // Replace All Placeholders with Parameters
        translation = translation.replace("%" + parameter.getKey() + "%", parameter.getValue());
      }

      translation = translation.replace("\\%", "%"); // Replace escaped Escape Character
      translation = translation.replace("\\n", "\n"); // Replace escaped Newline Character
    }

    return translation;
  }

  /**
   * Calculates the fallback string.
   *
   * @param key The key of the string to get the fallback string for
   * @param parameters The parameters to replace in the string
   * @return The fallback string
   * @since 2.0.0
   */
  private static String getFallBackString(String key, Map<String, String> parameters) {
    if (parameters == null) {
      return key;
    }

    StringBuilder sb = new StringBuilder();

    for (Map.Entry<String, String> entry : parameters.entrySet()) {
      sb.append("{")
          .append(sb.toString().isEmpty() ? "" : ", ")
          .append(entry.getKey())
          .append(": ")
          .append(entry.getValue())
          .append("}");
    }

    return sb.toString();
  }

  /**
   * Gets the default parameters.
   *
   * @return The default parameters
   * @since 2.0.0
   */
  private static Map<String, String> getDefaultParameters() {
    Map<String, String> parameters = new HashMap<>();

    parameters.put("NOTE", String.valueOf('\u266A'));
    parameters.put("STAR", String.valueOf('\u2606'));
    parameters.put("STAR_FILLED", String.valueOf('\u2605'));
    parameters.put("SUN", String.valueOf('\u2739'));
    parameters.put("MAIL", String.valueOf('\u2709'));
    parameters.put("CIRCLE", String.valueOf('\u25CB'));
    parameters.put("SPARK", String.valueOf('\u2727'));
    parameters.put("CHECK_MARK", String.valueOf('\u2713'));
    parameters.put("HEART_S", String.valueOf('\u2665'));
    parameters.put("HEART_L", String.valueOf('\u2764'));

    return parameters;
  }
}
