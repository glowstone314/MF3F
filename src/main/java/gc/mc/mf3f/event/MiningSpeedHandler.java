package gc.mc.mf3f.event;

import gc.mc.mf3f.MF3F;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod(value = MF3F.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = MF3F.MODID, value = Dist.CLIENT)
public class MiningSpeedHandler {

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        float speed = event.getOriginalSpeed();

        if (player.hasEffect(MobEffects.MINING_FATIGUE)) {
            int amplifier = player.getEffect(MobEffects.MINING_FATIGUE).getAmplifier();
            if (amplifier == 2) {
                speed /= 0.0027F;
                speed *= 0.027F;
            } else if (amplifier >= 3) {
                speed /= 8.1E-4F;
                float scale = (float) Math.pow(0.3, amplifier + 1);
                speed *= scale;
            }
        }

        event.setNewSpeed(speed);
    }
}