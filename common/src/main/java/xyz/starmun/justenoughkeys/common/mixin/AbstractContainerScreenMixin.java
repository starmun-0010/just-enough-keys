package xyz.starmun.justenoughkeys.common.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.starmun.justenoughkeys.common.client.JEKControls;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {

    @Shadow @Nullable protected Slot hoveredSlot;

    @Shadow protected abstract void slotClicked(Slot slot, int i, int j, ClickType clickType);

    @Inject(method = "keyPressed", at = @At("TAIL"), cancellable = true)
    public void keyPressed(int i, int j, int k, CallbackInfoReturnable<Boolean> cir){
        if(this.hoveredSlot != null && this.hoveredSlot.hasItem()){
            if(Minecraft.getInstance().options.keyDrop.matches(i,j)) {// 580
                this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, 0, ClickType.THROW);// 581
                cir.setReturnValue(true);
            }
            else if(JEKControls.dropStack.matches(i,j)){
                this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, 1 , ClickType.THROW);// 581
                cir.setReturnValue(true);
            }
        }
    }

    //Disable vanilla drop hovered item behaviour, in inventory
    @SuppressWarnings("rawtypes")
    @Redirect( method = "keyPressed", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ClickType;)V", ordinal = 1))
    public void hasControlDown(AbstractContainerScreen instance, Slot slot, int i, int j, ClickType clickType){
    }
 }
