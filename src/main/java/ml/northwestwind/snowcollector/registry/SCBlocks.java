package ml.northwestwind.snowcollector.registry;

import ml.northwestwind.snowcollector.SnowCollector;
import ml.northwestwind.snowcollector.block.IglooBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SCBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SnowCollector.MOD_ID);

    public static final RegistryObject<Block> IGLOO = BLOCKS.register("igloo", IglooBlock::new);

    public static void registerBlocks() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
