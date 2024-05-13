package dev.microcontrollers.overlaytweaks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// BetterF3 has a priority of 1100. This is to prevent a crash with cancelDebugCrosshair.
@Mixin(value = InGameHud.class, priority = 1200)
public class InGameHudMixin {
    @Shadow
    public float vignetteDarkness;
    @Shadow
    private Text title;
    @Shadow
    private Text subtitle;
    @Mutable
    @Final
    @Shadow
    private final DebugHud debugHud;
    @Unique
    MinecraftClient client = MinecraftClient.getInstance();

    public InGameHudMixin(DebugHud debugHud) {
        this.debugHud = debugHud;
    }

    @Inject(method = "updateVignetteDarkness", at = @At("TAIL"))
    private void changeVignetteDarkness(Entity entity, CallbackInfo ci) {
        if (OverlayTweaksConfig.CONFIG.instance().customVignetteDarkness)
            this.vignetteDarkness = OverlayTweaksConfig.CONFIG.instance().customVignetteDarknessValue / 100;
    }

    //#if MC >= 1.20.6
    @ModifyArg(method = "renderMiscOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V", ordinal = 0), index = 2)
    //#else
    //$$ @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V", ordinal = 0), index = 2)
    //#endif
    private float changePumpkinOpacity(float opacity) {
        return OverlayTweaksConfig.CONFIG.instance().pumpkinOpacity / 100F;
    }

    //#if MC >= 1.20.6
    @ModifyArg(method = "renderMiscOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V", ordinal = 1), index = 2)
    //#else
    //$$ @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V", ordinal = 1), index = 2)
    //#endif
    private float changeFreezingOpacity(float opacity) {
        return opacity * OverlayTweaksConfig.CONFIG.instance().freezingOpacity / 100F;
    }

    @Inject(method = "renderSpyglassOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIFFIIII)V", shift = At.Shift.BEFORE))
    private void changeSpyglassOpacityPre(DrawContext context, float scale, CallbackInfo ci) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, OverlayTweaksConfig.CONFIG.instance().spyglassOpacity / 100F);
    }
    @Inject(method = "renderSpyglassOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIFFIIII)V", shift = At.Shift.AFTER))
    private void changeSpyglassOpacityPost(DrawContext context, float scale, CallbackInfo ci) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);
    }

    @ModifyConstant(method = "renderSpyglassOverlay", constant = @Constant(intValue = -16777216))
    private int changeSpyglassColor(int constant) {
        return OverlayTweaksConfig.CONFIG.instance().spyglassColor.getRGB();
    }

    @Inject(method = "renderHeldItemTooltip", at = @At("HEAD"), cancellable = true)
    private void removeItemTooltip(DrawContext context, CallbackInfo ci) {
        if (OverlayTweaksConfig.CONFIG.instance().removeItemTooltip) ci.cancel();
    }

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At("HEAD"), cancellable = true)
    private void removeScoreboardInDebug(CallbackInfo ci) {
        //#if MC >= 1.20.4
        if (OverlayTweaksConfig.CONFIG.instance().hideScoreboardInDebug && client.getDebugHud().shouldShowDebugHud()) {
        //#else
        //$$ if (OverlayTweaksConfig.CONFIG.instance().hideScoreboardInDebug && MinecraftClient.getInstance().options.debugEnabled) {
        //#endif
            ci.cancel();
        }
    }

    /*
        The following methods were taken from Easeify under LGPLV3
        https://github.com/Polyfrost/Easeify/blob/main/LICENSE
        The code has been updated to 1.20 and with several fixes
     */

    //#if MC >= 1.20.6
    @ModifyExpressionValue(method = "renderTitleAndSubtitle", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I"))
    //#else
    //$$ @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I"))
    //#endif
    private int disableTitles(int value) {
        if (OverlayTweaksConfig.CONFIG.instance().disableTitles) return 0;
        else return value;
    }

    //#if MC >= 1.20.6
    @Inject(method = "renderTitleAndSubtitle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V", ordinal = 0, shift = At.Shift.AFTER))
    //#else
    //$$ @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V", ordinal = 0, shift = At.Shift.AFTER))
    //#endif
    private void modifyTitleScale(DrawContext context, float tickDelta, CallbackInfo ci) {
        float titleScale = OverlayTweaksConfig.CONFIG.instance().titleScale / 100;
        // TODO: MCCIsland uses a giant title to black out your screen when switching worlds, so let's keep that. Find a better way to only keep black out
        if (OverlayTweaksConfig.CONFIG.instance().autoTitleScale && (MinecraftClient.getInstance().getCurrentServerEntry() != null && !MinecraftClient.getInstance().getCurrentServerEntry().address.contains("mccisland.net"))) {
            final float width = MinecraftClient.getInstance().textRenderer.getWidth(title) * 4.0F;
            if (width > context.getScaledWindowWidth()) {
                titleScale = (context.getScaledWindowWidth() / width) * OverlayTweaksConfig.CONFIG.instance().titleScale / 100;
            }
        }
        context.getMatrices().scale(titleScale, titleScale, titleScale);
    }

    //#if MC >= 1.20.6
    @Inject(method = "renderTitleAndSubtitle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V", ordinal = 1, shift = At.Shift.AFTER))
    //#else
    //$$ @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V", ordinal = 1, shift = At.Shift.AFTER))
    //#endif
    private void modifySubtitleScale(DrawContext context, float tickDelta, CallbackInfo ci) {
        float titleScale = OverlayTweaksConfig.CONFIG.instance().titleScale / 100;
        // TODO: MCCIsland uses a giant title to black out your screen when switching worlds, so let's keep that. Find a better way to only keep black out
        if (OverlayTweaksConfig.CONFIG.instance().autoTitleScale && (MinecraftClient.getInstance().getCurrentServerEntry() != null && !MinecraftClient.getInstance().getCurrentServerEntry().address.contains("mccisland.net"))) {
            final float width = MinecraftClient.getInstance().textRenderer.getWidth(subtitle) * 2.0F;
            if (width > context.getScaledWindowWidth()) {
                titleScale = (context.getScaledWindowWidth() / width) * OverlayTweaksConfig.CONFIG.instance().titleScale / 100;
            }
        }
        context.getMatrices().scale(titleScale, titleScale, titleScale);
    }

    //#if MC >= 1.20.6
    @ModifyConstant(method = "renderTitleAndSubtitle", constant = @Constant(intValue = 255, ordinal = 0))
    //#else
    //$$ @ModifyConstant(method = "render", constant = @Constant(intValue = 255, ordinal = 3))
    //#endif
    private int modifyOpacity(int constant) {
        return (int) (OverlayTweaksConfig.CONFIG.instance().titleOpacity / 100 * 255);
    }

}
