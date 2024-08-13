package juuxel.adorn.data;

import com.google.common.hash.Hashing;
import juuxel.adorn.datagen.DataOutput;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public abstract class AdornCustomDataGenerator implements DataProvider {
    private final FabricDataOutput output;

    public AdornCustomDataGenerator(FabricDataOutput output) {
        this.output = output;
    }

    protected abstract void run(DataOutput output);

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return CompletableFuture.runAsync(() -> {
            run((path, content) -> {
                byte[] data = content.getBytes(StandardCharsets.UTF_8);
                var hashCode = Hashing.sha256().hashBytes(data);

                try {
                    writer.write(this.output.getPath().resolve(path), data, hashCode);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }, Util.getMainWorkerExecutor());
    }
}
