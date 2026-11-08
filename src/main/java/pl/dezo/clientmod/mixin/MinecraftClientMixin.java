package pl.dezo.clientmod.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Inject(method = "getWindowTitle", at = @At("HEAD"), cancellable = true, require = 0)
    private void dezo$windowTitle(CallbackInfoReturnable<String> cir) {
        // Wersja z Fabric Loadera zamiast SharedConstants - API gry zmienia sie miedzy wersjami.
        String version = FabricLoader.getInstance().getModContainer("minecraft")
            .map(c -> c.getMetadata().getVersion().getFriendlyString())
            .orElse("");
        cir.setReturnValue(("Dezo Client " + version).trim());
    }
}
