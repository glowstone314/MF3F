package gc.mc.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(
            method = "getBlockBreakingSpeed",
            at = @At("HEAD"),
            cancellable = true
    )
    private void overrideGetDestroySpeed(BlockState state, CallbackInfoReturnable<Float> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        float speed = player.getInventory().getSelectedStack().getMiningSpeedMultiplier(state);
        if (speed > 1.0F) {
            speed += (float) player.getAttributeValue(EntityAttributes.MINING_EFFICIENCY);
        }

        if (StatusEffectUtil.hasHaste(player)) {
            speed *= 1.0F + (float)(StatusEffectUtil.getHasteAmplifier(player) + 1) * 0.2F;
        }

        if (player.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
            int amplifier = player.getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier();
            float scale = (float) Math.pow(0.3, amplifier + 1);
            speed *= scale;
        }

        speed *= (float)player.getAttributeValue(EntityAttributes.BLOCK_BREAK_SPEED);
        if (player.isSubmergedIn(FluidTags.WATER)) {
            speed *= (float)player.getAttributeInstance(EntityAttributes.SUBMERGED_MINING_SPEED).getValue();
        }

        if (!player.isOnGround()) {
            speed /= 5.0F;
        }

        cir.setReturnValue(speed);
    }
}
