package xyz.starmun.justenoughkeys.common.contracts;

import com.mojang.blaze3d.platform.InputConstants;
import xyz.starmun.justenoughkeys.common.data.ModifierKeyMap;

public interface IJEKKeyMappingExtensions {
    ModifierKeyMap jek$getModifierKeyMap();
    InputConstants.Key jek$getKey();
}
