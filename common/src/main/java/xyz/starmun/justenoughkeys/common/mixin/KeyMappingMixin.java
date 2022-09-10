package xyz.starmun.justenoughkeys.common.mixin;

import com.google.common.base.Splitter;
import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
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
    public ModifierKeyMap jek$getDefaultModifierKeyMap(){
        return this.defaultModifierKeyMap;
    }

    @Override
    public void setDefaultModifierKeyMap(ModifierKeyMap modifierKey) {
        this.defaultModifierKeyMap = modifierKey;
    }

    @Unique
    private final ModifierKeyMap modifierKeyMap = new ModifierKeyMap();
    @Unique
    private ModifierKeyMap defaultModifierKeyMap = new ModifierKeyMap();
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
        cir.setReturnValue(Component.literal(getModifiersText().getString() + getKeyText().getString()));
    }

    private Component getKeyText() {
        Component displayText= Component.empty();
        if(this.jek$getModifierKeyMap().any()
                && !ModifierKey.isModifierKey(((IJEKKeyMappingExtensions) this).jek$getKey())){
            displayText = Component.literal("+");
        }
        if(!this.jek$getModifierKeyMap().any()
                || !ModifierKey.isModifierKey(((IJEKKeyMappingExtensions) this).jek$getKey())){
            displayText = Component.literal(displayText.getString() + ((IJEKKeyMappingExtensions) this).jek$getKey().getDisplayName().getString());
        }
        return displayText;
    }

    private Component getModifiersText() {
        final Component[] displayText = {Component.empty()};
        final Splitter NAME_SPLITTER = Splitter.on(' ');

        Integer[] keyIndexes = this.jek$getModifierKeyMap().keySet().toArray(new Integer[0]);
        for(int i = 0; i< keyIndexes.length; i++){
            Iterator<String> iterator = NAME_SPLITTER.split(ModifierKey.MODIFIER_KEYS.get(keyIndexes[i]).getDisplayName()).iterator();
            iterator.forEachRemaining(string-> displayText[0] = Component.literal(displayText[0].getString() + string.charAt(0)));
           if(i!=keyIndexes.length-1){
               displayText[0] = Component.literal(displayText[0].getString() + "+");
           }
        }
        return displayText[0];
    }

    @Inject(method = "matches", at=@At("HEAD"),cancellable = true)
    public void matches(int i, int j, CallbackInfoReturnable<Boolean> cir) {
        // when a modifier key is pressed on the keyboard
        // if no keymapping with the pressed modifiers is currently triggered
        // Let any matching keymapping, which has no modifier, pass through
        if((!modifierKeyMap.any()
                && !IJEKKeyMappingExtensions
                .getMatchingKeyMappingsWithModifiers(InputConstants.getKey(i,j)).isEmpty())
                || (modifierKeyMap.any() && !modifierKeyMap.isPressed())){
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

    @Inject(method = "isDefault", at=@At("TAIL"),cancellable = true)
    public void isDefault(CallbackInfoReturnable<Boolean> cir){
        ModifierKeyMap modifierKeyMap =((IJEKKeyMappingExtensions)this).jek$getModifierKeyMap();
        ModifierKeyMap defaultModifierKeyMap = ((IJEKKeyMappingExtensions)this).jek$getDefaultModifierKeyMap();
        if( modifierKeyMap.size() != defaultModifierKeyMap.size()
                || !modifierKeyMap.equals(defaultModifierKeyMap)){
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
