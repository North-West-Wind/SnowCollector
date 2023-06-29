package ml.northwestwind.snowcollector.registry;

import ml.northwestwind.snowcollector.SnowCollector;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SCItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SnowCollector.MOD_ID);
    public static final RegistryObject<Item> IGLOO = ITEMS.register("igloo", () -> new BlockItem(SCBlocks.IGLOO.get(), new Item.Properties().tab(SnowCollector.SnowCollectorCreativeModeTab.INSTANCE)));

    public static void registerItems() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
