package dev.microcontrollers.overlaytweaks.mixin;

import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static net.minecraft.entity.effect.StatusEffects.FIRE_RESISTANCE;

/*
  Fire overlay height code was taken from Easeify with permission from Wyvest
  Only changes to variables have been made
  https://github.com/Polyfrost/Easeify/blob/main/LICENSE
 */
@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {
    @Unique
    private static boolean hasPushed = false;

    @Inject(method = "renderUnderwaterOverlay", at = @At("HEAD"), cancellable = true)
    private static void cancelWaterOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        if (OverlayTweaksConfig.CONFIG.instance().removeWaterOverlay) ci.cancel();
    }

    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private static void cancelFireOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        if (OverlayTweaksConfig.CONFIG.instance().removeFireOverlay && client.player != null && (client.player.isCreative() || client.player.hasStatusEffect(FIRE_RESISTANCE)))
            ci.cancel();
    }

    @Inject(method = "renderFireOverlay", at = @At("HEAD"))
    private static void moveFireOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        hasPushed = true;
        matrices.push();
        matrices.translate(0, OverlayTweaksConfig.CONFIG.instance().fireOverlayHeight, 0);
    }

    @Inject(method = "renderFireOverlay", at = @At("RETURN"))
    private static void resetFireOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        if (hasPushed) {
            hasPushed = false;
            matrices.pop();
        }
    }

    @ModifyArg(method = "renderFireOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumer;color(FFFF)Lnet/minecraft/client/render/VertexConsumer;"), index = 3)
    private static float enableFireOverlayOpacity(float opacity) {
        return OverlayTweaksConfig.CONFIG.instance().customFireOverlayOpacity / 100F;
    }

    @ModifyArgs(method = "renderInWallOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumer;color(FFFF)Lnet/minecraft/client/render/VertexConsumer;"))
    private static void changeSuffocationOverlayColor(Args args) {
        args.set(0, OverlayTweaksConfig.CONFIG.instance().suffocationOverlayBrightness / 100F);
        args.set(1, OverlayTweaksConfig.CONFIG.instance().suffocationOverlayBrightness / 100F);
        args.set(2, OverlayTweaksConfig.CONFIG.instance().suffocationOverlayBrightness / 100F);
    }

}
