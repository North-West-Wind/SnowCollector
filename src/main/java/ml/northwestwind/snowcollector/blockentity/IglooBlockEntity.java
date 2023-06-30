package ml.northwestwind.snowcollector.blockentity;

import ml.northwestwind.snowcollector.block.IglooBlock;
import ml.northwestwind.snowcollector.registry.SCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IglooBlockEntity extends BlockEntity {
    public static final int MAX_PROGRESS = 100;
    private int progressTicks = 0;

    public NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private final LazyOptional<IItemHandler> inventoryHandlerLazyOptional = LazyOptional.of(() -> new ItemStackHandler(items));

    public IglooBlockEntity(BlockPos pos, BlockState state) {
        super(SCBlockEntityTypes.IGLOO.get(), pos, state);
    }

    public int getProgress() {
        return progressTicks;
    }

    public void setProgress(int progress) {
        this.progressTicks = progress;
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        boolean isSnowing = level.isRaining() && level.canSeeSky(pos.above()) && level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos.above()).getY() <= pos.above().getY() && level.getBiome(pos).value().getPrecipitation() == Biome.Precipitation.SNOW;
        boolean snowy = state.getValue(IglooBlock.SNOWY);
        if (isSnowing != snowy) {
            snowy = isSnowing;
            level.setBlockAndUpdate(pos, state.setValue(IglooBlock.SNOWY, snowy));
        }
        if (!snowy) return;
        IglooBlockEntity igloo = (IglooBlockEntity) blockEntity;
        int progress = igloo.getProgress();
        int lastProgress = progress;
        progress++;
        if (progress >= MAX_PROGRESS) {
            ItemStack stack = igloo.items.get(0);
            if (stack.isEmpty()) {
                igloo.items.set(0, Items.SNOW_BLOCK.getDefaultInstance());
                progress = 0;
                level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.SNOW_FALL, SoundSource.BLOCKS, 1, 1, false);
            } else if (stack.is(Items.SNOW_BLOCK) && stack.getCount() < 64) {
                stack.setCount(stack.getCount() + 1);
                progress = 0;
                level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.SNOW_FALL, SoundSource.BLOCKS, 1, 1, false);
            } else {
                progress = MAX_PROGRESS;
            }
        }
        igloo.setProgress(progress);
        if (lastProgress != progress) igloo.setChanged();
    }

    protected void saveEvenMore(CompoundTag tag) {
        tag.putInt("Progress", progressTicks);
        ContainerHelper.saveAllItems(tag, items);
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
        this.items = NonNullList.withSize(1, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items);
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
