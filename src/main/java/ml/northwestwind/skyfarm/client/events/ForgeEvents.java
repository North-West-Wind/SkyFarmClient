package ml.northwestwind.skyfarm.client.events;

import com.mojang.blaze3d.systems.RenderSystem;
import ml.northwestwind.skyfarm.client.SkyFarmClient;
import ml.northwestwind.skyfarm.client.command.ConfigCommand;
import ml.northwestwind.skyfarm.client.config.SkyFarmConfig;
import ml.northwestwind.skyfarm.client.discord.Discord;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SkyFarmClient.MOD_ID, value = Dist.CLIENT)
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

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void removeFog(EntityViewRenderEvent.FogDensity event) {
        if (SkyFarmConfig.NO_FOG.get()) {
            Entity entity = Minecraft.getInstance().getCameraEntity();
            if (!(entity instanceof LivingEntity) || !((LivingEntity)entity).hasEffect(Effects.BLINDNESS)) {
                float farPlane = (float)(Minecraft.getInstance().options.renderDistance * 16);
                RenderSystem.fogStart(0.0F);
                RenderSystem.fogEnd(farPlane * 16.0F);
                event.setDensity(0.0F);
                event.setCanceled(true);
            }
        }

    }
}
