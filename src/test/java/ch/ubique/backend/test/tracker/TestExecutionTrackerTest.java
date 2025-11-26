package ch.ubique.backend.test.tracker;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExecutableInvoker;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstances;
import org.junit.jupiter.api.function.ThrowingConsumer;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

class TestExecutionTrackerTest {

    private TestExecutionTracker tracker;

    @BeforeEach
    void beforeEach() {
        TestExecutionTracker.reset();
        tracker = new TestExecutionTracker();
    }

    @Test
    void testTrackingSingleExecution() {
        tracker.testSuccessful(mockContext("myTestMethod"));

        assertTrue(
                TestExecutionTracker.wasExecutedByMethodName("myTestMethod"),
                "ExecutionTracker should recognize myTestMethod as executed");
        assertFalse(
                TestExecutionTracker.wasExecutedByMethodName("otherMethod"),
                "ExecutionTracker should not recognize otherMethod as executed");
    }

    @Test
    void testResetClearsState() {
        tracker.testSuccessful(mockContext("testA"));
        assertTrue(TestExecutionTracker.wasExecutedByMethodName("testA"));
        assertEquals(1, TestExecutionTracker.getExecutedTests().size());

        TestExecutionTracker.reset();

        assertFalse(TestExecutionTracker.wasExecutedByMethodName("testA"));
        assertEquals(0, TestExecutionTracker.getExecutedTests().size());
    }

    @Test
    void testGetExecutedTestsReturnsCorrectSet() {
        tracker.testSuccessful(mockContext("firstTest"));
        tracker.testSuccessful(mockContext("secondTest"));

        final var all = TestExecutionTracker.getExecutedTests();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(s -> s.contains("firstTest")));
        assertTrue(all.stream().anyMatch(s -> s.contains("secondTest")));
    }

    @Test
    void testWasExecutedByMethodNameMatching() {
        tracker.testSuccessful(mockContext("ShouldRunWithMongo.mongoTest()"));

        assertTrue(
                TestExecutionTracker.wasExecutedByMethodName("mongoTest"),
                "Expected mongoTest to be detected as executed");

        assertTrue(
                TestExecutionTracker.wasExecutedByMethodName("ShouldRunWithMongo"),
                "Expected ShouldRunWithMongo class name to be matched");

        assertFalse(
                TestExecutionTracker.wasExecutedByMethodName("nonexistentMethod"),
                "Expected non-existent method to not be matched");
    }

    private ExtensionContext mockContext(final String displayName) {
        return new ExtensionContext() {

            @Override
            public String getDisplayName() {
                return displayName;
            }

            // Stub all other methods
            @Override
            public Optional<ExtensionContext> getParent() {
                return Optional.empty();
            }

            @Override
            public ExtensionContext getRoot() {
                return this;
            }

            @Override
            public String getUniqueId() {
                return displayName;
            }

            @Override
            public Optional<String> getConfigurationParameter(final String key) {
                return Optional.empty();
            }

            @Override
            public <T> Optional<T> getConfigurationParameter(
                    final String key, final Function<? super String, ? extends T> transformer) {
                return Optional.empty();
            }

            @Override
            public void publishReportEntry(final Map<String, String> map) {}

            @Override
            public void publishFile(
                    final String name,
                    final MediaType mediaType,
                    final ThrowingConsumer<Path> action) {}

            @Override
            public void publishDirectory(final String name, final ThrowingConsumer<Path> action) {}

            @Override
            public Optional<Class<?>> getTestClass() {
                return Optional.empty();
            }

            @Override
            public List<Class<?>> getEnclosingTestClasses() {
                return List.of();
            }

            @Override
            public Optional<TestInstance.Lifecycle> getTestInstanceLifecycle() {
                return Optional.empty();
            }

            @Override
            public Optional<Object> getTestInstance() {
                return Optional.empty();
            }

            @Override
            public Optional<TestInstances> getTestInstances() {
                return Optional.empty();
            }

            @Override
            public Optional<Method> getTestMethod() {
                return Optional.empty();
            }

            @Override
            public Optional<Throwable> getExecutionException() {
                return Optional.empty();
            }

            @Override
            public Store getStore(final Namespace namespace) {
                return null;
            }

            @Override
            public Store getStore(final StoreScope scope, final Namespace namespace) {
                return null;
            }

            @Override
            public ExecutionMode getExecutionMode() {
                return null;
            }

            @Override
            public ExecutableInvoker getExecutableInvoker() {
                return null;
            }

            @Override
            public Optional<AnnotatedElement> getElement() {
                return Optional.empty();
            }

            @Override
            public Set<String> getTags() {
                return Set.of();
            }
        };
    }
}
