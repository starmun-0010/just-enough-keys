package xyz.starmun.justenoughkeys.platform.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.BaseComponent;

import net.minecraftforge.fmlclient.gui.GuiUtils;

import java.util.Collections;

public class TooltipUtilsExpectPlatformImpl {
    public static void renderTooltip(BaseComponent component, PoseStack poseStack,  int mouseX, int mouseY, int screenWidth, int screenHeight,  Font font){
        GuiUtils.drawHoveringText(poseStack, Collections.singletonList(component), mouseX, mouseY
                , screenWidth, screenHeight
                , 0, font);
    }
}
