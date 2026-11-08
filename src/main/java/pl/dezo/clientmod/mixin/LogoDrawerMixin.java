package pl.dezo.clientmod.mixin;

import net.minecraft.client.gui.LogoDrawer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LogoDrawer.class)
public abstract class LogoDrawerMixin {

    // Bez deskryptora: obejmuje wszystkie przeciazenia draw(...) w danej wersji gry.
    @Inject(method = "draw", at = @At("HEAD"), cancellable = true, require = 0)
    private void dezo$cancelLogo(CallbackInfo ci) {
        ci.cancel();
    }
}
