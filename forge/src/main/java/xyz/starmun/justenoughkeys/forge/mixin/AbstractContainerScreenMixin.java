package xyz.starmun.justenoughkeys.forge.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {

    @Shadow @Nullable protected Slot hoveredSlot;

    @Shadow protected abstract void slotClicked(Slot slot, int i, int j, ClickType clickType);

    @Inject(method = "keyPressed", at = @At("TAIL"), cancellable = true)
    public void keyPressed(int i, int j, int k, CallbackInfoReturnable<Boolean> cir){
        if(this.hoveredSlot != null && this.hoveredSlot.hasItem()
                &&Minecraft.getInstance().options.keyDrop.matches(i, j)) {// 580
            this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, Screen.hasControlDown() ? 1 : 0, ClickType.THROW);// 581
            cir.setReturnValue(true);
        }
    }
 }
