package xyz.starmun.justenoughkeys.forge.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.extensions.IForgeKeyMapping;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
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

@Mixin(KeyMapping.class)
public abstract class ForgeKeyMappingMixin implements Comparable<KeyMapping>, IForgeKeyMapping {

    @Shadow private InputConstants.Key key;
    @Shadow
    @Final
    private String name;

    @Inject(method = "<init>(Ljava/lang/String;Lnet/minecraftforge/client/settings/IKeyConflictContext;Lnet/minecraftforge/client/settings/KeyModifier;Lcom/mojang/blaze3d/platform/InputConstants$Key;Ljava/lang/String;)V", at = @At("TAIL"))
    public void fillMap(String description, IKeyConflictContext keyConflictContext, KeyModifier keyModifier, InputConstants.Key keyCode, String category, CallbackInfo ci) {
        ((IJEKKeyMappingExtensions)this).setPlatformDefaultModifierKey(getModifierKeyFromKeyModifier(keyModifier));
        ((IJEKKeyMappingExtensions)this).jek$getModifierKeyMap().set(getModifierKeyFromKeyModifier(keyModifier), true);
        IJEKKeyMappingExtensions.ALL.put(this.name, (KeyMapping) (Comparable<KeyMapping>) this);
        IJEKKeyMappingExtensions.initMAP((KeyMapping) (Comparable<KeyMapping>) this);
    }

    @Override
    public boolean isActiveAndMatches(InputConstants.Key keyCode) {
        return keyCode != InputConstants.UNKNOWN && keyCode.equals(getKey()) && getKeyConflictContext().isActive() && ((IJEKKeyMappingExtensions) this).jek$getModifierKeyMap().isPressed();
    }

    @Inject(method = "same", at=@At("HEAD"), cancellable = true)
    public void same(KeyMapping keyMapping, CallbackInfoReturnable<Boolean> cir){
        if(this.getKeyConflictContext().conflicts(keyMapping.getKeyConflictContext())
                && ((IJEKKeyMappingExtensions)keyMapping).jek$getModifierKeyMap().equals(((IJEKKeyMappingExtensions) this).jek$getModifierKeyMap())
                && this.key.equals(keyMapping.getKey())){
            cir.setReturnValue(true);
            return;
        }
        cir.setReturnValue(false);
    }
    @Shadow
    public int compareTo(@NotNull KeyMapping o) {
        return 0;
    }

    @Inject(method = "isDefault", at=@At("HEAD"),cancellable = true)
    public void isDefault(CallbackInfoReturnable<Boolean> cir){
        ModifierKeyMap keyMap =((IJEKKeyMappingExtensions)this).jek$getModifierKeyMap();
        KeyModifier defaultKeyModifier = ((KeyMapping)(Comparable<KeyMapping>)this).getDefaultKeyModifier();
        if(( keyMap.size() > 1
                ||defaultKeyModifier ==  KeyModifier.NONE && keyMap.any())
                ||(keyMap.size() == 1 && !keyMap.containsKey(getModifierKeyFromKeyModifier(defaultKeyModifier).id))
                ||defaultKeyModifier != KeyModifier.NONE && !keyMap.any()){
            cir.setReturnValue(false);
        }
    }

    @Unique
    private ModifierKey getModifierKeyFromKeyModifier(KeyModifier keyModifier){
        if(keyModifier == KeyModifier.ALT){
            return ModifierKey.KEYBOARD_LEFT_ALT;
        }
        else if(keyModifier == KeyModifier.CONTROL)
        {
            return ModifierKey.KEYBOARD_LEFT_CONTROL;
        }
        else if(keyModifier == KeyModifier.SHIFT){
            return ModifierKey.KEYBOARD_LEFT_SHIFT;
        }
        return ModifierKey.UNKNOWN;
    }
    @Shadow
    public InputConstants.Key getKey() {
        return null;
    }


    @Shadow
    public void setKeyConflictContext(IKeyConflictContext iKeyConflictContext) {

    }

    @Shadow
    public IKeyConflictContext getKeyConflictContext() {
        return null;
    }

    @Shadow
    public KeyModifier getKeyModifierDefault() {
        return null;
    }

    @Shadow
    public KeyModifier getKeyModifier() {
        return null;
    }

    @Shadow
    public void setKeyModifierAndCode(KeyModifier keyModifier, InputConstants.Key arg) {
    }
}
