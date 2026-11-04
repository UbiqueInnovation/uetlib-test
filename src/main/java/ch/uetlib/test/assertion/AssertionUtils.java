package ch.uetlib.test.assertion;

import static java.util.Optional.ofNullable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

import java.text.MessageFormat;

public final class AssertionUtils {

    private AssertionUtils() {}

    public static <T extends Throwable> void assertThrowsWithMessage(
            final Class<T> expectedType,
            final Executable executable,
            final String expectedMessage) {
        final var exception = Assertions.assertThrows(expectedType, executable);
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    public static <T extends Throwable, U extends Throwable> void assertThrowsWithMessageAndCause(
            final Class<T> expectedType,
            final Executable executable,
            final String expectedMessage,
            final Class<U> expectedCauseType) {
        final var exception = Assertions.assertThrows(expectedType, executable);
        Assertions.assertEquals(expectedMessage, exception.getMessage());
        // runtime version of `instanceof expectedCauseType` check
        final var actualCause = exception.getCause();
        Assertions.assertTrue(
                expectedCauseType.isInstance(actualCause),
                () -> {
                    final var actualCauseClassName =
                            ofNullable(actualCause)
                                    .map(Throwable::getClass)
                                    .map(Class::getCanonicalName)
                                    .orElse(null);
                    return MessageFormat.format(
                            "Unexpected exception cause, expected: <{0}> but was: <{1}>",
                            expectedCauseType.getCanonicalName(), actualCauseClassName);
                });
    }
}
