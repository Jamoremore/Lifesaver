package lifesaver;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import basemod.BaseMod;
import basemod.interfaces.StartGameSubscriber;
import basemod.interfaces.PostInitializeSubscriber;

@SpireInitializer
public class LifeSaverMod implements PostInitializeSubscriber, StartGameSubscriber {

    public static boolean isAskingForSL = false;
    public static boolean shouldInterceptDeath = true;
    public static boolean isSafeToQuit = false;
    public static int lastKnownHP = 1;

    // 【新增】防误触保护计时器
    public static float protectionTimer = 0.0f;

    public LifeSaverMod() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new LifeSaverMod();
    }

    @Override
    public void receivePostInitialize() {
    }

    @Override
    public void receiveStartGame() {
        shouldInterceptDeath = true;
        isAskingForSL = false;
        isSafeToQuit = false;
        lastKnownHP = 1;
        protectionTimer = 0.0f; // 重置计时器
    }
}