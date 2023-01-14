package juuxel.adorn.util

import com.mojang.serialization.DataResult
import com.mojang.serialization.Decoder
import com.mojang.serialization.Encoder
import juuxel.adorn.lib.Registered
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.item.ItemStack
import net.minecraft.menu.MenuContext
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtOps
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.property.Property
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.function.Function

fun ItemStack.toTextWithCount(): MutableText =
    Text.translatable("text.adorn.item_stack_with_count", count, toHoverableText())

fun BlockState.withBlock(block: Block): BlockState =
    entries.entries.fold(block.defaultState) { acc, (key, value) ->
        @Suppress("UNCHECKED_CAST") // Cast to Comparable<Any>
        acc.with(key as Property<Comparable<Any>>, value as Comparable<Any>)
    }

/**
 * Gets the squared distance of this block entity to ([x], [y], [z]).
 *
 * Used to be in vanilla but was removed.
 */
fun BlockEntity.getSquaredDistance(x: Double, y: Double, z: Double): Double {
    val xd = pos.x + 0.5 - x
    val yd = pos.y + 0.5 - y
    val zd = pos.z + 0.5 - z
    return xd * xd + yd * yd + zd * zd
}

/**
 * Creates a safe copy of this block's settings.
 *
 * The safe copy does not have lambdas that reference this block directly.
 * Instead, the default state is used for the various lambdas.
 */
fun Block.copySettingsSafely(): AbstractBlock.Settings =
    AbstractBlock.Settings.of(defaultState.material)
        .luminance { defaultState.luminance }
        .apply { getHardness(defaultState)?.let(this::hardness) }
        .resistance(blastResistance)
        .velocityMultiplier(velocityMultiplier)
        .jumpVelocityMultiplier(jumpVelocityMultiplier)
        .slipperiness(slipperiness)
        .sounds(defaultState.soundGroup)

private fun getHardness(state: BlockState): Float? =
    try {
        state.getHardness(null, null)
    } catch (e: NullPointerException) {
        null
    }

fun Direction.Axis.turnHorizontally(): Direction.Axis =
    when (this) {
        Direction.Axis.X -> Direction.Axis.Z
        Direction.Axis.Z -> Direction.Axis.X
        Direction.Axis.Y -> Direction.Axis.Y
    }

fun Direction.Axis.getDirection(axisDirection: Direction.AxisDirection): Direction =
    when (axisDirection) {
        Direction.AxisDirection.POSITIVE -> when (this) {
            Direction.Axis.X -> Direction.EAST
            Direction.Axis.Y -> Direction.UP
            Direction.Axis.Z -> Direction.SOUTH
        }

        Direction.AxisDirection.NEGATIVE -> when (this) {
            Direction.Axis.X -> Direction.WEST
            Direction.Axis.Y -> Direction.DOWN
            Direction.Axis.Z -> Direction.NORTH
        }
    }

fun <K, V> Array<K>.associateLazily(mapper: (K) -> Registered<V>): Registered<Map<K, V>> {
    val pairs = map { it to mapper(it) }
    val map = lazy { pairs.associate { (key, value) -> key to value.get() } }
    return Registered(map::value)
}

/**
 * Gets the block entity located at this context's position.
 */
fun MenuContext.getBlockEntity(): BlockEntity? =
    get { world: World, pos: BlockPos -> world.getBlockEntity(pos) }
        .orElse(null)

/**
 * Gets the block located at this context's position.
 */
fun MenuContext.getBlock() =
    get { world, pos -> world.getBlockState(pos).block }
        .orElse(Blocks.AIR)

/**
 * Creates a menu context in the world and at the position of the [blockEntity].
 * In a way, the inverse operation to [getBlockEntity].
 */
fun menuContextOf(blockEntity: BlockEntity): MenuContext =
    MenuContext.create(blockEntity.world, blockEntity.pos)

/**
 * Syncs this block entity to the client. If not running on the server, crashes.
 */
fun BlockEntity.syncToClient() {
    val world = this.world

    if (world is ServerWorld) {
        world.chunkManager.markForUpdate(pos)
    } else {
        throw IllegalStateException("[Adorn] Can't sync server->client from the server. What on earth am I doing?")
    }
}

fun <T> PacketByteBuf.writeNbtWithCodec(encoder: Encoder<in T>, value: T) {
    val nbt = encoder.encodeStart(NbtOps.INSTANCE, value).get().map(Function.identity()) {
        throw IllegalArgumentException("Could not write $value to NBT using $encoder: ${it.message()}")
    }
    val wrapper = NbtCompound()
    wrapper.put("Value", nbt)
    writeNbt(wrapper)
}

fun <T> PacketByteBuf.readNbtWithCodec(decoder: Decoder<out T>): T {
    val wrapper = readNbt()!!
    val nbt = wrapper.get("Value")!!
    return decoder.decode(NbtOps.INSTANCE, nbt).map { it.first }.get().map(Function.identity()) {
        throw RuntimeException("Could not read a value from NBT using $decoder: ${it.message()}")
    }
}
