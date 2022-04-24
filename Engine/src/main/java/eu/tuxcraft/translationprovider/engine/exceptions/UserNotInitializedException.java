package eu.tuxcraft.translationprovider.engine.exceptions;

/**
 * Exception thrown when a user is not initialized.
 *
 * @author thelooter
 * @since 2.0.0
 */
public class UserNotInitializedException extends RuntimeException {

  /**
   * Creates a new instance of {@link UserNotInitializedException}.defaultLanguage
   *
   * @since 2.0.0
   */
  public UserNotInitializedException() {
    super();
  }

  /**
   * Creates a new instance of {@link UserNotInitializedException}.
   *
   * @param message the message
   * @since 2.0.0
   */
  public UserNotInitializedException(String message) {
    super(message);
  }

  /**
   * Creates a new instance of {@link UserNotInitializedException}.
   *
   * @param message the message
   * @param cause the cause
   * @since 2.0.0
   */
  public UserNotInitializedException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new instance of {@link UserNotInitializedException}.
   *
   * @param cause the cause
   * @since 2.0.0
   */
  public UserNotInitializedException(Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new instance of {@link UserNotInitializedException}.
   *
   * @param message the message
   * @param cause the cause
   * @param enableSuppression the enable suppression
   * @param writableStackTrace the writable stack trace
   * @since 2.0.0
   */
  protected UserNotInitializedException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
