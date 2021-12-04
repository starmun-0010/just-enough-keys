package xyz.starmun.justenoughkeys.common.mixin;

import com.google.common.base.Splitter;
import com.mojang.blaze3d.platform.InputConstants;
import me.shedaniel.architectury.annotations.PlatformOnly;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.starmun.justenoughkeys.common.JustEnoughKeys;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;
import xyz.starmun.justenoughkeys.common.data.ModifierKey;
import xyz.starmun.justenoughkeys.common.data.ModifierKeyMap;

import java.util.Iterator;


@Mixin(value = KeyMapping.class,priority = 999)
public class KeyMappingMixin  implements  Comparable<KeyMapping>, IJEKKeyMappingExtensions {

    @Shadow
    private InputConstants.Key key;
    @Shadow @Final private String name;

    @Shadow
    private int clickCount;

    @Override
    public InputConstants.Key jek$getKey() {
        return key;
    }

    @Override
    public void jek$setClickCount(int i) {
       this.clickCount = i;
    }

    @Override
    public int jek$getClickCount() {
       return clickCount;
    }
    @Override
    public ModifierKey getPlatformDefaultModifierKey(){
        return this.platformDefaultModifierKey;
    }

    @Override
    public void setPlatformDefaultModifierKey(ModifierKey modifierKey) {
        this.platformDefaultModifierKey = modifierKey;
    }

    @Unique
    private ModifierKeyMap modifierKeyMap = new ModifierKeyMap();

    @Unique
    private ModifierKey platformDefaultModifierKey = ModifierKey.UNKNOWN;
    @SuppressWarnings("ConstantConditions")
    @Inject(method = "<init>(Ljava/lang/String;Lcom/mojang/blaze3d/platform/InputConstants$Type;ILjava/lang/String;)V", at=@At("TAIL"))
    public void fillMap(String string, InputConstants.Type type, int i, String string2, CallbackInfo ci){
        IJEKKeyMappingExtensions.ALL.put(this.name,(KeyMapping)(Comparable<KeyMapping>)this);
        IJEKKeyMappingExtensions.initMAP((KeyMapping)(Comparable<KeyMapping>)this);
    }
    @Inject(method = "click", at=@At("HEAD"), cancellable = true)
    private static void click(InputConstants.Key key, CallbackInfo ci) {
        IJEKKeyMappingExtensions.click(key);
        ci.cancel();
    }

    @Inject(method = "set", at=@At("HEAD"), cancellable = true)
    private static void set(InputConstants.Key key, boolean pressed, CallbackInfo ci) {
        IJEKKeyMappingExtensions.set(key, pressed);
        ci.cancel();
    }
    @Inject(method = "getTranslatedKeyMessage", at=@At("HEAD"),cancellable = true)
    public void getTranslatedKeyMessage(CallbackInfoReturnable<Component> cir) {
        TextComponent displayText = new TextComponent("");
        final Splitter NAME_SPLITTER = Splitter.on(' ');

        this.jek$getModifierKeyMap().forEach((id, modifierKey) ->{
            Iterator<String> iterator = NAME_SPLITTER.split(InputConstants.getKey(modifierKey.getName()).getDisplayName().getString()).iterator();
            iterator.forEachRemaining(string-> displayText.append(string.substring(0,1)));
            displayText.append(new TextComponent("+"));
        });
        displayText.append(((IJEKKeyMappingExtensions) this).jek$getKey().getDisplayName());
        cir.setReturnValue(displayText);
    }
    @Inject(method = "matches", at=@At("HEAD"),cancellable = true)
    public void matches(int i, int j, CallbackInfoReturnable<Boolean> cir) {
        if (modifierKeyMap.any() && !modifierKeyMap.isPressed()){
                cir.setReturnValue(false);
        }
    }

    @Inject(method = "matchesMouse", at=@At("HEAD"),cancellable = true)
    public void matchesMouse(int i, CallbackInfoReturnable<Boolean> cir) {
        if (modifierKeyMap.any() && !modifierKeyMap.isPressed()){
                cir.setReturnValue(false);
        }
    }
    @PlatformOnly(PlatformOnly.FABRIC)
    @Inject(method = "same", at=@At("TAIL"), cancellable = true)
    public void same(KeyMapping keyMapping, CallbackInfoReturnable<Boolean> cir){
       if(!((IJEKKeyMappingExtensions)keyMapping).jek$getModifierKeyMap().equals(modifierKeyMap)){
          cir.setReturnValue(false);
       }
    }

    @PlatformOnly(PlatformOnly.FABRIC)
    @Inject(method = "isDefault", at=@At("TAIL"),cancellable = true)
    public void isDefault(CallbackInfoReturnable<Boolean> cir){
       if(modifierKeyMap.any()){
           cir.setReturnValue(false);
       }
    }

    @Inject(method = "setAll", at = @At("HEAD"), cancellable = true)
    private static void setAll(CallbackInfo ci){
        IJEKKeyMappingExtensions.setAll();
        ci.cancel();
    }
    @Inject(method = "releaseAll", at=@At("HEAD"), cancellable = true)
    private static void releaseAll(CallbackInfo ci){
       IJEKKeyMappingExtensions.releaseAll();
       ci.cancel();
    }
    @Override
    public ModifierKeyMap jek$getModifierKeyMap() {
        return modifierKeyMap;
    }

    @Shadow
     public int compareTo(@NotNull KeyMapping o) {
        return 0;
    }
}
