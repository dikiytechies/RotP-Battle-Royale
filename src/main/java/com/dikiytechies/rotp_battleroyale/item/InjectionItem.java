package com.dikiytechies.rotp_battleroyale.item;

import com.dikiytechies.rotp_battleroyale.capability.HamonUtilCap;
import com.dikiytechies.rotp_battleroyale.capability.HamonUtilProvider;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.HamonData;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

public class InjectionItem extends Item {
    private final InjectionType injectionType;
    public InjectionItem(Properties properties, InjectionType injectionType) {
        super(properties);
        this.injectionType = injectionType;
    }

    public static enum InjectionType {
        RESOLVE,
        HAMON
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        switch (injectionType) {
            case RESOLVE:
                IStandPower power = IStandPower.getPlayerStandPower(player);
                if (power.getType() != null) {
                    if (power.getResolveLevel() < 4) {
                        power.setResolveLevel(power.getResolveLevel() + 1);
                    } else {
                        player.addEffect(new EffectInstance(ModStatusEffects.RESOLVE.get(), 1800, 4, false, false, true));
                    }
                    return ActionResult.success(stack);
                }
                break;
            case HAMON:
                INonStandPower nonStandPower = INonStandPower.getPlayerNonStandPower(player);
                if (nonStandPower.getType() == ModPowers.HAMON.get()) {
                    HamonData hamon = nonStandPower.getTypeSpecificData(ModPowers.HAMON.get()).get();
                    hamon.setBreathingLevel(hamon.getBreathingLevel() + 20);
                    LazyOptional<HamonUtilCap> livingDataOptional = player.getCapability(HamonUtilProvider.CAPABILITY);
                    livingDataOptional.ifPresent(livingData -> {
                        float points = livingData.getPointsMultiplier() == 0.0f? 1.0f: livingData.getPointsMultiplier();
                        livingData.setPointsMultiplier(points + 1.0f);
                    });
                    return ActionResult.success(stack);
                }
                break;
        }
        return ActionResult.fail(stack);
    }
}
