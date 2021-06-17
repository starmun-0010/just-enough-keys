package xyz.starmun.justenoughkeys.common.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import me.shedaniel.architectury.annotations.ExpectPlatform;
import me.shedaniel.architectury.annotations.PlatformOnly;
import me.shedaniel.architectury.platform.Platform;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;

import java.util.Map;


@Mixin(KeyMapping.class)
public class KeyMappingMixin implements IJEKKeyMappingExtensions {

    @Shadow
    private InputConstants.Key key;

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

}
