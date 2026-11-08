package pl.dezo.clientmod.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.dezo.clientmod.TextUtil;

import java.util.ArrayList;
import java.util.List;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    @Unique
    private boolean dezo$laidOut = false;

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"), require = 0)
    private void dezo$resetLayout(CallbackInfo ci) {
        this.dezo$laidOut = false;
    }

    /** Ukrywa przyciski 20x20 (jezyk, ulatwienia dostepu) oraz widget z napisem Mojang. */
    @Inject(method = "init", at = @At("TAIL"), require = 0)
    private void dezo$hideSmallButtons(CallbackInfo ci) {
        for (var element : this.children()) {
            if (element instanceof ClickableWidget widget) {
                boolean small = widget.getWidth() == 20 && widget.getHeight() == 20;
                boolean mojang = widget.getMessage() != null && widget.getMessage().getString().contains("Mojang");
                if (small || mojang) {
                    widget.visible = false;
                    widget.active = false;
                }
            }
        }
    }

    /**
     * Uklad menu: 1. Singleplayer 2. Multiplayer 3. Mody 4. Ustawienia 5. Wyjdz z gry
     * Ustawienia i Wyjdz maja ta sama wielkosc co Singleplayer.
     * Wykonywane przy pierwszym renderze po init(), zeby inne mody (np. Mod Menu) zdazyly dodac swoje przyciski.
     */
    @Inject(method = "render", at = @At("HEAD"), require = 0)
    private void dezo$relayout(CallbackInfo ci) {
        if (this.dezo$laidOut) return;
        this.dezo$laidOut = true;

        ClickableWidget sp = null, mp = null, mods = null, opt = null, quit = null;
        for (var element : this.children()) {
            if (!(element instanceof ClickableWidget w)) continue;
            if (dezo$is(w, "menu.singleplayer")) sp = w;
            else if (dezo$is(w, "menu.multiplayer")) mp = w;
            else if (dezo$is(w, "modmenu.title")) mods = w;
            else if (dezo$is(w, "menu.options")) opt = w;
            else if (dezo$is(w, "menu.quit")) quit = w;
        }
        if (sp == null || opt == null || quit == null) return;

        int x = sp.getX();
        int y = sp.getY();
        int width = sp.getWidth();
        int step = (mp != null) ? Math.max(mp.getY() - sp.getY(), sp.getHeight()) : sp.getHeight() + 4;

        List<ClickableWidget> order = new ArrayList<>();
        order.add(sp);
        if (mp != null) order.add(mp);
        if (mods != null) order.add(mods);
        order.add(opt);
        order.add(quit);

        for (int i = 0; i < order.size(); i++) {
            ClickableWidget w = order.get(i);
            w.setWidth(width);
            w.setX(x);
            w.setY(y + i * step);
        }
    }

    @Unique
    private static boolean dezo$is(ClickableWidget w, String key) {
        Text t = w.getMessage();
        if (t == null) return false;
        if (t.getContent() instanceof TranslatableTextContent c && key.equals(c.getKey())) return true;
        return t.getString().equals(I18n.translate(key));
    }

    /** drawTextWithShadow(TextRenderer, String, ...) */
    @ModifyArg(
        method = "render",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)I"),
        index = 1,
        require = 0
    )
    private String dezo$rewriteString(String text) {
        return TextUtil.rewrite(text);
    }

    /** drawTextWithShadow(TextRenderer, Text, ...) */
    @ModifyArg(
        method = "render",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I"),
        index = 1,
        require = 0
    )
    private Text dezo$rewriteText(Text text) {
        return TextUtil.rewrite(text);
    }

    // Od 1.21.6 metody DrawContext zwracaja void zamiast int - te same cele z deskryptorem ...)V.

    /** drawTextWithShadow(TextRenderer, String, ...) - wariant void */
    @ModifyArg(
        method = "render",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"),
        index = 1,
        require = 0
    )
    private String dezo$rewriteStringVoid(String text) {
        return TextUtil.rewrite(text);
    }

    /** drawTextWithShadow(TextRenderer, Text, ...) - wariant void */
    @ModifyArg(
        method = "render",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V"),
        index = 1,
        require = 0
    )
    private Text dezo$rewriteTextVoid(Text text) {
        return TextUtil.rewrite(text);
    }
}
