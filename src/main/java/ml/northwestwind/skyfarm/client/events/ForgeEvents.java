package ml.northwestwind.skyfarm.client.events;

import ml.northwestwind.skyfarm.client.SkyFarmDiscord;
import ml.northwestwind.skyfarm.client.command.ConfigCommand;
import ml.northwestwind.skyfarm.client.discord.Discord;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SkyFarmDiscord.MOD_ID, value = Dist.CLIENT)
public class ForgeEvents {
    @SubscribeEvent
    public static void menuOpened(final GuiOpenEvent event) {
        Screen screen = event.getGui();
        if (screen == null) return;
        if (screen instanceof MainMenuScreen || screen instanceof MultiplayerScreen) {
            Discord.updateRichPresence("Main Menu", ModList.get().getMods().size() + " Mods Loaded", new Discord.DiscordImage("skyfarm", ""), null);
        }
    }

    @SubscribeEvent
    public static void playerJoin(final EntityJoinWorldEvent event) {
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

    @SubscribeEvent
    public static void registerCommand(final RegisterCommandsEvent event) {
        ConfigCommand.registerCommand(event.getDispatcher());
    }
}
