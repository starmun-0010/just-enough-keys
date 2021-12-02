package xyz.starmun.justenoughkeys.forge;

import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import xyz.starmun.justenoughkeys.common.JustEnoughKeys;
import net.minecraftforge.fml.common.Mod;
import xyz.starmun.justenoughkeys.forge.compat.SophisticatedBackpacksCompat;

@Mod(JustEnoughKeys.MOD_ID)
public class JustEnoughKeysForge {
    public JustEnoughKeysForge() {
        SophisticatedBackpacksCompat.init();
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "", (a, b) -> true));
        JustEnoughKeys.init();
    }
}
