package juuxel.adorn.datagen;

public enum ColorMaterial implements Material {
    WHITE("white"),
    ORANGE("orange"),
    MAGENTA("magenta"),
    LIGHT_BLUE("light_blue"),
    YELLOW("yellow"),
    LIME("lime"),
    PINK("pink"),
    GRAY("gray"),
    LIGHT_GRAY("light_gray"),
    CYAN("cyan"),
    PURPLE("purple"),
    BLUE("blue"),
    BROWN("brown"),
    GREEN("green"),
    RED("red"),
    BLACK("black");

    private final Id id;
    private final Id wool;

    ColorMaterial(String color) {
        this.id = new Id("minecraft", color);
        this.wool = id.suffixed("wool");
    }

    @Override
    public Id getId() {
        return id;
    }

    @Override
    public Id getStick() {
        return Material.STICK;
    }

    @Override
    public String getSnowflake() {
        return "wool/" + id;
    }

    @Override
    public void appendTemplates(TemplateContext context) {
        context.set("wool", wool);
    }
}
