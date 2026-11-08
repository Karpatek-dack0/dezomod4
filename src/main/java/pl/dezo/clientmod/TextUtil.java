package pl.dezo.clientmod;

import net.minecraft.text.Text;

public final class TextUtil {
    private TextUtil() {}

    /** Usuwa teksty z "Mojang", zamienia "Minecraft" na "Dezo Client". */
    public static String rewrite(String s) {
        if (s == null) return null;
        if (s.contains("Mojang")) return "";
        return s.replace("Minecraft", "Dezo Client");
    }

    public static Text rewrite(Text t) {
        if (t == null) return null;
        String original = t.getString();
        String changed = rewrite(original);
        if (original.equals(changed)) return t;
        return Text.literal(changed).setStyle(t.getStyle());
    }
}
