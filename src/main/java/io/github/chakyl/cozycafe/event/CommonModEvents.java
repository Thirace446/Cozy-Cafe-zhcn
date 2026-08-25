package io.github.chakyl.cozycafe.event;

import io.github.chakyl.cozycafe.CozyCafe;
import io.github.chakyl.cozycafe.CozyRegistry;
import io.github.chakyl.cozycafe.entities.CustomerEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = CozyCafe.MODID)
public class CommonModEvents {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> CozyCafe.QUALITY_FOOD_INSTALLED = ModList.get().isLoaded("quality_food"));
        event.enqueueWork(() -> CozyCafe.KUBEJS_INSTALLED = ModList.get().isLoaded("kubejs"));
        event.enqueueWork(() -> CozyCafe.NUMISMATICS_INSTALLED = ModList.get().isLoaded("numismatics"));
        event.enqueueWork(() -> CozyCafe.NUMISMATICS_UTILS_INSTALLED = ModList.get().isLoaded("numismaticsutils"));
        event.enqueueWork(() -> CozyCafe.EMI_INSTALLED = ModList.get().isLoaded("emi"));

    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(CozyRegistry.EntityRegistry.CUSTOMER.get(), CustomerEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.FOLLOW_RANGE, 32.0D).build());
    }
}