package xyz.starmun.justenoughkeys.common.mixin;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
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
    private static File jekOptionsFile;

    @Final
    @Shadow
    public KeyMapping[] keyMappings;

    @Shadow
    @Final
    private static Splitter OPTION_SPLITTER;

    @Unique
    private static final Splitter VALUE_SPLITTER = Splitter.on(',');

    @ModifyVariable(method = "<init>", at = @At(value = "HEAD" ), argsOnly = true)
    private static File init(File file) {
        jekOptionsFile = new File(file.getPath(), "options." + JustEnoughKeys.MOD_ID + ".txt");
        return file;
    }

    @Inject(method = "load", at = @At("TAIL"))
    public void load(CallbackInfo ci) {
        try {
            if (jekOptionsFile == null || !jekOptionsFile.exists()) {
                return;
            }
            //noinspection UnstableApiUsage
            try (BufferedReader bufferedReader = Files.newReader(jekOptionsFile, Charsets.UTF_8)) {
                bufferedReader.lines().forEach((line) -> {
                    try {
                        Iterator<String> lineIterator = OPTION_SPLITTER.split(line).iterator();
                        KeyMapping keyMapping = IJEKKeyMappingExtensions.ALL.get(lineIterator.next().replaceFirst("modifiers.", "").trim());
                        Iterator<String> valueIterator = VALUE_SPLITTER.split(lineIterator.next()).iterator();
                        valueIterator.forEachRemaining(keyValue -> {
                            if (keyValue.trim().length() > 0 && ((IJEKKeyMappingExtensions) keyMapping).jek$getModifierKeyMap()
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
            }
        } catch (Exception ex) {
            JustEnoughKeys.LOGGER.error("Could not load Just Enough Keys options file", ex);
        }
        IJEKKeyMappingExtensions.resetMapping();
    }

    @Inject(method = "save", at = @At("TAIL"))
    public void save(CallbackInfo ci) {
        try {

            try (PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(jekOptionsFile), StandardCharsets.UTF_8))) {
                for (KeyMapping keyMapping : this.keyMappings) {
                    StringBuilder builder = new StringBuilder();
                    ((IJEKKeyMappingExtensions) keyMapping).jek$getModifierKeyMap().values().forEach(modifierKey -> builder.append(modifierKey.getName()).append(","));
                    printWriter.println("modifiers." + keyMapping.getName() + ":" + (builder.length() > 0 ? builder.substring(0, builder.length() - 1) : ""));
                }
            } catch (Exception ex) {
                JustEnoughKeys.LOGGER.error("Failed to save Just Enough Keys options file", ex);
            }
        } catch (Exception ex) {
            JustEnoughKeys.LOGGER.error("Failed to save Just Enough Keys options file", ex);
        }
    }
}
