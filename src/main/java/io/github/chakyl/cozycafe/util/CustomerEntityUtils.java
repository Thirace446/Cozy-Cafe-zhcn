package io.github.chakyl.cozycafe.util;

import io.github.chakyl.cozycafe.entities.CustomerEntity;
import io.netty.util.internal.StringUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class CustomerEntityUtils {
    public static void spawnCustomerAndTarget(BlockPos pos, ServerLevel serverLevel, String skinUsername, BlockPos targetPos, CustomerEntity customer, CustomerTarget target) {
        if (!StringUtil.isNullOrEmpty(skinUsername)) {
            customer.setCustomerSkin(skinUsername);
        }
        finalizeSpawn(pos, serverLevel, targetPos, customer, target);
    }

    private static void finalizeSpawn(BlockPos pos, ServerLevel serverLevel, BlockPos targetPos, CustomerEntity customer, CustomerTarget target) {
        customer.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, serverLevel.random.nextFloat() * 360.0F, 0.0F);

        if (target == CustomerTarget.MENU) {
            customer.setTargetMenuPos(targetPos);
        } else if (target == CustomerTarget.SIGN) {
            customer.setTargetSignPos(targetPos);
        }

        serverLevel.addFreshEntity(customer);
    }
}
