package xyz.starmun.justenoughkeys.common.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.main.GameConfig;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.starmun.justenoughkeys.common.client.JEKMouseHandler;

@Mixin(Minecraft.class)
public class MinecraftMixin extends ReentrantBlockableEventLoop<Runnable> {

    @Shadow
    public MouseHandler mouseHandler;

    public MinecraftMixin(String string) {
        super(string);
    }

    @Inject(method = "<init>", at=@At("RETURN"))
    public void initKeyBoard(GameConfig gameConfig, CallbackInfo ci){
        mouseHandler = new JEKMouseHandler((Minecraft)(ReentrantBlockableEventLoop)this);
        mouseHandler.setup(((Minecraft)(ReentrantBlockableEventLoop)this).getWindow().getWindow());
    }

    @Shadow
    protected Runnable wrapRunnable(Runnable runnable) {
        return null;
    }

    @Shadow
    protected boolean shouldRun(Runnable runnable) {
        return false;
    }

    @Shadow
    protected Thread getRunningThread() {
        return null;
    }
}
