package juuxel.adorn.datagen;

public interface BuildingMaterial extends Material {
    Id getPlanks(); // this is really the "base" block of the material so planks or something like cobblestone
    Id getSlab();
}
