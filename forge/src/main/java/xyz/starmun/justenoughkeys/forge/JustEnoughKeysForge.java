package xyz.starmun.justenoughkeys.forge;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import xyz.starmun.justenoughkeys.common.JustEnoughKeys;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;
import xyz.starmun.justenoughkeys.common.data.ModifierKey;

@Mod(JustEnoughKeys.MOD_ID)
public class JustEnoughKeysForge {
    public JustEnoughKeysForge() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        MinecraftForge.EVENT_BUS.addListener(this::onGuiKeyPressedPre);
        JustEnoughKeys.init();
    }
    public void onGuiKeyPressedPre(GuiScreenEvent.KeyboardKeyPressedEvent.Pre event) {
        InputConstants.Key key = InputConstants.getKey(event.getKeyCode(), event.getScanCode());
        if (ModifierKey.isModifierKey(key)) {
            IJEKKeyMappingExtensions.set(key, true);
        }
    }
}
