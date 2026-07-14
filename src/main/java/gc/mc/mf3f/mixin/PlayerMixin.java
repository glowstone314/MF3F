package gc.mc.mf3f.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Inject(
            method = "getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)F",
            at = @At("HEAD"),
            cancellable = true
    )
    private void overrideGetDestroySpeed(BlockState state, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        Player player = (Player) (Object) this;

        float speed = player.getInventory().getSelectedItem().getDestroySpeed(state);
        if (speed > 1.0F) {
            speed += (float) player.getAttributeValue(Attributes.MINING_EFFICIENCY);
        }

        if (MobEffectUtil.hasDigSpeed(player)) {
            speed *= 1.0F + (float) (MobEffectUtil.getDigSpeedAmplification(player) + 1) * 0.2F;
        }

        if (player.hasEffect(MobEffects.MINING_FATIGUE)) {
            int amplifier = player.getEffect(MobEffects.MINING_FATIGUE).getAmplifier();
            float scale = (float) Math.pow(0.3, amplifier + 1);
            speed *= scale;
        }

        speed *= (float) player.getAttributeValue(Attributes.BLOCK_BREAK_SPEED);
        if (player.isEyeInFluid(FluidTags.WATER)) {
            speed *= (float) player.getAttribute(Attributes.SUBMERGED_MINING_SPEED).getValue();
        }

        if (!player.onGround()) {
            speed /= 5.0F;
        }

        speed = net.neoforged.neoforge.event.EventHooks.getBreakSpeed(player, state, speed, pos);
        cir.setReturnValue(speed);
    }
}