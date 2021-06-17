package xyz.starmun.justenoughkeys.forge.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xyz.starmun.justenoughkeys.common.JustEnoughKeys;
import xyz.starmun.justenoughkeys.forge.client.JEKForgeKeyMapping;

@Mixin(ClientRegistry.class)
public class ClientRegistryMixin {
    @ModifyVariable(method = "registerKeyBinding", at = @At("HEAD"), remap = false)
    private static KeyMapping registerJEKKeyMapping(KeyMapping keyMapping) {
        if (!(keyMapping instanceof JEKForgeKeyMapping)) {
            JustEnoughKeys.LOGGER.info("Replaced with jek binding" + keyMapping.getName());
            return new JEKForgeKeyMapping(keyMapping.getName(), keyMapping.getKeyConflictContext(), keyMapping.getKeyModifier(), keyMapping.getKey(), keyMapping.getCategory());
        }
        return keyMapping;
    }

}
