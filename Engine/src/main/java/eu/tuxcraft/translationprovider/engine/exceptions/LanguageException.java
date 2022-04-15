package eu.tuxcraft.translationprovider.engine.exceptions;

public class LanguageException extends RuntimeException {
  public LanguageException() {
    super();
  }

  public LanguageException(String message) {
    super(message);
  }

  public LanguageException(String message, Throwable cause) {
    super(message, cause);
  }

  public LanguageException(Throwable cause) {
    super(cause);
  }

  protected LanguageException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
