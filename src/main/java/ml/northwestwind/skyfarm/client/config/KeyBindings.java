package ml.northwestwind.skyfarm.client.config;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static final KeyBinding openMenu = new KeyBinding("key.skyfarm.settings", GLFW.GLFW_KEY_BACKSLASH, "category.skyfarm.client");

    public static void registerKeybindings() {
        ClientRegistry.registerKeyBinding(openMenu);
    }
}
