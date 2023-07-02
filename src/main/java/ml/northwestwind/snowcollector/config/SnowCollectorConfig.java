package ml.northwestwind.snowcollector.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;

public class SnowCollectorConfig {
    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec COMMON;

    public static ForgeConfigSpec.BooleanValue ONLY_SNOW;

    static {
        init();
        COMMON = COMMON_BUILDER.build();
    }

    public static void loadConfig(String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        COMMON.setConfig(file);
    }

    public static void init() {
        ONLY_SNOW = COMMON_BUILDER.comment("Make the igloo to only work if snowing instead of raining.").define("snowcollector.only_snow", false);
    }
}
