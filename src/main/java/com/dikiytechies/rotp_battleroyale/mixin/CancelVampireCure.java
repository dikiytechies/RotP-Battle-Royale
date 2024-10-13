package com.dikiytechies.rotp_battleroyale.mixin;

import com.github.standobyte.jojo.power.impl.nonstand.type.vampirism.VampirismUtil;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(VampirismUtil.class)
public class CancelVampireCure {
    /**
     * @author dikiytechies
     * @reason gameplay issue
     */
    @Overwrite(remap = false)
    public static void onEnchantedGoldenAppleEaten(LivingEntity entity) {
    }
}
