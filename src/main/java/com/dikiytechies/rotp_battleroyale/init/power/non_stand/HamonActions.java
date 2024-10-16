package com.dikiytechies.rotp_battleroyale.init.power.non_stand;

import com.dikiytechies.rotp_battleroyale.AddonMain;
import com.dikiytechies.rotp_battleroyale.action.non_stand.HamonCreateInjection;
import com.github.standobyte.jojo.action.non_stand.HamonAction;
import com.github.standobyte.jojo.init.power.non_stand.hamon.ModHamonActions;
import com.github.standobyte.jojo.power.impl.nonstand.type.NonStandPowerType;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.HamonPowerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.function.Supplier;

import static com.github.standobyte.jojo.init.power.ModCommonRegisters.ACTIONS;

public class HamonActions {
    public static void loadRegistryObjects() {}

    public static final Supplier<HamonPowerType> HAMON = ModHamonActions.HAMON;
    public static final RegistryObject<HamonAction> CREATE_INJECTION = ACTIONS.register("hamon_create_injection",
            () -> new HamonCreateInjection(new HamonCreateInjection.Builder().energyCost(600F))
    );

    @Mod.EventBusSubscriber(modid = AddonMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void afterNonPowerRegister(@Nonnull final RegistryEvent.Register<NonStandPowerType<?>> event) {
            NonStandPowerType<?> hamon = HAMON.get();
            if (hamon != null) {
                try {
                    HamonAction[] rightClickHotbar = (HamonAction[]) HAMON_TYPE_RMB_HOTBAR.get(hamon);
                    HamonAction[] edited = new HamonAction[rightClickHotbar.length + 1];
                    edited[0] = rightClickHotbar[0];
                    System.arraycopy(rightClickHotbar, 1, edited, 1, rightClickHotbar.length - 1);
                    edited[rightClickHotbar.length] = CREATE_INJECTION.get();
                    HAMON_TYPE_RMB_HOTBAR.set(hamon, edited);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                    System.out.println("[STEAMED HAM] EHHH FAIL");
                    throw new RuntimeException(e);
                }
            }
        }

        private static final Field HAMON_TYPE_RMB_HOTBAR = getField(NonStandPowerType.class, "abilities");

        private static Field getField(Class<?> clazz, String fieldName) {
            try {
                Field f = clazz.getDeclaredField(fieldName);
                f.setAccessible(true);
                return f;
            }
            catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
