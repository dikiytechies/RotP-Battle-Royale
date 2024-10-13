package com.dikiytechies.rotp_battleroyale.init.power.non_stand;

import com.dikiytechies.rotp_battleroyale.AddonMain;
import com.dikiytechies.rotp_battleroyale.action.non_stand.VampirismCreateInjection;
import com.github.standobyte.jojo.action.non_stand.NonStandAction;
import com.github.standobyte.jojo.action.non_stand.VampirismAction;
import com.github.standobyte.jojo.init.power.non_stand.vampirism.ModVampirismActions;
import com.github.standobyte.jojo.power.impl.nonstand.type.NonStandPowerType;
import com.github.standobyte.jojo.power.impl.nonstand.type.vampirism.VampirismPowerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Supplier;

import static com.github.standobyte.jojo.init.power.ModCommonRegisters.ACTIONS;

public class VampirismActions {
    public static void loadRegistryObjects() {}

    public static final Supplier<VampirismPowerType> VAMPIRISM = ModVampirismActions.VAMPIRISM;
    public static final RegistryObject<VampirismAction> CREATE_INJECTION = ACTIONS.register("vampirism_create_injection",
            () -> new VampirismCreateInjection(new NonStandAction.Builder())
    );

    @Mod.EventBusSubscriber(modid = AddonMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void afterNonPowerRegister(@Nonnull final RegistryEvent.Register<NonStandPowerType<?>> event) {
            NonStandPowerType<?> vamp = VAMPIRISM.get();
            if (vamp != null) {
                try {
                    VampirismAction[] rightClickHotbar = (VampirismAction[]) VAMP_TYPE_RMB_HOTBAR.get(vamp);
                    VampirismAction[] edited = new VampirismAction[rightClickHotbar.length + 1];
                    edited[0] = rightClickHotbar[0];
                    System.arraycopy(rightClickHotbar, 1, edited, 1, rightClickHotbar.length - 1);
                    edited[rightClickHotbar.length] = CREATE_INJECTION.get();
                    VAMP_TYPE_RMB_HOTBAR.set(vamp, edited);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                    System.out.println("[VAMP] EHHH FAIL");
                    throw new RuntimeException(e);
                }
            }
        }

        private static final Field VAMP_TYPE_RMB_HOTBAR = getField(NonStandPowerType.class, "abilities");

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
