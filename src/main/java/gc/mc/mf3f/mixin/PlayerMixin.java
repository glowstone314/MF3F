package gc.mc.mf3f.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.EventHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Inject(
            method = "getDigSpeed",
            at = @At("HEAD"),
            cancellable = true
    )
    private void overrideGetDestroySpeed(BlockState state, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        Player player = (Player) (Object) this;

        float f = player.getInventory().getDestroySpeed(state);
        if (f > 1.0F) {
            f += (float)player.getAttributeValue(Attributes.MINING_EFFICIENCY);
        }

        if (MobEffectUtil.hasDigSpeed(player)) {
            f *= 1.0F + (float)(MobEffectUtil.getDigSpeedAmplification(player) + 1) * 0.2F;
        }

        if (player.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            int amplifier = player.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier();
            float scale = (float) Math.pow(0.3, amplifier + 1);
            f *= scale;
        }

        f *= (float)player.getAttributeValue(Attributes.BLOCK_BREAK_SPEED);
        if (player.isEyeInFluid(FluidTags.WATER)) {
            f *= (float)player.getAttribute(Attributes.SUBMERGED_MINING_SPEED).getValue();
        }

        if (!player.onGround()) {
            f /= 5.0F;
        }

        f = EventHooks.getBreakSpeed(player, state, f, pos);
        cir.setReturnValue(f);
    }
}