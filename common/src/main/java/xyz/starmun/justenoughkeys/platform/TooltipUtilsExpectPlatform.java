package xyz.starmun.justenoughkeys.platform;

import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.architectury.annotations.ExpectPlatform;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.BaseComponent;
import org.apache.commons.lang3.NotImplementedException;

public class TooltipUtilsExpectPlatform {

    @ExpectPlatform
    public static void renderTooltip(BaseComponent component, PoseStack poseStack,  int mouseX, int mouseY, int screenWidth, int screenHeight,  Font font){
        throw new NotImplementedException("Method not implemented.");
    }
}
