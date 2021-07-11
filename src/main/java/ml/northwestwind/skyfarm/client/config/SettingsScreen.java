package ml.northwestwind.skyfarm.client.config;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SettingsScreen extends Screen {
    private static final List<Triple<String, Supplier<Boolean>, Consumer<Boolean>>> buttons = Lists.newArrayList(
            new ImmutableTriple<>("skybox", () -> SkyFarmConfig.GOG_SKYBOX.get(), SkyFarmConfig::setGogSkybox),
            new ImmutableTriple<>("no_horizon", () -> SkyFarmConfig.NO_HORIZON.get(), SkyFarmConfig::setNoHorizon),
            new ImmutableTriple<>("no_fog", () -> SkyFarmConfig.NO_FOG.get(), SkyFarmConfig::setNoFog),
            new ImmutableTriple<>("clear_skies", () -> SkyFarmConfig.CLEAR_SKIES.get(), SkyFarmConfig::setClearSkies)
    );

    public SettingsScreen() {
        super(new TranslationTextComponent("screen.skyfarm.settings"));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        for (int i = 0; i < buttons.size(); i++) {
            Triple<String, Supplier<Boolean>, Consumer<Boolean>> triple = buttons.get(i);
            Button button = new Button(this.width / 2 - 75, this.height / 2 - 40 + 25 * i, 150, 20, getText(triple.getLeft(), triple.getMiddle().get()),
                    b -> {
                        triple.getRight().accept(!triple.getMiddle().get());
                        b.setMessage(getText(triple.getLeft(), triple.getMiddle().get()));
                    },
                    (b, matrix, mouseX, mouseY) -> renderWrappedToolTip(matrix, Lists.newArrayList(new TranslationTextComponent("tooltip.skyfarm."+triple.getLeft())), mouseX, mouseY, this.font)
            );
            addButton(button);
        }
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrix);
        drawCenteredString(matrix, this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(matrix, mouseX, mouseY, partialTicks);
    }

    private static ITextComponent getText(String feature, boolean enabled) {
        return new TranslationTextComponent("option.skyfarm."+feature, new TranslationTextComponent("option.skyfarm."+enabled).withStyle(enabled ? TextFormatting.GREEN : TextFormatting.RED));
    }
}
