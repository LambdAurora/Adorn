package juuxel.adorn.gradle.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;

public final class Asm {
    public static void transformJar(Path jar, Transformer transformer) throws IOException {
        try (var fs = FileSystems.newFileSystem(URI.create("jar:" + jar.toUri()), Map.of("create", false))) {
            for (Path root : fs.getRootDirectories()) {
                try (var classes = Files.walk(root).filter(path -> Files.isRegularFile(path) && path.toString().endsWith(".class"))) {
                    var iter = classes.iterator();
                    while (iter.hasNext()) {
                        var c = iter.next();

                        // Read the existing class
                        ClassReader cr;
                        try (var in = Files.newInputStream(c)) {
                            cr = new ClassReader(in);
                        }
                        var node = new ClassNode();
                        cr.accept(node, 0);

                        // Transform
                        transformer.transform(fs::getPath, node);

                        // Write out the new class
                        var cw = new ClassWriter(cr, 0);
                        node.accept(cw);
                        Files.write(c, cw.toByteArray());
                    }
                }
            }
        }
    }

    @FunctionalInterface
    public interface Transformer {
        void transform(Function<String, Path> filer, ClassNode classNode) throws IOException;
    }
}
