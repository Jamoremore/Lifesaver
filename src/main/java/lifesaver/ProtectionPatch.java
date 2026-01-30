package lifesaver;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ProtectionPatch {

    // 1. 计时器更新逻辑
    @SpirePatch(clz = AbstractDungeon.class, method = "update")
    public static class TimerUpdate {
        @SpirePrefixPatch
        public static void Prefix(AbstractDungeon __instance) {
            if (LifeSaverMod.isAskingForSL && LifeSaverMod.protectionTimer > 0.0f) {
                // 减去上一帧经过的时间
                LifeSaverMod.protectionTimer -= Gdx.graphics.getDeltaTime();

                // 视觉反馈（可选）：你可以在这里打日志，或者以后在界面上画个倒计时
                // System.out.println("Locked: " + LifeSaverMod.protectionTimer);
            }
        }
    }

    // 2. 拦截关闭屏幕逻辑 (防手滑核心)
    @SpirePatch(clz = AbstractDungeon.class, method = "closeCurrentScreen")
    public static class BlockClose {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix() {
            // 如果处于 SL 询问状态，且保护时间还没结束
            if (LifeSaverMod.isAskingForSL && LifeSaverMod.protectionTimer > 0.0f) {
                // 直接 Return，拦截掉原版关闭屏幕的逻辑
                // 这意味着按下 ESC 或点击“继续”按钮将没有任何反应
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}