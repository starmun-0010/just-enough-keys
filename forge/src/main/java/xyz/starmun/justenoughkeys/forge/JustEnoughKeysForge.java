package xyz.starmun.justenoughkeys.forge;

import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import xyz.starmun.justenoughkeys.common.JustEnoughKeys;
import net.minecraftforge.fml.common.Mod;

@Mod(JustEnoughKeys.MOD_ID)
public class JustEnoughKeysForge {
    public JustEnoughKeysForge() {
        ClientRegistry.registerKeyBinding(JustEnoughKeys.dropStack);
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "", (a, b) -> true));
        JustEnoughKeys.init();
    }
}
