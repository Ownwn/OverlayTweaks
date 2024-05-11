package dev.microcontrollers.overlaytweaks.mixin;

import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.particle.ElderGuardianAppearanceParticle;
import net.minecraft.client.render.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ElderGuardianAppearanceParticle.class)
public class ElderGuardianAppearanceParticleMixin {
    @ModifyArg(method = "buildGeometry", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/Model;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"), index = 7)
    private float changeElderGuardianOpacity(float g) {
        return g * OverlayTweaksConfig.CONFIG.instance().elderGuardianOpacity / 100F / 0.55F;
    }

    // just cancel everything if it's 0 anyways
    @Inject(method = "buildGeometry", at = @At("HEAD"), cancellable = true)
    private void removeElderGuardianJumpscare(VertexConsumer vertexConsumer, Camera camera, float tickDelta, CallbackInfo ci) {
        if (OverlayTweaksConfig.CONFIG.instance().elderGuardianOpacity == 0F) ci.cancel();
    }

    // TODO: make it render in front of world always
    @ModifyArgs(method = "buildGeometry", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V"))
    private void changeElderGuardianScale(Args args) {
        args.set(0, -OverlayTweaksConfig.CONFIG.instance().elderGuardianScale);
        args.set(1, -OverlayTweaksConfig.CONFIG.instance().elderGuardianScale);
        args.set(2, OverlayTweaksConfig.CONFIG.instance().elderGuardianScale);
    }

}
