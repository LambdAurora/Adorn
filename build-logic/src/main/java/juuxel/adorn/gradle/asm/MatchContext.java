package juuxel.adorn.gradle.asm;

import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.List;

public interface MatchContext {
    List<AbstractInsnNode> instructions();
    void replaceWith(List<AbstractInsnNode> instructions);
}
