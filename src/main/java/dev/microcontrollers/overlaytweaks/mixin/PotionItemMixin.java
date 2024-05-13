package dev.microcontrollers.overlaytweaks.mixin;

import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
//#if MC >= 1.20.6
import net.minecraft.component.DataComponentTypes;
//#endif
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
//#if MC <= 1.20.4
//$$ import net.minecraft.potion.PotionUtil;
//#endif
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PotionItem.class)
public class PotionItemMixin extends Item {
    public PotionItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        //#if MC >= 1.20.6
        if (OverlayTweaksConfig.CONFIG.instance().potionGlint) return stack.get(DataComponentTypes.POTION_CONTENTS).hasEffects();
        else return false;
        //#else
        //$$ return OverlayTweaksConfig.CONFIG.instance().potionGlint && !PotionUtil.getPotionEffects(stack).isEmpty();
        //#endif
    }

}
