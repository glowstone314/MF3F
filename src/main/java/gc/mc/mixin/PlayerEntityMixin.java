package gc.mc.mixin;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerEntityMixin {
    @Inject(
            method = "getDestroySpeed",
            at = @At("RETURN"),
            cancellable = true
    )
    private void onGetDestroySpeed(BlockState state, CallbackInfoReturnable<Float> cir) {
        Player player = (Player) (Object) this;

        float speed = cir.getReturnValue();

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

        cir.setReturnValue(speed);
    }
}