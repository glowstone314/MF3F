package gc.mc.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
        
        float speed = player.getInventory().getBlockBreakingSpeed(state);
        if (speed > 1.0F) {
            int i = EnchantmentHelper.getEfficiency(player);
            ItemStack itemStack = player.getMainHandStack();
            if (i > 0 && !itemStack.isEmpty()) {
                speed += (float)(i * i + 1);
            }
        }

        if (StatusEffectUtil.hasHaste(player)) {
            speed *= 1.0F + (float)(StatusEffectUtil.getHasteAmplifier(player) + 1) * 0.2F;
        }

        if (player.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
            int amplifier = player.getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier();
            float scale = (float) Math.pow(0.3, amplifier + 1);
            speed *= scale;
        }

        if (player.isSubmergedIn(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(player)) {
            speed /= 5.0F;
        }

        if (!player.isOnGround()) {
            speed /= 5.0F;
        }

        cir.setReturnValue(speed);
    }
}
