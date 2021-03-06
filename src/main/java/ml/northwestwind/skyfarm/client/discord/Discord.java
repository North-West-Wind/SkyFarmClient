package ml.northwestwind.skyfarm.client.discord;

import ml.northwestwind.skyfarm.client.SkyFarmClient;
import net.arikia.dev.drpc.*;
import net.arikia.dev.drpc.callbacks.ReadyCallback;

import javax.annotation.Nullable;

public class Discord {
    public static void startup() {
        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(new ReadyHandler()).build();
        DiscordRPC.discordRunCallbacks();
        DiscordRPC.discordInitialize("826311737455804446", handlers, true);
        Runtime.getRuntime().addShutdownHook(new Thread(DiscordRPC::discordShutdown));
    }

    private static class ReadyHandler implements ReadyCallback {
        @Override
        public void apply(DiscordUser user) {
            SkyFarmClient.LOGGER.info(String.format("Connected to %s#%s. Discord ready.", user.username, user.discriminator));
        }
    }

    public static void updateRichPresence(String state, @Nullable String details, @Nullable DiscordImage bigImg, @Nullable DiscordImage smallImg) {
        DiscordRichPresence.Builder presence = new DiscordRichPresence.Builder(state).setStartTimestamps(System.currentTimeMillis());
        if (details != null) presence.setDetails(details);
        if (bigImg == null) presence.setBigImage("sky_farm", "");
        else presence.setBigImage(bigImg.getKey(), bigImg.getText());
        if (smallImg != null) presence.setSmallImage(smallImg.getKey(), smallImg.getText());
        DiscordRPC.discordUpdatePresence(presence.build());
    }

    public static class DiscordImage {
        private final String key, text;

        public DiscordImage(String key, String text) {
            this.key = key;
            this.text = text;
        }

        public String getKey() {
            return key;
        }

        public String getText() {
            return text;
        }
    }
}
