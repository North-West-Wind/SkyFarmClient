package ml.northwestwind.skyfarm.client.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import ml.northwestwind.skyfarm.client.Utils;
import ml.northwestwind.skyfarm.client.config.SkyFarmConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow
    @Final
    private VertexFormat skyFormat;

    @Shadow
    @Nullable
    private VertexBuffer starBuffer;

    /**
     * Render planets and other extras, after the first invoke to ms.mulPose(Y) after getRainStrength is called
     */
    @Inject(
            method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V",
            slice = @Slice(
                    from = @At(
                            ordinal = 0, value = "INVOKE",
                            target = "Lnet/minecraft/client/world/ClientWorld;getRainLevel(F)F"
                    )
            ),
            at = @At(
                    shift = At.Shift.AFTER,
                    ordinal = 0,
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/matrix/MatrixStack;mulPose(Lnet/minecraft/util/math/vector/Quaternion;)V"
            ),
            require = 0
    )
    private void renderExtras(MatrixStack ms, float partialTicks, CallbackInfo ci) {
        if (ModList.get().isLoaded("botania") && SkyFarmConfig.GOG_SKYBOX.get()) Utils.renderExtra(ms, Minecraft.getInstance().level, partialTicks, 0);
    }

    /**
     * Make the sun bigger, replace any 30.0F seen before first call to bind
     */
    @ModifyConstant(
            method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V",
            slice = @Slice(to = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bind(Lnet/minecraft/util/ResourceLocation;)V")),
            constant = @Constant(floatValue = 30.0F),
            require = 0
    )
    private float makeSunBigger(float oldValue) {
        return SkyFarmConfig.GOG_SKYBOX.get() ? 60f : oldValue;
    }

    /**
     * Make the moon bigger, replace any 20.0F seen between first and second call to bind
     */
    @ModifyConstant(
            method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V",
            slice = @Slice(
                    from = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bind(Lnet/minecraft/util/ResourceLocation;)V"),
                    to = @At(ordinal = 1, value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bind(Lnet/minecraft/util/ResourceLocation;)V")
            ),
            constant = @Constant(floatValue = 20.0F),
            require = 0
    )
    private float makeMoonBigger(float oldValue) {
        return SkyFarmConfig.GOG_SKYBOX.get() ? 60f : oldValue;
    }

    @Redirect(
            at = @At(value = "INVOKE",target = "Lnet/minecraft/client/world/ClientWorld$ClientWorldInfo;getHorizonHeight()D"),
            method = "renderSky",
            slice = @Slice(
                    from = @At(ordinal = 3, value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;disableTexture()V"),
                    to = @At(ordinal = 1, value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableTexture()V")
            ),
            require = 0
    )
    private double horizonDistance(ClientWorld.ClientWorldInfo clientWorldInfo) {
        return SkyFarmConfig.NO_HORIZON.get() ? -64 : clientWorldInfo.getHorizonHeight();
    }

    /**
     * Render lots of extra stars
     */
    @Inject(
            method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getStarBrightness(F)F"),
            require = 0
    )
    private void renderExtraStars(MatrixStack ms, float partialTicks, CallbackInfo ci) {
        if (SkyFarmConfig.GOG_SKYBOX.get()) Utils.renderStars(skyFormat, starBuffer, ms, partialTicks);
    }

}