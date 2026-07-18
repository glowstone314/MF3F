package gc.mc.mf3f.event;

import gc.mc.mf3f.MF3F;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MF3F.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MiningSpeedHandler {

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getPlayer();
        float speed = event.getOriginalSpeed();

        if (player.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            int amplifier = player.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier();
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
