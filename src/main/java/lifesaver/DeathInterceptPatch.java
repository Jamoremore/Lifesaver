package lifesaver;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "damage"
)
public class DeathInterceptPatch {

    @SpireInsertPatch(locator = Locator.class)
    public static SpireReturn<Void> Insert(AbstractPlayer __instance, DamageInfo info) {

        // 1. 预判逻辑 (保持不变)
        boolean hasBloom = __instance.hasRelic("Mark of the Bloom");
        boolean hasFairyPotion = false;
        for (AbstractPotion p : __instance.potions) {
            if ("FairyPotion".equals(p.ID)) { hasFairyPotion = true; break; }
        }
        boolean hasLizardTail = false;
        AbstractRelic tail = __instance.getRelic("Lizard Tail");
        if (tail != null && tail.counter == -1) { hasLizardTail = true; }
        boolean willReallyDie = hasBloom || (!hasFairyPotion && !hasLizardTail);

        // 2. 拦截并打开设置
        if (LifeSaverMod.shouldInterceptDeath
                && __instance.currentHealth <= 0
                && !LifeSaverMod.isAskingForSL
                && willReallyDie) {

            LifeSaverMod.isAskingForSL = true;

            // 设定 3 秒保护时间
            LifeSaverMod.protectionTimer = 3.0f;

            // 血量回溯视觉效果
            if (LifeSaverMod.lastKnownHP > 0) {
                __instance.currentHealth = LifeSaverMod.lastKnownHP;
            } else {
                __instance.currentHealth = 1;
            }
            __instance.healthBarUpdatedEvent();

            // 打开设置界面
            AbstractDungeon.settingsScreen.open();

            return SpireReturn.Return();
        }
        return SpireReturn.Continue();
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}