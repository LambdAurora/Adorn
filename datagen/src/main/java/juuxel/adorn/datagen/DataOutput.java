package juuxel.adorn.datagen;

@FunctionalInterface
public interface DataOutput {
    void write(String path, String content);
}
