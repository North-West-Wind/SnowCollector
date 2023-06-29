package ml.northwestwind.snowcollector.blockentity;

import ml.northwestwind.snowcollector.SnowCollector;
import ml.northwestwind.snowcollector.registry.SCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IglooBlockEntity extends BlockEntity {
    private static final int MAX_PROGRESS = 100;
    private int progressTicks = 0;
    private final LazyOptional<IItemHandler> inventoryHandlerLazyOptional = LazyOptional.of(ItemStackHandler::new);

    protected NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    public IglooBlockEntity(BlockPos pos, BlockState state) {
        super(SCBlockEntityTypes.IGLOO.get(), pos, state);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        boolean isSnowing = level.isRainingAt(pos) && level.getBiome(pos).value().getPrecipitation() == Biome.Precipitation.SNOW;
        boolean isOpenSky = level.canSeeSky(pos.above());

    }

    protected void saveEvenMore(CompoundTag tag) {
        tag.putInt("Progress", progressTicks);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        saveEvenMore(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.progressTicks = tag.getInt("Progress");
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveEvenMore(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side == Direction.DOWN) {
            return inventoryHandlerLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inventoryHandlerLazyOptional.invalidate();
    }
}
