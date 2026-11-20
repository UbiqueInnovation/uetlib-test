package ch.uetlib.test.assertion;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

@SuppressWarnings({"ResultOfMethodCallIgnored", "DataFlowIssue"})
class AssertionUtilsTest {

    @Test
    void assertThrowsWithMessage() {
        assertDoesNotThrow(() ->
                AssertionUtils.assertThrowsWithMessage(
                        NullPointerException.class,
                        () -> {
                            final String a = null;
                            a.getClass();
                        },
                        "Cannot invoke \"Object.getClass()\" because \"a\" is null"));

        final var errorIncorrectException =
                assertThrows(
                        AssertionFailedError.class,
                        () ->
                                AssertionUtils.assertThrowsWithMessage(
                                        IllegalArgumentException.class,
                                        () -> {
                                            final String a = null;
                                            a.getClass();
                                        },
                                        "Cannot invoke \"Object.getClass()\" because \"a\" is"
                                                + " null"));
        assertEquals(
                "Unexpected exception type thrown, expected: <java.lang.IllegalArgumentException>"
                        + " but was: <java.lang.NullPointerException>",
                errorIncorrectException.getMessage());

        final var errorIncorrectMessage =
                assertThrows(
                        AssertionFailedError.class,
                        () ->
                                AssertionUtils.assertThrowsWithMessage(
                                        NullPointerException.class,
                                        () -> {
                                            final String a = null;
                                            a.getClass();
                                        },
                                        "incorrect message"));
        assertEquals(
                "expected: <incorrect message> but was: <Cannot invoke \"Object.getClass()\""
                        + " because \"a\" is null>",
                errorIncorrectMessage.getMessage());

        final var errorNullPointerExceptionOnExpectedType =
                assertThrows(
                        NullPointerException.class,
                        () ->
                                AssertionUtils.assertThrowsWithMessage(
                                        null,
                                        () -> {
                                            final String a = null;
                                            a.getClass();
                                        },
                                        "incorrect message"));
        assertEquals(
                "Cannot invoke \"java.lang.Class.isInstance(Object)\" because \"expectedType\" is"
                        + " null",
                errorNullPointerExceptionOnExpectedType.getMessage());
    }

    @Test
    void assertThrowsWithMessageAndCause() {
        assertDoesNotThrow(() ->
                AssertionUtils.assertThrowsWithMessageAndCause(
                        RuntimeException.class,
                        () -> {
                            final String a = null;
                            try {
                                a.getClass();
                            } catch (final NullPointerException npe) {
                                throw new RuntimeException(
                                        "npe thrown, caused by: " + npe.getMessage(), npe);
                            }
                        },
                        "npe thrown, caused by: Cannot invoke \"Object.getClass()\" because"
                                + " \"a\" is null",
                        NullPointerException.class));

        final var errorIncorrectException =
                assertThrows(
                        AssertionFailedError.class,
                        () ->
                                AssertionUtils.assertThrowsWithMessageAndCause(
                                        IllegalArgumentException.class,
                                        () -> {
                                            final String a = null;
                                            try {
                                                a.getClass();
                                            } catch (final NullPointerException npe) {
                                                throw new RuntimeException(
                                                        "npe thrown, caused by: "
                                                                + npe.getMessage(),
                                                        npe);
                                            }
                                        },
                                        "npe thrown, caused by: Cannot invoke \"Object.getClass()\""
                                                + " because \"a\" is null",
                                        NullPointerException.class));
        assertEquals(
                "Unexpected exception type thrown, expected: <java.lang.IllegalArgumentException>"
                        + " but was: <java.lang.RuntimeException>",
                errorIncorrectException.getMessage());

        final var errorIncorrectMessage =
                assertThrows(
                        AssertionFailedError.class,
                        () ->
                                AssertionUtils.assertThrowsWithMessageAndCause(
                                        RuntimeException.class,
                                        () -> {
                                            final String a = null;
                                            try {
                                                a.getClass();
                                            } catch (final NullPointerException npe) {
                                                throw new RuntimeException(
                                                        "npe thrown, caused by: "
                                                                + npe.getMessage(),
                                                        npe);
                                            }
                                        },
                                        "incorrect message",
                                        NullPointerException.class));
        assertEquals(
                "expected: <incorrect message> but was: <npe thrown, caused by: Cannot invoke"
                        + " \"Object.getClass()\" because \"a\" is null>",
                errorIncorrectMessage.getMessage());

        final var errorIncorrectCause =
                assertThrows(
                        AssertionFailedError.class,
                        () ->
                                AssertionUtils.assertThrowsWithMessageAndCause(
                                        RuntimeException.class,
                                        () -> {
                                            final String a = null;
                                            try {
                                                a.getClass();
                                            } catch (final NullPointerException npe) {
                                                throw new RuntimeException(
                                                        "npe thrown, caused by: "
                                                                + npe.getMessage(),
                                                        npe);
                                            }
                                        },
                                        "npe thrown, caused by: Cannot invoke \"Object.getClass()\""
                                                + " because \"a\" is null",
                                        IllegalArgumentException.class));
        assertEquals(
                "Unexpected exception cause, expected: <java.lang.IllegalArgumentException> but"
                        + " was: <java.lang.NullPointerException> ==> expected: <true> but was:"
                        + " <false>",
                errorIncorrectCause.getMessage());

        final var errorNullPointerExceptionOnExpectedType =
                assertThrows(
                        NullPointerException.class,
                        () ->
                                AssertionUtils.assertThrowsWithMessageAndCause(
                                        null,
                                        () -> {
                                            final String a = null;
                                            try {
                                                a.getClass();
                                            } catch (final NullPointerException npe) {
                                                throw new RuntimeException(
                                                        "npe thrown, caused by: "
                                                                + npe.getMessage(),
                                                        npe);
                                            }
                                        },
                                        "npe thrown, caused by: Cannot invoke \"Object.getClass()\""
                                                + " because \"a\" is null",
                                        NullPointerException.class));
        assertEquals(
                "Cannot invoke \"java.lang.Class.isInstance(Object)\" because \"expectedType\" is"
                        + " null",
                errorNullPointerExceptionOnExpectedType.getMessage());

        final var errorNullPointerExceptionOnExpectedCauseType =
                assertThrows(
                        NullPointerException.class,
                        () ->
                                AssertionUtils.assertThrowsWithMessageAndCause(
                                        RuntimeException.class,
                                        () -> {
                                            final String a = null;
                                            try {
                                                a.getClass();
                                            } catch (final NullPointerException npe) {
                                                throw new RuntimeException(
                                                        "npe thrown, caused by: "
                                                                + npe.getMessage(),
                                                        npe);
                                            }
                                        },
                                        "npe thrown, caused by: Cannot invoke \"Object.getClass()\""
                                                + " because \"a\" is null",
                                        null));
        assertEquals(
                "Cannot invoke \"java.lang.Class.isInstance(Object)\" because \"expectedCauseType\""
                        + " is null",
                errorNullPointerExceptionOnExpectedCauseType.getMessage());
    }
}
