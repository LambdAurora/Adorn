package juuxel.adorn.datagen;

public interface Material {
    Id STICK = new Id("c", "rods/wooden");
    Id STONE_ROD = new Id("c", "rods/stone");

    /**
     * {@return a unique ID for this material}
     */
    Id getId();
    Id getStick();

    /**
     * {@return a unique snowflake string containing all data from this material}
     * TODO: Used for up-to-date checks.
     */
    String getSnowflake();

    default boolean isModded() {
        return !getId().namespace().equals("minecraft");
    }

    void appendTemplates(TemplateContext context);
}
