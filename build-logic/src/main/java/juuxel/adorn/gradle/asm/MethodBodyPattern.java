package juuxel.adorn.gradle.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record MethodBodyPattern(List<InsnPattern<?>> patterns) {
    @SuppressWarnings("unchecked")
    public boolean match(MethodNode mn, Consumer<MatchContext> visitor) {
        for (AbstractInsnNode insn : mn.instructions) {
            var start = (InsnPattern<AbstractInsnNode>) patterns.get(0);
            if (start.type().isInstance(insn) && start.filter().test(insn)) {
                Stream<AbstractInsnNode> insnStream = Stream.iterate(insn.getNext(), it -> it.getNext() != null, AbstractInsnNode::getNext)
                    .filter(it -> !(it instanceof LabelNode)
                        && !(it instanceof LineNumberNode)
                        && !(it instanceof InsnNode insnNode && insnNode.getOpcode() == Opcodes.NOP));
                var matches = zip(patterns.stream().skip(1).iterator(), insnStream.iterator())
                    .filter(entry -> {
                        var pattern = entry.getKey();
                        var node = entry.getValue();
                        return pattern.type().isInstance(node) && ((InsnPattern<AbstractInsnNode>) pattern).filter().test(node);
                    })
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toCollection(ArrayList::new));

                if (matches.size() != patterns.size() - 1) continue;
                matches.add(0, insn);
                var context = new MatchContext() {
                    @Override
                    public List<AbstractInsnNode> instructions() {
                        return matches;
                    }

                    @Override
                    public void replaceWith(List<AbstractInsnNode> instructions) {
                        if (instructions.size() < matches.size()) {
                            throw new IllegalArgumentException("Incorrect number of replacements");
                        }

                        for (int i = 0; i < matches.size(); i++) {
                            mn.instructions.set(matches.get(i), instructions.get(i));
                        }

                        if (instructions.size() > matches.size()) {
                            for (int i = matches.size(); i < instructions.size(); i++) {
                                mn.instructions.insert(instructions.get(i - 1), instructions.get(i));
                            }
                        }
                    }
                };
                visitor.accept(context);
                return true;
            }
        }

        return false;
    }

    private static <T, U> Stream<Map.Entry<T, U>> zip(Iterator<T> left, Iterator<U> right) {
        Stream.Builder<Map.Entry<T, U>> builder = Stream.builder();
        while (left.hasNext() && right.hasNext()) {
            builder.add(Map.entry(left.next(), right.next()));
        }
        return builder.build();
    }
}
