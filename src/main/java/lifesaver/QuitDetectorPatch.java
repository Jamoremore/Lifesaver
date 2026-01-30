package lifesaver;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.screens.options.OptionsPanel;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import basemod.ReflectionHacks;

@SpirePatch(
        clz = OptionsPanel.class,
        method = "update"
)
public class QuitDetectorPatch {

    @SpirePostfixPatch
    public static void Postfix(OptionsPanel __instance) {
        if (LifeSaverMod.isAskingForSL) {

            Object exitBtnObj = ReflectionHacks.getPrivate(__instance, OptionsPanel.class, "exitBtn");
            Object abandonBtnObj = ReflectionHacks.getPrivate(__instance, OptionsPanel.class, "abandonBtn");

            Hitbox exitHb = null;
            Hitbox abandonHb = null;

            if (exitBtnObj != null) {
                exitHb = ReflectionHacks.getPrivate(exitBtnObj, exitBtnObj.getClass(), "hb");
            }
            if (abandonBtnObj != null) {
                abandonHb = ReflectionHacks.getPrivate(abandonBtnObj, abandonBtnObj.getClass(), "hb");
            }

            boolean clickedExit = (exitHb != null && exitHb.hovered && InputHelper.justClickedLeft);
            boolean clickedAbandon = (abandonHb != null && abandonHb.hovered && InputHelper.justClickedLeft);

            if (clickedExit || clickedAbandon) {
                // 【核心修改】只发放免死金牌
                LifeSaverMod.isSafeToQuit = true;

                // 不要在这里关闭拦截器状态！
                // 保持 isAskingForSL = true，这样 DeathEnforcer 依然会监控
                // 但因为有免死金牌，DeathEnforcer 不会杀人
                // 这样就把“如何退出、如何淡出画面”的控制权完全交还给了原版游戏
            }
        }
    }
}