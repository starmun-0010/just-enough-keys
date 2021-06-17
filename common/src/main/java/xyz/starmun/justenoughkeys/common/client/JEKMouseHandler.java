package xyz.starmun.justenoughkeys.common.client;

import com.mojang.blaze3d.platform.InputConstants;
import me.shedaniel.architectury.platform.Platform;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMapping;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class JEKMouseHandler extends MouseHandler {
    private final Minecraft minecraft;

    public JEKMouseHandler(Minecraft minecraft) {
        super(minecraft);
        this.minecraft = minecraft;
    }

    GLFWMouseButtonCallback originalGlfwMouseButtonCallback;


    @Override
    public void setup(long l) {
        super.setup(l);

        originalGlfwMouseButtonCallback = GLFW.glfwSetMouseButtonCallback(l, (lx, i, j, k) -> {
            this.jekOnPress(lx, i, j, k);
        });
    }

    private void jekOnPress(long l, int i, int j, int k) {

        this.minecraft.execute(() -> {
            if (originalGlfwMouseButtonCallback != null) {
                originalGlfwMouseButtonCallback.invoke(l, i, j, k);
            }
            if (l == this.minecraft.getWindow().getWindow() && this.minecraft.overlay == null && this.minecraft.screen == null) {
                IJEKKeyMapping.set(InputConstants.Type.MOUSE.getOrCreate(i), j == 1);
                if (j == 1 && !(this.minecraft.player.isSpectator() && i == 2)) {
                    KeyMapping.click(InputConstants.Type.MOUSE.getOrCreate(i));
                }
            }
        });
    }

    @Override
    public void grabMouse() {
        super.grabMouse();
        if (this.minecraft.isWindowActive()) {
            if (!Minecraft.ON_OSX) {
                IJEKKeyMapping.setAll();
            }
        }
        if (Platform.isDevelopmentEnvironment()) {
            GLFW.glfwSetInputMode(this.minecraft.getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }
    }


}
