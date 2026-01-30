package lifesaver;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(
        clz = AbstractDungeon.class,
        method = "update"
)
public class DeathEnforcerPatch {

    @SpirePrefixPatch
    public static void Prefix(AbstractDungeon __instance) {
        if (LifeSaverMod.isAskingForSL) {

            // 1. 如果有免死金牌，什么都别管，让原版游戏自己去处理退出流程
            if (LifeSaverMod.isSafeToQuit) {
                return;
            }

            // 2. 检测：屏幕关闭且没有免死金牌
            // 只有当保护时间结束(protectionTimer<=0)，closeCurrentScreen 才会成功执行
            // 所以这里能检测到 screen != SETTINGS，说明 3 秒已过且玩家主动关闭了界面
            if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.SETTINGS) {

                LifeSaverMod.isAskingForSL = false;
                LifeSaverMod.shouldInterceptDeath = false;

                AbstractDungeon.player.damage(new DamageInfo(
                        null, 9999, DamageInfo.DamageType.HP_LOSS
                ));
            }
        }
    }
}