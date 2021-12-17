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
import java.util.HashMap;
import java.util.Map;

@Mixin(KeyMapping.class)
public abstract class ForgeKeyMappingMixin implements Comparable<KeyMapping>, IForgeKeyMapping, IJEKKeyMappingExtensions {

    @Shadow private InputConstants.Key key;
    @Shadow
    @Final
    private String name;

    @Inject(method = "<init>(Ljava/lang/String;Lnet/minecraftforge/client/settings/IKeyConflictContext;Lnet/minecraftforge/client/settings/KeyModifier;Lcom/mojang/blaze3d/platform/InputConstants$Key;Ljava/lang/String;)V", at = @At("TAIL"))
    public void fillMap(String description, IKeyConflictContext keyConflictContext, KeyModifier keyModifier, InputConstants.Key keyCode, String category, CallbackInfo ci) {
        this.setDefaultModifierKeyMap(getModifierKeyMapFromForgeKeyModifier(keyModifier));
        this.jek$getModifierKeyMap().set(getModifierKeyMapFromForgeKeyModifier(keyModifier));

    }

    @Override
    public boolean isActiveAndMatches(InputConstants.Key keyCode) {
        return keyCode != InputConstants.UNKNOWN
                && keyCode.equals(getKey())
                && getKeyConflictContext().isActive()
                && this.jek$getModifierKeyMap().isPressed();
    }

    @Inject(method = "same", at=@At("HEAD"), cancellable = true)
    public void same(KeyMapping keyMapping, CallbackInfoReturnable<Boolean> cir){
        if((this.getKeyConflictContext().conflicts(keyMapping.getKeyConflictContext())||keyMapping.getKeyConflictContext().conflicts(this.getKeyConflictContext()))
                && ((IJEKKeyMappingExtensions)keyMapping).jek$getModifierKeyMap().equals(this.jek$getModifierKeyMap())
                && this.key.equals(keyMapping.getKey())) {
            cir.setReturnValue(true);
            return;
        }
        cir.setReturnValue(false);
    }
    @Shadow
    public int compareTo(@NotNull KeyMapping o) {
        return 0;
    }


    private final Map<KeyModifier,ModifierKey> forgeKeyModifierToJEKKEYModifierLookupTable = new HashMap<KeyModifier,ModifierKey>() {{
        put(KeyModifier.ALT, ModifierKey.KEYBOARD_LEFT_ALT);
        put(KeyModifier.SHIFT, ModifierKey.KEYBOARD_LEFT_SHIFT);
        put(KeyModifier.CONTROL, ModifierKey.KEYBOARD_LEFT_CONTROL);
        put(KeyModifier.NONE, ModifierKey.UNKNOWN);
    }};
    @Unique
    private ModifierKeyMap getModifierKeyMapFromForgeKeyModifier(KeyModifier keyModifier){
       return new ModifierKeyMap(){{
           set(forgeKeyModifierToJEKKEYModifierLookupTable.get(keyModifier),true);
       }};
    }
}
