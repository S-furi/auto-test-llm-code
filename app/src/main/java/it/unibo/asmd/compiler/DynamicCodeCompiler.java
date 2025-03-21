package it.unibo.asmd.compiler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Objects;
import java.util.Optional;

public class DynamicCodeCompiler {
    private final JavaCompiler compiler;

    public DynamicCodeCompiler() {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        if (Objects.isNull(this.compiler)) {
            System.err.println("No Java compiler found. Are you using a JDK?");
            System.exit(1);
        }
    }

    public boolean dumpGeneratedCode(final String generatedCode, final String className) {
        final File javaFile = new File(className + ".java");
        try (final FileWriter writer = new FileWriter(javaFile)) {
            writer.write(generatedCode);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean compileGeneratedCode(final String className) {
        final File javaFile = new File(className + ".java");
        return compiler.run(null, null, null, javaFile.getPath()) == 0;
    }

    public Optional<Object> loadCompiledCode(final String classPath) {
        try (final URLClassLoader classLoader = URLClassLoader.newInstance(new java.net.URL[] { new File(".").toURI().toURL() })) {
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
