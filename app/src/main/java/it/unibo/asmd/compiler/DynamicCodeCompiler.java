package it.unibo.asmd.compiler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;
import java.util.Optional;

public class DynamicCodeCompiler {
    private final JavaCompiler compiler;
    private final File generatedRoot;

    public DynamicCodeCompiler() {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        if (Objects.isNull(this.compiler)) {
            System.err.println("No Java compiler found. Are you using a JDK?");
            System.exit(1);
        }

        try {
            this.generatedRoot = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("generated")).toURI());
            if (!this.generatedRoot.isDirectory()) throw new IllegalStateException("No /resources/generated directory found!");
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean dumpGeneratedCode(final String generatedCode, final String className) {
        final File javaFile = new File(this.generatedRoot, className + ".java");
        try (final FileWriter writer = new FileWriter(javaFile)) {
            writer.write(generatedCode);
            return true;
        } catch (final IOException ignored) {
            return false;
        }
    }

    public boolean compileGeneratedCode(final String className) {
        final File javaFile = new File(this.generatedRoot, className + ".java");
        return compiler.run(null, null, null, javaFile.getPath()) == 0;
    }

    public Optional<Object> loadCompiledCode(final String classPath) {
        try (final URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { this.generatedRoot.toURI().toURL() })) {
            final Class<?> clazz = Class.forName(classPath, true, classLoader);
            final Object obj = clazz.getDeclaredConstructor().newInstance();
            return Optional.of(obj);
        } catch (final ClassCastException ignored) {
            System.err.println("Failed casting the loaded object");
            return Optional.empty();
        } catch (final ClassNotFoundException ignored) {
            System.err.println("Class not found");
            return Optional.empty();
        } catch (final NoSuchMethodException ignored) {
            System.err.println("No constructor was found for class.");
            return Optional.empty();
        } catch (final Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
