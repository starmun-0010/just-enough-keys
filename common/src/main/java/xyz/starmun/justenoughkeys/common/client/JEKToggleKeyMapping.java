package xyz.starmun.justenoughkeys.common.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;

import java.util.function.BooleanSupplier;

public class JEKToggleKeyMapping extends JEKKeyMapping {
    private final BooleanSupplier needsToggle;

    public JEKToggleKeyMapping(String string, int i, String string2, BooleanSupplier booleanSupplier) {
        super(string, InputConstants.Type.KEYSYM, i, string2);
        this.needsToggle = booleanSupplier;
    }

    public void setDown(boolean bl) {
        if (this.needsToggle.getAsBoolean()) {
            if (bl) {
                super.setDown(!this.isDown());
            }
        } else {
            super.setDown(bl);
        }
    }
}
