package dev.microcontrollers.overlaytweaks.mixin;

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

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow
    public float vignetteDarkness;
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

}
