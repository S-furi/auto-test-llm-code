package it.unibo.asmd.compiler;

import javax.tools.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RuntimeCodeCompiler implements CodeCompiler {
    private final JavaCompiler compiler;
    private final File generatedRoot;

    private final URLClassLoader classLoader;

    public RuntimeCodeCompiler() {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        if (Objects.isNull(this.compiler)) {
            System.err.println("No Java compiler found. Are you using a JDK?");
            System.exit(1);
        }

        try {
            this.generatedRoot = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("generated")).toURI());
            if (!this.generatedRoot.isDirectory()) throw new IllegalStateException("No /resources/generated directory found!");
            this.classLoader = URLClassLoader.newInstance(
                    new URL[] { this.generatedRoot.toURI().toURL() },
                    this.getClass().getClassLoader()
            );

        } catch (final URISyntaxException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean dumpGeneratedCode(final String generatedCode, final String className) {
        final File javaFile = new File(this.generatedRoot, className + ".java");
        try (final FileWriter writer = new FileWriter(javaFile)) {
            writer.write(generatedCode);
            return true;
        } catch (final IOException ignored) {
            return false;
        }
    }

    @Override
    public boolean compileGeneratedCode(final String className) {
        final File javaFile = new File(this.generatedRoot, className + ".java");
        List<String> options = List.of(
                "-classpath", generatedRoot.getAbsolutePath() + File.pathSeparator + System.getProperty("java.class.path")
        );

        return compiler.getTask(
                null,
                null,
                null,
                options,
                null,
                compiler.getStandardFileManager(null, null, null)
                        .getJavaFileObjectsFromFiles(List.of(javaFile))
        ).call();
    }

    @Override
    public Optional<Object> loadCompiledCode(final String classPath) {
        try {
            final Class<?> clazz = Class.forName(classPath, true, this.classLoader);
            final Object obj = clazz.getDeclaredConstructor().newInstance();
            return Optional.of(obj);
        } catch (final ClassCastException ignored) {
            System.err.println("Failed casting the loaded object");
        } catch (final ClassNotFoundException ignored) {
            System.err.println("Class not found");
        } catch (final NoSuchMethodException ignored) {
            System.err.println("No constructor was found for class.");
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Diagnostic<? extends JavaFileObject>>> canCompile(final String classname, final String code) {
        final InMemoryJavaSource source = new InMemoryJavaSource(classname, code);
        final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        try (final StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null)) {
            final List<String> options = List.of(
                    "-classpath", generatedRoot.getAbsolutePath() + File.pathSeparator + System.getProperty("java.class.path")
            );

            final JavaCompiler.CompilationTask task = compiler.getTask(
                    null, fileManager, diagnostics, options, null, List.of(source)
            );

            final var success = task.call();
            if (!success) {
                diagnostics.getDiagnostics().forEach(d ->
                        System.err.println("Error on line " + d.getLineNumber() + ": " + d.getMessage(null))
                );
            }
            return success ? Optional.empty() : Optional.of(diagnostics.getDiagnostics());
        } catch (final IOException e) {
            e.printStackTrace();
            return Optional.of(diagnostics.getDiagnostics());
        }
    }

    private static class InMemoryJavaSource extends SimpleJavaFileObject {
        private final String code;

        public InMemoryJavaSource(final String className, final String code) {
            super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }
}
