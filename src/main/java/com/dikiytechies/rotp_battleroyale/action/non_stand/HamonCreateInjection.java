package com.dikiytechies.rotp_battleroyale.action.non_stand;

import com.dikiytechies.rotp_battleroyale.init.InitItems;
import com.dikiytechies.rotp_battleroyale.init.power.non_stand.HamonActions;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.non_stand.HamonAction;
import com.github.standobyte.jojo.action.non_stand.NonStandAction;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class HamonCreateInjection extends HamonAction {

    public HamonCreateInjection(HamonCreateInjection.Builder builder) { super(builder); }

    @Override
    protected ActionConditionResult checkHeldItems(LivingEntity user, INonStandPower power) {
        if ((user.getItemInHand(Hand.OFF_HAND).getItem() == Items.SKELETON_SKULL.getItem() && user.getMainHandItem().getItem() == InitItems.EMPTY_INJECTION.get()) ||
                (user.getMainHandItem().getItem() == Items.SKELETON_SKULL.getItem() && user.getOffhandItem().getItem() == InitItems.EMPTY_INJECTION.get())) {
            return ActionConditionResult.POSITIVE;
        }
        return ActionConditionResult.NEGATIVE;
    }

    @Override
    protected void perform(World world, LivingEntity user, INonStandPower power, ActionTarget target) {
        if (!user.level.isClientSide()) {
            if (user instanceof PlayerEntity) {
                PlayerEntity playerEntity = (PlayerEntity) user;
                ItemStack stack = null;
                ItemStack offstack = null;
                Hand hand = null;
                if (user.getMainHandItem().getItem() == Items.SKELETON_SKULL.getItem() && user.getOffhandItem().getItem() == InitItems.EMPTY_INJECTION.get()) {
                    stack = user.getMainHandItem();
                    offstack = user.getOffhandItem();
                    hand = Hand.MAIN_HAND;
                } else if (user.getOffhandItem().getItem() == Items.SKELETON_SKULL.getItem() && user.getMainHandItem().getItem() == InitItems.EMPTY_INJECTION.get()) {
                    stack = user.getOffhandItem();
                    offstack = user.getMainHandItem();
                    hand = Hand.OFF_HAND;
                }
                if (((hand == Hand.MAIN_HAND && stack.getCount() == 1) || (hand == Hand.OFF_HAND && offstack.getCount() == 1)) && !playerEntity.isCreative()) {
                    playerEntity.setItemInHand(Hand.MAIN_HAND, new ItemStack(InitItems.HAMON_INJECTION.get()));
                    if (Hand.OFF_HAND == hand) {
                        stack.shrink(1);
                    } else offstack.shrink(1);
                } else {
                    if (hand != null && !playerEntity.isCreative()) {
                        stack.shrink(1);
                        offstack.shrink(1);
                    }
                    playerEntity.addItem(new ItemStack(InitItems.HAMON_INJECTION.get()));
                }
            }
        }
    }
}
