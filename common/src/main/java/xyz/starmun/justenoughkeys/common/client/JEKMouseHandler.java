package xyz.starmun.justenoughkeys.common.client;

import me.shedaniel.architectury.platform.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.lwjgl.glfw.GLFW;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;

public class JEKMouseHandler extends MouseHandler {
    private final Minecraft minecraft;

    public JEKMouseHandler(Minecraft minecraft) {
        super(minecraft);
        this.minecraft = minecraft;
    }
    @Override
    public void grabMouse() {
        super.grabMouse();
        if (Platform.isDevelopmentEnvironment()) {
            GLFW.glfwSetInputMode(this.minecraft.getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }
    }
}
