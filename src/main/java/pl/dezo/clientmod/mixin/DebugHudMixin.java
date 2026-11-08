package pl.dezo.clientmod.mixin;

import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * UWAGA: od 1.21.9 ekran F3 zostal przebudowany (osobne wpisy debug), wiec getLeftText moze
 * w 1.21.11 nie istniec - wtedy mixin jest po cichu pomijany (require = 0), bez crasha.
 */
@Mixin(DebugHud.class)
public abstract class DebugHudMixin {

    @Inject(method = "getLeftText", at = @At("RETURN"), require = 0)
    private void dezo$replaceMinecraft(CallbackInfoReturnable<List<String>> cir) {
        List<String> lines = cir.getReturnValue();
        if (lines == null) return;
        try {
            lines.replaceAll(line -> line == null ? null : line.replace("Minecraft", "Dezo Client"));
        } catch (UnsupportedOperationException e) {
            cir.setReturnValue(lines.stream()
                .map(line -> line == null ? null : line.replace("Minecraft", "Dezo Client"))
                .collect(java.util.stream.Collectors.toList()));
        }
    }
}
