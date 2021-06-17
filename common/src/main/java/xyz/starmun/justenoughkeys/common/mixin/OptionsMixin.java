package xyz.starmun.justenoughkeys.common.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.starmun.justenoughkeys.common.client.JEKKeyMapping;
import xyz.starmun.justenoughkeys.common.client.JEKToggleKeyMapping;

import java.io.File;

@Mixin(Options.class)
public abstract class OptionsMixin {

    @Shadow
    public
    boolean toggleCrouch;
    @Shadow
    public
    boolean toggleSprint;
    @Shadow
    public KeyMapping[] keyMappings;
    
    @Shadow
    public KeyMapping keyAttack;

    @Shadow
    public KeyMapping keyUse;

    @Shadow
    public KeyMapping keyUp;

    @Shadow
    public KeyMapping keyLeft;

    @Shadow
    public KeyMapping keyDown;

    @Shadow
    public KeyMapping keyRight;

    @Shadow
    public KeyMapping keyJump;

    @Shadow
    public KeyMapping keyShift;

    @Shadow
    public KeyMapping keySprint;

    @Shadow
    public KeyMapping keyDrop;

    @Shadow
    public KeyMapping keyInventory;

    @Shadow
    public KeyMapping keyChat;

    @Shadow
    public KeyMapping keyPlayerList;

    @Shadow
    public KeyMapping keyPickItem;

    @Shadow
    public KeyMapping keyCommand;

    @Shadow
    public KeyMapping keySocialInteractions;

    @Shadow
    public KeyMapping keyScreenshot;

    @Shadow
    public KeyMapping keyTogglePerspective;

    @Shadow
    public KeyMapping keySmoothCamera;

    @Shadow
    public KeyMapping keyFullscreen;

    @Shadow
    public KeyMapping keySpectatorOutlines;

    @Shadow
    public KeyMapping keySwapOffhand;

    @Shadow
    public KeyMapping keySaveHotbarActivator;

    @Shadow
    public KeyMapping keyLoadHotbarActivator;

    @Shadow
    public KeyMapping keyAdvancements;

    @Shadow
    public KeyMapping[] keyHotbarSlots;


    @Shadow
    public void load(){}

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(Minecraft minecraft, File file, CallbackInfo ci) {
        this.keyUp = new JEKKeyMapping("key.forward", 87, "key.categories.movement");
        this.keyLeft = new JEKKeyMapping("key.left", 65, "key.categories.movement");
        this.keyDown = new JEKKeyMapping("key.back", 83, "key.categories.movement");
        this.keyRight = new JEKKeyMapping("key.right", 68, "key.categories.movement");
        this.keyJump = new JEKKeyMapping("key.jump", 32, "key.categories.movement");
        this.keyShift = new JEKToggleKeyMapping("key.sneak", 340, "key.categories.movement", () -> {
            return this.toggleCrouch;
        });
        this.keySprint = new JEKToggleKeyMapping("key.sprint", 341, "key.categories.movement", () -> {
            return this.toggleSprint;
        });
        this.keyInventory = new JEKKeyMapping("key.inventory", 69, "key.categories.inventory");
        this.keySwapOffhand = new JEKKeyMapping("key.swapOffhand", 70, "key.categories.inventory");
        this.keyDrop = new JEKKeyMapping("key.drop", 81, "key.categories.inventory");
        this.keyUse = new JEKKeyMapping("key.use", InputConstants.Type.MOUSE, 1, "key.categories.gameplay");
        this.keyAttack = new JEKKeyMapping("key.attack", InputConstants.Type.MOUSE, 0, "key.categories.gameplay");
        this.keyPickItem = new JEKKeyMapping("key.pickItem", InputConstants.Type.MOUSE, 2, "key.categories.gameplay");
        this.keyChat = new JEKKeyMapping("key.chat", 84, "key.categories.multiplayer");
        this.keyPlayerList = new JEKKeyMapping("key.playerlist", 258, "key.categories.multiplayer");
        this.keyCommand = new JEKKeyMapping("key.command", 47, "key.categories.multiplayer");
        this.keySocialInteractions = new JEKKeyMapping("key.socialInteractions", 80, "key.categories.multiplayer");
        this.keyScreenshot = new JEKKeyMapping("key.screenshot", 291, "key.categories.misc");
        this.keyTogglePerspective = new JEKKeyMapping("key.togglePerspective", 294, "key.categories.misc");
        this.keySmoothCamera = new JEKKeyMapping("key.smoothCamera", InputConstants.UNKNOWN.getValue(), "key.categories.misc");
        this.keyFullscreen = new JEKKeyMapping("key.fullscreen", 300, "key.categories.misc");
        this.keySpectatorOutlines = new JEKKeyMapping("key.spectatorOutlines", InputConstants.UNKNOWN.getValue(), "key.categories.misc");
        this.keyAdvancements = new JEKKeyMapping("key.advancements", 76, "key.categories.misc");
        this.keyHotbarSlots = new JEKKeyMapping[]{new JEKKeyMapping("key.hotbar.1", 49, "key.categories.inventory"), new JEKKeyMapping("key.hotbar.2", 50, "key.categories.inventory"), new JEKKeyMapping("key.hotbar.3", 51, "key.categories.inventory"), new JEKKeyMapping("key.hotbar.4", 52, "key.categories.inventory"), new JEKKeyMapping("key.hotbar.5", 53, "key.categories.inventory"), new JEKKeyMapping("key.hotbar.6", 54, "key.categories.inventory"), new JEKKeyMapping("key.hotbar.7", 55, "key.categories.inventory"), new JEKKeyMapping("key.hotbar.8", 56, "key.categories.inventory"), new JEKKeyMapping("key.hotbar.9", 57, "key.categories.inventory")};
        this.keySaveHotbarActivator = new JEKKeyMapping("key.saveToolbarActivator", 67, "key.categories.creative");
        this.keyLoadHotbarActivator = new JEKKeyMapping("key.loadToolbarActivator", 88, "key.categories.creative");
        this.keyMappings = ArrayUtils.addAll(new KeyMapping[]{this.keyAttack, this.keyUse, this.keyUp, this.keyLeft, this.keyDown, this.keyRight, this.keyJump, this.keyShift, this.keySprint, this.keyDrop, this.keyInventory, this.keyChat, this.keyPlayerList, this.keyPickItem, this.keyCommand, this.keySocialInteractions, this.keyScreenshot, this.keyTogglePerspective, this.keySmoothCamera, this.keyFullscreen, this.keySpectatorOutlines, this.keySwapOffhand, this.keySaveHotbarActivator, this.keyLoadHotbarActivator, this.keyAdvancements}, this.keyHotbarSlots);
        this.load();
    }
}
