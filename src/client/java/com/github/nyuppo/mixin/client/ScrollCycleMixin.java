package com.github.nyuppo.mixin.client;

import com.github.nyuppo.HotbarCycleClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class ScrollCycleMixin {
    @Shadow
    public int selectedSlot;

    // Coba dengan method name yang berbeda untuk NeoForge/Connector
    @Inject(
            method = "scrollInHotbar",
            at = @At("HEAD"),
            cancellable = true,
            require = 0  // PENTING: Jangan crash jika method tidak ditemukan
    )
    private void hotbarcycleScrollInHotbar(double scrollAmount, CallbackInfo ci) {
        final HotbarCycleClient.Direction direction = Math.signum(scrollAmount) < 0
                ? HotbarCycleClient.Direction.UP
                : HotbarCycleClient.Direction.DOWN;

        if (HotbarCycleClient.getConfig().getHoldAndScroll() && HotbarCycleClient.getCycleKeyBinding().isPressed()) {
            HotbarCycleClient.shiftRows(MinecraftClient.getInstance(), direction);
            ci.cancel();
        } else if (HotbarCycleClient.getConfig().getHoldAndScroll() && HotbarCycleClient.getSingleCycleKeyBinding().isPressed()) {
            HotbarCycleClient.shiftSingle(MinecraftClient.getInstance(), this.selectedSlot, direction);
            ci.cancel();
        }
    }
}