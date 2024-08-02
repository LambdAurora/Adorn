package juuxel.adorn.gradle.action;

import juuxel.adorn.gradle.asm.Asm;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

public final class InlineServiceLoader implements Action<Task> {
    @Override
    public void execute(Task task) {
        try {
            run(task);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void run(Task task) throws IOException {
        Asm.transformJar(task.getOutputs().getFiles().getSingleFile().toPath(), (filer, classNode) -> {
            List<AnnotationNode> invisibleAnnotations = Objects.requireNonNullElse(classNode.invisibleAnnotations, List.of());
            if (invisibleAnnotations.stream().noneMatch(node -> "Ljuuxel/adorn/util/InlineServices;".equals(node.desc))) {
                // Skip classes without the anno
                return false;
            }

            for (MethodNode method : classNode.methods) {
                if (method.invisibleAnnotations == null) continue;
                for (AnnotationNode annotation : method.invisibleAnnotations) {
                    if ("Ljuuxel/adorn/util/InlineServices$Getter;".equals(annotation.desc)) {
                        var serviceType = Type.getReturnType(method.desc).getInternalName().replace('/', '.');
                        var serviceFile = filer.apply("META-INF/services/" + serviceType);
                        String implType;
                        if (Files.exists(serviceFile)) {
                            try {
                                var serviceImpls = Files.readAllLines(serviceFile);
                                if (serviceImpls.size() != 1) {
                                    throw new IllegalArgumentException("Service file " + serviceType + " must have exactly one provider");
                                }
                                implType = serviceImpls.get(0).replace('.', '/');
                            } catch (IOException e) {
                                throw new UncheckedIOException(e);
                            }
                        } else {
                            continue;
                        }

                        method.instructions.clear();
                        method.instructions.add(new LdcInsnNode(new ConstantDynamic(
                            "instance",
                            "L" + implType + ";",
                            new Handle(
                                Opcodes.H_INVOKESTATIC,
                                "java/lang/invoke/ConstantBootstraps",
                                "invoke",
                                "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/invoke/MethodHandle;[Ljava/lang/Object;)Ljava/lang/Object;",
                                false
                            ),
                            new Handle(Opcodes.H_NEWINVOKESPECIAL, implType, "<init>", "()V", false)
                        )));
                        method.instructions.add(new InsnNode(Opcodes.ARETURN));
                        return true;
                    }
                }
            }

            return false;
        });
    }
}
