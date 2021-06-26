package ml.northwestwind.skyfarm.client.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;

public class SkyFarmConfig {
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec CLIENT;

    public static ForgeConfigSpec.BooleanValue GOG_SKYBOX, NO_HORIZON, NO_FOG, CLEAR_SKIES;

    static {
        init();
        CLIENT = CLIENT_BUILDER.build();
    }

    public static void loadClientConfig(String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        CLIENT.setConfig(file);
    }

    private static void init() {
        GOG_SKYBOX = CLIENT_BUILDER.comment("Whether or not to enable the Garden of Glass skybox").define("gog_skybox", true);
        NO_HORIZON = CLIENT_BUILDER.comment("Removes the annoying black sky under y=64").define("no_horizon", true);
        NO_FOG = CLIENT_BUILDER.comment("Removes distant fog").define("no_fog", true);
        CLEAR_SKIES = CLIENT_BUILDER.comment("Removes horizon fog").define("clear_skies", true);
    }

    public static void setGogSkybox(boolean enabled) {
        GOG_SKYBOX.set(enabled);
        GOG_SKYBOX.save();
    }

    public static void setNoHorizon(boolean enabled) {
        NO_HORIZON.set(enabled);
        NO_HORIZON.save();
    }

    public static void setNoFog(boolean enabled) {
        NO_FOG.set(enabled);
        NO_FOG.save();
    }

    public static void setClearSkies(boolean enabled) {
        CLEAR_SKIES.set(enabled);
        CLEAR_SKIES.save();
    }
}