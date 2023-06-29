package ml.northwestwind.snowcollector.registry;

import ml.northwestwind.snowcollector.SnowCollector;
import ml.northwestwind.snowcollector.blockentity.IglooBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SCBlockEntityTypes {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, SnowCollector.MOD_ID);
    public static final RegistryObject<BlockEntityType<?>> IGLOO = BLOCK_ENTITY_TYPES.register("igloo", () -> BlockEntityType.Builder.of(IglooBlockEntity::new, SCBlocks.IGLOO.get()).build(null));

    public static void registerBlockEntityTypes() {
        BLOCK_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
