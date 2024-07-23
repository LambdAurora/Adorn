package juuxel.adorn.gradle.asm;

import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.function.Predicate;

public record InsnPattern<T extends AbstractInsnNode>(Class<T> type, Predicate<T> filter) {
}
