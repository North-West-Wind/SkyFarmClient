package ml.northwestwind.skyfarm.client.events;

import ml.northwestwind.skyfarm.client.SkyFarmDiscord;
import ml.northwestwind.skyfarm.client.config.SkyFarmConfig;
import ml.northwestwind.skyfarm.client.discord.Discord;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod.EventBusSubscriber(modid = SkyFarmDiscord.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SetupEvents {
    @SubscribeEvent
    public static void setup(final FMLClientSetupEvent event) {
        SkyFarmConfig.loadClientConfig(FMLPaths.CONFIGDIR.get().resolve("skyfarm-client.toml").toString());
        Discord.startup();
        Discord.updateRichPresence("Starting Sky Farm...", "Mods are loading...", new Discord.DiscordImage("loading", "Loading..."), null);
    }
}
