package juuxel.adorn.datagen;

public final class SimpleWoodMaterial implements WoodMaterial {
    private final Id id;
    private final boolean nonFlammable;
    private final Id slab;
    private final Id planks;
    private final String logName;
    private final String snowflake;

    public SimpleWoodMaterial(Id id, boolean fungus, boolean nonFlammable) {
        this.id = id;
        this.nonFlammable = nonFlammable;
        this.slab = id.suffixed("slab");
        this.planks = id.suffixed("planks");
        this.logName = fungus ? "stem" : "log";
        this.snowflake = "wood/%s/fungus=%s".formatted(id, fungus);
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
        return snowflake;
    }

    @Override
    public boolean isNonFlammable() {
        return nonFlammable;
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
    public void appendTemplates(TemplateContext context) {
        context.set("main-texture", "<planks.namespace>:block/%s<wood_texture_separator>planks".formatted(this.id.path()));
        context.set("planks", this.planks);
        context.set("slab", this.slab);
        context.set("log_type", this.logName);
        context.setId("log", this.id.namespace(), this.id.path() + "_<log_type>");
        context.set("log_side", "<log.namespace>:block/%s<wood_texture_separator><log_type>".formatted(this.id.path()));
        context.set("log_end", "<log.namespace>:block/%s<wood_texture_separator><log_type>_top".formatted(this.id.path()));
    }
}
