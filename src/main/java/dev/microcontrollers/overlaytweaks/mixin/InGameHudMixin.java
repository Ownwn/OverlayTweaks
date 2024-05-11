package dev.microcontrollers.overlaytweaks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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
    @Final
    @Shadow
    private static Identifier CROSSHAIR_ATTACK_INDICATOR_FULL_TEXTURE;
    @Final
    @Shadow
    private static Identifier CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_TEXTURE;
    @Final
    @Shadow
    private static Identifier CROSSHAIR_ATTACK_INDICATOR_PROGRESS_TEXTURE;
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

    @ModifyArg(method = "renderMiscOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V", ordinal = 0), index = 2)
    private float changePumpkinOpacity(float opacity) {
        return OverlayTweaksConfig.CONFIG.instance().pumpkinOpacity / 100F;
    }

    @ModifyArg(method = "renderMiscOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V", ordinal = 1), index = 2)
    private float changeFreezingOpacity(float opacity) {
        return opacity * OverlayTweaksConfig.CONFIG.instance().freezingOpacity / 100F;
    }

    @Inject(method = "renderSpyglassOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIFFIIII)V", shift = At.Shift.BEFORE))
    private void changeSpyglassOpacityPre(DrawContext context, float scale, CallbackInfo ci) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, OverlayTweaksConfig.CONFIG.instance().spyglassOpacity);
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

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void removeCrosshairInContainer(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (MinecraftClient.getInstance().currentScreen != null && OverlayTweaksConfig.CONFIG.instance().hideCrosshairInContainers)
            ci.cancel();
    }

    @ModifyExpressionValue(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/Perspective;isFirstPerson()Z"))
    private boolean removeFirstPersonCheck(boolean original) {
        if (OverlayTweaksConfig.CONFIG.instance().showCrosshairInPerspective) return true;
        else return original;
    }

    @WrapWithCondition(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFuncSeparate(Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;)V"))
    private boolean removeCrosshairBlending(GlStateManager.SrcFactor srcFactor, GlStateManager.DstFactor dstFactor, GlStateManager.SrcFactor srcAlpha, GlStateManager.DstFactor dstAlpha) {
        return !OverlayTweaksConfig.CONFIG.instance().removeCrosshairBlending;
    }

    @Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFuncSeparate(Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;)V", shift = At.Shift.AFTER))
    private void changeCrosshairOpacity(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (OverlayTweaksConfig.CONFIG.instance().removeCrosshairBlending)
            RenderSystem.setShaderColor(1F, 1F, 1F, OverlayTweaksConfig.CONFIG.instance().crosshairOpacity / 100F);
    }

    @ModifyArg(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;renderCrosshair(I)V"))
    private int changeDebugCrosshairSize(int size) {
        return OverlayTweaksConfig.CONFIG.instance().debugCrosshairSize;
    }

    @ModifyExpressionValue(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;shouldShowDebugHud()Z"))
    private boolean cancelDebugCrosshair(boolean original) {
        if (OverlayTweaksConfig.CONFIG.instance().useNormalCrosshair) return false;
        else if (OverlayTweaksConfig.CONFIG.instance().useDebugCrosshair) return true;
        return original;
    }

    @Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;applyModelViewMatrix()V", ordinal = 0))
    private void showCooldownOnDebug(DrawContext context, float tickDelta, CallbackInfo ci) {
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        if (!OverlayTweaksConfig.CONFIG.instance().fixDebugCooldown) return;
        if (this.client.options.getAttackIndicator().getValue() == AttackIndicator.CROSSHAIR) {
            assert this.client.player != null;
            float attackCooldownProgress = this.client.player.getAttackCooldownProgress(0.0F);
            boolean bool = false;
            if (this.client.targetedEntity != null && this.client.targetedEntity instanceof LivingEntity && attackCooldownProgress >= 1.0F) {
                bool = this.client.player.getAttackCooldownProgressPerTick() > 5.0F;
                bool &= this.client.targetedEntity.isAlive();
            }
            int height = context.getScaledWindowHeight() / 2 - 7 + 16;
            int width = context.getScaledWindowWidth() / 2 - 8;
            if (bool) {
                context.drawGuiTexture(CROSSHAIR_ATTACK_INDICATOR_FULL_TEXTURE, width, height, 16, 16);
            } else if (attackCooldownProgress < 1.0F) {
                int l = (int)(attackCooldownProgress * 17.0F);
                context.drawGuiTexture(CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_TEXTURE, width, height, 16, 4);
                context.drawGuiTexture(CROSSHAIR_ATTACK_INDICATOR_PROGRESS_TEXTURE, 16, 4, 0, 0, width, height, l, 4);
            }
        }
        RenderSystem.defaultBlendFunc();
    }

    @ModifyReturnValue(method = "shouldRenderSpectatorCrosshair", at = @At("RETURN"))
    private boolean showInSpectator(boolean original) {
        if (OverlayTweaksConfig.CONFIG.instance().showCrosshairInSpectator) return true;
        return original;
    }

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At("HEAD"), cancellable = true)
    private void removeScoreboardInDebug(CallbackInfo ci) {
        if (OverlayTweaksConfig.CONFIG.instance().hideScoreboardInDebug && client.getDebugHud().shouldShowDebugHud()) ci.cancel();
    }

    /*
        The following methods were taken from Easeify under LGPLV3
        https://github.com/Polyfrost/Easeify/blob/main/LICENSE
        The code has been updated to 1.20 and with several fixes
     */

    @ModifyExpressionValue(method = "renderTitleAndSubtitle", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I"))
    private int disableTitles(int value) {
        if (OverlayTweaksConfig.CONFIG.instance().disableTitles) return 0;
        else return value;
    }

    @Inject(method = "renderTitleAndSubtitle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V", ordinal = 0, shift = At.Shift.AFTER))
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

    @Inject(method = "renderTitleAndSubtitle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V", ordinal = 1, shift = At.Shift.AFTER))
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

    @ModifyConstant(method = "renderTitleAndSubtitle", constant = @Constant(intValue = 255, ordinal = 0))
    private int modifyOpacity(int constant) {
        return (int) (OverlayTweaksConfig.CONFIG.instance().titleOpacity / 100 * 255);
    }

}
