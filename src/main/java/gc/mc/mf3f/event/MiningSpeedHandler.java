package gc.mc.mf3f.event;

import gc.mc.mf3f.MF3F;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MF3F.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MiningSpeedHandler {

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        PlayerEntity player = event.getEntityPlayer();
        float speed = event.getOriginalSpeed();

        if (player.isPotionActive(Effects.MINING_FATIGUE)) {
            int amplifier = player.getActivePotionEffect(Effects.MINING_FATIGUE).getAmplifier();
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