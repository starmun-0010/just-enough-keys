package xyz.starmun.justenoughkeys.common.contracts;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

import java.util.Map;

public interface IJEKKeyMappingExtensions {

    InputConstants.Key jek$getKey();
    void jek$setClickCount(int i);
    int jek$getClickCount();
}
