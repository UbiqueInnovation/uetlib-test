package ch.uetlib.test.tracker;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.HashSet;
import java.util.Set;

public class TestExecutionTracker implements TestWatcher, BeforeAllCallback {

    private static final Set<String> EXECUTED_TESTS = new HashSet<>();

    public static void reset() {
        EXECUTED_TESTS.clear();
    }

    public static boolean wasExecutedByMethodName(final String methodSubstring) {
        return EXECUTED_TESTS.stream().anyMatch(id -> id.contains(methodSubstring));
    }

    public static Set<String> getExecutedTests() {
        return Set.copyOf(EXECUTED_TESTS);
    }

    @Override
    public void testSuccessful(final ExtensionContext context) {
        EXECUTED_TESTS.add(context.getUniqueId());
    }

    @Override
    public void beforeAll(final @NonNull ExtensionContext context) {
        // Optional: clear before each class to isolate class-level tracking
        EXECUTED_TESTS.clear();
    }
}
