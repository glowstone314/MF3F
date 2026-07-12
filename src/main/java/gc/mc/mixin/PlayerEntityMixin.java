package gc.mc.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @ModifyConstant(
            method = "getBlockBreakingSpeed",
            constant = @Constant(floatValue = 0.0027F)
    )
    private float modifyMiningFatigueCase2(float original) {
        return 0.027F;
    }
}
