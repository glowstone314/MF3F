package gc.mc.mf3f.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Player.class)
public class PlayerMixin {
    @ModifyConstant(
            method = "getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)F",
            constant = @Constant(floatValue = 0.0027F)
    )
    private float modifyMiningFatigueCase2(float original) {
        return 0.027F;
    }
}
