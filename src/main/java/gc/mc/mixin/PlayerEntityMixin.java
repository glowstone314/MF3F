package gc.mc.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(
            method = "getBlockBreakingSpeed",
            at = @At("RETURN"),
            cancellable = true
    )
    private void onGetDestroySpeed(BlockState state, CallbackInfoReturnable<Float> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        float speed = cir.getReturnValue();

        if (player.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
            int amplifier = player.getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier();

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
