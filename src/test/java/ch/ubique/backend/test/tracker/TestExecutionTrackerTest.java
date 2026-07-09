package ch.ubique.backend.test.tracker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.jspecify.annotations.Nullable;
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

        assertThat(TestExecutionTracker.wasExecutedByMethodName("myTestMethod"))
                .as("ExecutionTracker should recognize myTestMethod as executed")
                .isTrue();
        assertThat(TestExecutionTracker.wasExecutedByMethodName("otherMethod"))
                .as("ExecutionTracker should not recognize otherMethod as executed")
                .isFalse();
    }

    @Test
    void testResetClearsState() {
        tracker.testSuccessful(mockContext("testA"));
        assertThat(TestExecutionTracker.wasExecutedByMethodName("testA")).isTrue();
        assertThat(TestExecutionTracker.getExecutedTests()).hasSize(1);

        TestExecutionTracker.reset();

        assertThat(TestExecutionTracker.wasExecutedByMethodName("testA")).isFalse();
        assertThat(TestExecutionTracker.getExecutedTests()).isEmpty();
    }

    @Test
    void testGetExecutedTestsReturnsCorrectSet() {
        tracker.testSuccessful(mockContext("firstTest"));
        tracker.testSuccessful(mockContext("secondTest"));

        final var all = TestExecutionTracker.getExecutedTests();
        assertThat(all)
                .hasSize(2)
                .anyMatch(s -> s.contains("firstTest"))
                .anyMatch(s -> s.contains("secondTest"));
    }

    @Test
    void testWasExecutedByMethodNameMatching() {
        tracker.testSuccessful(mockContext("ShouldRunWithMongo.mongoTest()"));

        assertThat(TestExecutionTracker.wasExecutedByMethodName("mongoTest"))
                .withFailMessage("Expected mongoTest to be detected as executed")
                .isTrue();

        assertThat(TestExecutionTracker.wasExecutedByMethodName("ShouldRunWithMongo"))
                .withFailMessage("Expected ShouldRunWithMongo class name to be matched")
                .isTrue();

        assertThat(TestExecutionTracker.wasExecutedByMethodName("nonexistentMethod"))
                .withFailMessage("Expected non-existent method to not be matched")
                .isFalse();
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
                    final String key,
                    final Function<? super String, ? extends @Nullable T> transformer) {
                return Optional.empty();
            }

            @Override
            public void publishReportEntry(final Map<String, String> map) {
                /* empty */
            }

            @Override
            public void publishFile(
                    final String name,
                    final MediaType mediaType,
                    final ThrowingConsumer<Path> action) {
                /* empty */
            }

            @Override
            public void publishDirectory(final String name, final ThrowingConsumer<Path> action) {
                /* empty */
            }

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
                return mock(Store.class);
            }

            @Override
            public Store getStore(final StoreScope scope, final Namespace namespace) {
                return mock(Store.class);
            }

            @Override
            public ExecutionMode getExecutionMode() {
                return mock(ExecutionMode.class);
            }

            @Override
            public ExecutableInvoker getExecutableInvoker() {
                return mock(ExecutableInvoker.class);
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
