package com.github.nyuppo.mixin.client;

import com.github.nyuppo.HotbarCycleClient;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public class ScrollCycleMixin {
    // @Inject(method = "scrollInHotbar(D)V", at = @At("HEAD"), cancellable = true)
    private void hotbarcycleScrollInHotbar(double scrollAmount, CallbackInfo ci) {
        final HotbarCycleClient.Direction direction = Math.signum(scrollAmount) < 0
                ? HotbarCycleClient.Direction.UP
                : HotbarCycleClient.Direction.DOWN;
        if (HotbarCycleClient.getConfig().getHoldAndScroll() && HotbarCycleClient.getCycleKeyBinding().isDown()) {
            HotbarCycleClient.shiftRows(Minecraft.getInstance(), direction);
            ci.cancel();
        } else if (HotbarCycleClient.getConfig().getHoldAndScroll() && HotbarCycleClient.getSingleCycleKeyBinding().isDown()) {
            HotbarCycleClient.shiftSingle(Minecraft.getInstance(), ((Inventory)(Object)this).getSelectedSlot(), direction);
            ci.cancel();
        }
    }
}
