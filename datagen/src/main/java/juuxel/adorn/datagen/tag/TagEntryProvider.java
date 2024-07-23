package juuxel.adorn.datagen.tag;

import juuxel.adorn.datagen.Id;
import juuxel.adorn.datagen.Material;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface TagEntryProvider {
    List<Entry> getEntries(Collection<? extends Material> materials);

    record Entry(Material material, Id id, boolean isModded) {
    }

    record Simple(String blockType) implements TagEntryProvider {
        @Override
        public List<Entry> getEntries(Collection<? extends Material> materials) {
            List<Entry> result = new ArrayList<>();

            for (Material material : materials) {
                var path = material.isModded()
                    ? material.getId().namespace() + "/" + material.getId().path()
                    : material.getId().path();
                var id = new Id("adorn", path);
                result.add(new Entry(material, id.suffixed(blockType), material.isModded()));
            }

            return result;
        }
    }

    record Filtered(TagEntryProvider parent, Predicate<Material> predicate) implements TagEntryProvider {
        @Override
        public List<Entry> getEntries(Collection<? extends Material> materials) {
            return parent.getEntries(materials)
                .stream()
                .filter(entry -> predicate.test(entry.material()))
                .toList();
        }
    }

    record Multi(List<TagEntryProvider> parents) implements TagEntryProvider {
        public Multi(TagEntryProvider... parents) {
            this(List.of(parents));
        }

        @Override
        public List<Entry> getEntries(Collection<? extends Material> materials) {
            List<Entry> entries = new ArrayList<>();

            for (var parent : parents) {
                entries.addAll(parent.getEntries(materials));
            }

            return entries;
        }
    }
}
