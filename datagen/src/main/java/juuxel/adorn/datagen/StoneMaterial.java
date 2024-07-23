package juuxel.adorn.datagen;

public final class StoneMaterial implements BuildingMaterial {
    private final Id id;
    private final boolean hasSidedTexture;
    private final Id slab;
    private final Id planks;
    private final String snowflake;

    public StoneMaterial(Id id, boolean bricks, boolean hasSidedTexture) {
        this.id = id;
        this.hasSidedTexture = hasSidedTexture;
        this.slab = id.suffixed("slab");
        this.planks = bricks ? id.rawSuffixed("s") : id; // brick => bricks
        this.snowflake = "stone/%s/bricks=%s/hasSidedTexture=%s".formatted(id, bricks, hasSidedTexture);
    }

    @Override
    public Id getId() {
        return id;
    }

    @Override
    public Id getStick() {
        return Material.STONE_ROD;
    }

    @Override
    public String getSnowflake() {
        return snowflake;
    }

    @Override
    public Id getPlanks() {
        return planks;
    }

    @Override
    public Id getSlab() {
        return slab;
    }

    public boolean getHasSidedTexture() {
        return hasSidedTexture;
    }

    @Override
    public void appendTemplates(TemplateContext context) {
        context.set("main-texture", "<planks.namespace>:block/<planks.path>");
        context.set("planks", this.planks);
        context.set("slab", this.slab);
    }
}
