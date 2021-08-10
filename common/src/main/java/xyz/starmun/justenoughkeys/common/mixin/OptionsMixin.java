package xyz.starmun.justenoughkeys.common.mixin;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.starmun.justenoughkeys.common.JustEnoughKeys;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;
import xyz.starmun.justenoughkeys.common.data.ModifierKey;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

@Mixin(Options.class)
public class OptionsMixin {
    @Unique
    private File jekOptionsFile;

    @Final
    @Shadow
    public KeyMapping[] keyMappings;

    @Shadow
    @Final
    private static Splitter OPTION_SPLITTER;

    @Unique
    private static final Splitter VALUE_SPLITTER = Splitter.on(',');

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;load()V", shift = At.Shift.BEFORE))
    public void init(Minecraft minecraft, File file, CallbackInfo ci) {
        this.jekOptionsFile = new File(file.getPath(), "options." + JustEnoughKeys.MOD_ID + ".txt");
    }

    @Inject(method = "load", at = @At("TAIL"))
    public void load(CallbackInfo ci) {
        try {
            if (this.jekOptionsFile == null || !this.jekOptionsFile.exists()) {
                return;
            }
            BufferedReader bufferedReader = Files.newReader(this.jekOptionsFile, Charsets.UTF_8);
            try {
                bufferedReader.lines().forEach((line) -> {
                    try {
                        Iterator<String> lineIterator = OPTION_SPLITTER.split(line).iterator();
                        KeyMapping keyMapping = IJEKKeyMappingExtensions.ALL.get(lineIterator.next().replaceFirst("modifiers.", "").trim());
                        Iterator<String> valueIterator = VALUE_SPLITTER.split(lineIterator.next()).iterator();
                        valueIterator.forEachRemaining(keyValue -> {
                            if (keyValue.trim().length() >0 && ((IJEKKeyMappingExtensions) keyMapping).jek$getModifierKeyMap()
                                    .set(InputConstants.getKey(keyValue.trim())) == ModifierKey.UNKNOWN) {
                                JustEnoughKeys.LOGGER.error("Skipping option: {}, could not load.", line);
                            }
                        });
                    } catch (Exception ex) {
                        JustEnoughKeys.LOGGER.error("Skipping option: {}, could not load.", line);
                    }
                });
            } catch (Exception ex) {
                JustEnoughKeys.LOGGER.error("Could not load Just Enough Keys options.", ex);
            } finally {
                if (bufferedReader != null)
                    bufferedReader.close();
            }
        } catch (Exception ex) {
            JustEnoughKeys.LOGGER.error("Could not load Just Enough Keys options file", ex);
        }
        IJEKKeyMappingExtensions.resetMapping();
    }

    @Inject(method = "save", at = @At("TAIL"))
    public void save(CallbackInfo ci) {
        try {
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.jekOptionsFile), StandardCharsets.UTF_8));

            try {
                for (int i = 0; i < this.keyMappings.length; ++i) {
                    KeyMapping keyMapping = this.keyMappings[i];
                    StringBuilder builder = new StringBuilder();
                    ((IJEKKeyMappingExtensions) keyMapping).jek$getModifierKeyMap().values().forEach(modifierKey -> builder.append(modifierKey.getName() + ","));

                    printWriter.println("modifiers." + keyMapping.getName() + ":" + (builder.length() > 0 ? builder.substring(0, builder.length() - 1) : ""));
                }
            } catch (Exception ex) {
                JustEnoughKeys.LOGGER.error("Failed to save Just Enough Keys options file", ex);
            } finally {
                if (printWriter != null)
                    printWriter.close();
            }
        } catch (Exception ex) {
            JustEnoughKeys.LOGGER.error("Failed to save Just Enough Keys options file", ex);
        }
    }
}
