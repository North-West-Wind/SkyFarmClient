package ml.northwestwind.skyfarm.discord;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

@Mod("skyfarm_discord")
public class SkyFarmDiscord
{
    public static final Logger LOGGER = LogManager.getLogger();

    public SkyFarmDiscord() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SkyFarmDiscord::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void menuOpened(final GuiOpenEvent event) {
        Screen screen = event.getGui();
        if (screen == null) return;
        if (screen instanceof MainMenuScreen || screen instanceof MultiplayerScreen) {
            Discord.updateRichPresence("Main Menu", ModList.get().getMods().size() + " Mods Loaded", new Discord.DiscordImage("skyfarm", ""), null);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void playerJoin(final EntityJoinWorldEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        PlayerEntity player = minecraft.player;
        if (player == null) return;
        Entity entity = event.getEntity();
        if (!entity.getUUID().equals(player.getUUID())) return;
        Discord.updateRichPresence(
                minecraft.hasSingleplayerServer() ? "Singleplayer" : "Multiplayer",
                ModList.get().getMods().size() + " Mods Loaded",
                new Discord.DiscordImage("skyfarm", "Farming in the Sky"),
                minecraft.hasSingleplayerServer() ? new Discord.DiscordImage("singleplayer", "For themselves") : new Discord.DiscordImage("multiplayer", "But with friends")
        );
    }

    private static void clientSetup(final FMLClientSetupEvent event) {
        Discord.startup();
        Discord.updateRichPresence("Starting Sky Farm...", "Mods are loading...", new Discord.DiscordImage("loading", "Loading..."), null);
    }
}
