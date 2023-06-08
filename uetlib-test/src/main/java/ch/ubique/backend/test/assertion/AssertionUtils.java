package ch.ubique.backend.test.assertion;

import jakarta.validation.constraints.NotNull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

import java.text.MessageFormat;

public final class AssertionUtils {

    private AssertionUtils() {}

    public static <T extends Throwable> void assertThrowsWithMessage(
            final @NotNull Class<T> expectedType,
            final Executable executable,
            final String expectedMessage) {
        final var exception = Assertions.assertThrows(expectedType, executable);
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    public static <T extends Throwable, U extends Throwable> void assertThrowsWithMessageAndCause(
            final @NotNull Class<T> expectedType,
            final Executable executable,
            final String expectedMessage,
            final @NotNull Class<U> expectedCauseType) {
        final var exception = Assertions.assertThrows(expectedType, executable);
        Assertions.assertEquals(expectedMessage, exception.getMessage());
        // runtime version of `instanceof expectedCauseType` check
        final var actualCause = exception.getCause();
        Assertions.assertTrue(
                expectedCauseType.isInstance(actualCause),
                () ->
                        MessageFormat.format(
                                "Unexpected exception cause, expected: <{0}> but was: <{1}>",
                                expectedCauseType.getCanonicalName(),
                                actualCause.getClass().getCanonicalName()));
    }
}
