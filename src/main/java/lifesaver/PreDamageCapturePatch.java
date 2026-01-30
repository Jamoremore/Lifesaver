package lifesaver;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "damage"
)
public class PreDamageCapturePatch {

    @SpirePrefixPatch
    public static void Prefix(AbstractPlayer __instance, DamageInfo info) {
        // 只有当玩家还活着的时候，才记录血量
        // 这样可以确保我们记录的是“这一击之前”的血量
        if (__instance.currentHealth > 0) {
            LifeSaverMod.lastKnownHP = __instance.currentHealth;
        }
    }
}