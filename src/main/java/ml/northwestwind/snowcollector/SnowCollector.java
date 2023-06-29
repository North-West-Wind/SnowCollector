package ml.northwestwind.snowcollector;

import ml.northwestwind.snowcollector.registry.SCBlockEntityTypes;
import ml.northwestwind.snowcollector.registry.SCBlocks;
import ml.northwestwind.snowcollector.registry.SCItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SnowCollector.MOD_ID)
public class SnowCollector {
    public static final String MOD_ID = "snowcollector";
    public static final Logger LOGGER = LogManager.getLogger();

    public SnowCollector() {
        SCBlocks.registerBlocks();
        SCBlockEntityTypes.registerBlockEntityTypes();
        SCItems.registerItems();
    }

    public static class SnowCollectorCreativeModeTab extends CreativeModeTab {
        public static final SnowCollectorCreativeModeTab INSTANCE = new SnowCollectorCreativeModeTab();

        public SnowCollectorCreativeModeTab() {
            super(MOD_ID);
        }

        @Override
        public ItemStack makeIcon() {
            return SCItems.IGLOO.get().getDefaultInstance();
        }
    }
}
