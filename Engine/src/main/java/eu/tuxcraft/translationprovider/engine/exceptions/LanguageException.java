package eu.tuxcraft.translationprovider.engine.exceptions;

/**
 * Generic exception for language related errors.
 *
 * @author thelooter
 * @since 2.0.0
 */
public class LanguageException extends RuntimeException {

  /**
   * Creates a new LanguageException with the given message.
   *
   * @since 2.0.0
   */
  public LanguageException() {
    super();
  }

  /**
   * Creates a new LanguageException with the given message.
   *
   * @param message The message to use.
   * @since 2.0.0
   */
  public LanguageException(String message) {
    super(message);
  }

  /**
   * Creates a new LanguageException with the given message and cause.
   *
   * @param message The message to use.
   * @param cause The cause to use.
   * @since 2.0.0
   */
  public LanguageException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new LanguageException with the given cause.
   *
   * @param cause The cause to use.
   * @since 2.0.0
   */
  public LanguageException(Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new LanguageException with the given message and cause as well as to enable
   * Suppresion and enable a writable stack trace.
   *
   * @param message The message to use.
   * @param cause The cause to use.
   * @param enableSuppression Whether or not suppression is enabled or disabled.
   * @param writableStackTrace Whether or not the stack trace should be writable.
   * @since 2.0.0
   */
  protected LanguageException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
