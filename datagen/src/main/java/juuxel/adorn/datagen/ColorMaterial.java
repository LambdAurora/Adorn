package juuxel.adorn.datagen;

public enum ColorMaterial implements Material, WoodMaterial {
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
    private final Id planks;
    private final Id slab;

    ColorMaterial(String color) {
        this.id = new Id("minecraft", color);
        this.wool = id.suffixed("wool");
        this.planks = new Id("adorn", color + "_planks");
        this.slab = new Id("adorn", color + "_wood_slab");
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
    public Id getPlanks() {
        return planks;
    }

    @Override
    public Id getSlab() {
        return slab;
    }

    @Override
    public boolean isNonFlammable() {
        return false;
    }

    @Override
    public String getSnowflake() {
        return "color/" + id;
    }

    @Override
    public void appendTemplates(TemplateContext context) {
        context.set("wool", wool);
        context.set("main-texture", "<planks.namespace>:block/%s_planks".formatted(this.id.path()));
        context.set("planks", this.planks);
        context.set("slab", this.slab);
        context.set("log_side", "<main-texture>");
        context.set("log_end", "<main-texture>");
    }
}
