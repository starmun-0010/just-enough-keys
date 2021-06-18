package xyz.starmun.justenoughkeys.forge.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.extensions.IForgeKeybinding;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;

@Mixin(KeyMapping.class)
public class ForgeKeyMappingMixin implements Comparable<KeyMapping>, IForgeKeybinding {
    @Shadow
    @Final
    private String name;

    @Inject(method = "<init>(Ljava/lang/String;Lnet/minecraftforge/client/settings/IKeyConflictContext;Lnet/minecraftforge/client/settings/KeyModifier;Lcom/mojang/blaze3d/platform/InputConstants$Key;Ljava/lang/String;)V", at = @At("INVOKE"))
    public void fillMap(String description, IKeyConflictContext keyConflictContext, KeyModifier keyModifier, InputConstants.Key keyCode, String category, CallbackInfo ci) {
        IJEKKeyMappingExtensions.ALL.put(this.name, (KeyMapping) (Comparable<KeyMapping>) this);
        IJEKKeyMappingExtensions.initMAP((KeyMapping) (Comparable<KeyMapping>) this);
    }

    @Override
    public boolean isActiveAndMatches(InputConstants.Key keyCode) {

        return keyCode != InputConstants.UNKNOWN && keyCode.equals(getKey()) && getKeyConflictContext().isActive() && ((IJEKKeyMappingExtensions) this).getModifierKeyMap().isPressed();
    }

    @Shadow
    public int compareTo(@NotNull KeyMapping o) {
        return 0;
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
