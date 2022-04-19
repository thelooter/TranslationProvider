package eu.tuxcraft.translationprovider.engine.exceptions;

public class UserNotInitializedException extends RuntimeException {
  public UserNotInitializedException() {
    super();
  }

  public UserNotInitializedException(String message) {
    super(message);
  }

  public UserNotInitializedException(String message, Throwable cause) {
    super(message, cause);
  }

  public UserNotInitializedException(Throwable cause) {
    super(cause);
  }

  protected UserNotInitializedException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
