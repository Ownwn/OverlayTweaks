package dev.microcontrollers.overlaytweaks.mixin.screenopacity;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin {
    //#if MC >= 1.20.4
    @Inject(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
    //#else
    //$$ @Inject(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"))
    //#endif
    private void buttonOpacityStart(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1F, 1F, 1F, OverlayTweaksConfig.CONFIG.instance().containerTextureOpacity / 100F);
    }

    //#if MC >= 1.20.4
    @Inject(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", shift = At.Shift.AFTER))
    //#else
    //$$ @Inject(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", shift = At.Shift.AFTER))
    //#endif
    private void buttonOpacityEnd(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
    }

    //#if MC >= 1.20.4
    @Inject(method = "drawInvalidRecipeArrow", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
    //#else
    //$$ @Inject(method = "drawInvalidRecipeArrow", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"))
    //#endif
    private void invalidArrowOpacityStart(DrawContext context, int x, int y, CallbackInfo ci) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1F, 1F, 1F, OverlayTweaksConfig.CONFIG.instance().containerTextureOpacity / 100F);
    }

    //#if MC >= 1.20.4
    @Inject(method = "drawInvalidRecipeArrow", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", shift = At.Shift.AFTER))
    //#else
    //$$ @Inject(method = "drawInvalidRecipeArrow", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", shift = At.Shift.AFTER))
    //#endif
    private void invalidArrowOpacityEnd(DrawContext context, int x, int y, CallbackInfo ci) {
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
    }

}
