package gc.mc.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Player.class)
public class PlayerEntityMixin {
    @ModifyConstant(
            method = "getDestroySpeed",
            constant = @Constant(floatValue = 0.0027F)
    )
    private float modifyMiningFatigueCase2(float original) {
        return 0.027F;
    }
}
