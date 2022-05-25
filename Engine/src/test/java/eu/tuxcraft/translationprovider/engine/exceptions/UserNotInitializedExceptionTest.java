package eu.tuxcraft.translationprovider.engine.exceptions;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class UserNotInitializedExceptionTest {

    @Test
    void testEmptyConstructor() {
        UserNotInitializedException userNotInitializedException = new UserNotInitializedException();

        try {
            throw userNotInitializedException;
        } catch (UserNotInitializedException e) {
            assertThat(e.getMessage(), nullValue());
            assertThat(e.getCause(), nullValue());
            e.printStackTrace();
        }
    }

    @Test
    void testConstructorWithMessage() {
        UserNotInitializedException userNotInitializedException = new UserNotInitializedException("Test");

        try {
            throw userNotInitializedException;
        } catch (UserNotInitializedException e) {
            assertThat(e.getMessage(), equalTo("Test"));
            assertThat(e.getCause(), nullValue());
            e.printStackTrace();
        }
    }

    @Test
    void testConstructorWithCause() {
        Exception cause = new Exception("Test");
        UserNotInitializedException userNotInitializedException = new UserNotInitializedException(cause);

        try {
            throw userNotInitializedException;
        } catch (UserNotInitializedException e) {
            assertThat(e.getMessage(), equalTo("java.lang.Exception: Test"));
            assertThat(e.getCause(), equalTo(cause));
            e.printStackTrace();
        }
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Exception cause = new Exception("Test");
        UserNotInitializedException userNotInitializedException = new UserNotInitializedException("This is a message", cause);

        try {
            throw userNotInitializedException;
        } catch (UserNotInitializedException e) {
            assertThat(e.getMessage(), equalTo("This is a message"));
            assertThat(e.getCause(), equalTo(cause));
            e.printStackTrace();
        }
    }

    @Test
    void testCompleteConstructor() {
        Exception cause = new Exception("Test");
        UserNotInitializedException userNotInitializedException = new UserNotInitializedException("This is a message", cause, true, true);

        try{
            throw userNotInitializedException;
        } catch (UserNotInitializedException e) {
            assertThat(e.getMessage(), equalTo("This is a message"));
            assertThat(e.getCause(), equalTo(cause));
            e.printStackTrace();
        }
    }
}
