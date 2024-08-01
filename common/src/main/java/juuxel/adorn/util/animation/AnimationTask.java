package juuxel.adorn.util.animation;

public interface AnimationTask {
    boolean isAlive();
    void tick();
    default void removed() {
    }
}
