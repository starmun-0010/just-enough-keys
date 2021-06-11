package xyz.starmun.justenoughkeys.common.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;
import xyz.starmun.justenoughkeys.common.data.ModifierKeyMap;

@Mixin(KeyMapping.class)
public class KeyMappingMixin implements IJEKKeyMappingExtensions {

    @Shadow
    private InputConstants.Key key;
    @Inject(method = "getTranslatedKeyMessage", at=@At("TAIL"),cancellable = true)
    public void getName(CallbackInfoReturnable<Component> cir){
        StringBuilder builder = new StringBuilder();
        modifierKeyMap.forEach((id,modifierKey)-> builder.append(modifierKey.name));
        cir.setReturnValue(new TextComponent( builder.toString()).append(key.getDisplayName()));
    }
    @Unique
    private final ModifierKeyMap modifierKeyMap = new ModifierKeyMap();

    @Override
    public ModifierKeyMap jek$getModifierKeyMap() {
        return modifierKeyMap;
    }

    @Override
    public InputConstants.Key jek$getKey() {
        return key;
    }
}
