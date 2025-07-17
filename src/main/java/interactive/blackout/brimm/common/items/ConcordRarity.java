package interactive.blackout.brimm.common.items;

import net.minecraft.util.text.TextFormatting;


public enum ConcordRarity {

    COMMON(TextFormatting.GREEN),
    UNCOMMON(TextFormatting.GOLD),
    RARE(TextFormatting.LIGHT_PURPLE),
    EPIC(TextFormatting.RED);

    public final TextFormatting color;

    ConcordRarity(TextFormatting color) {
        this.color = color;
    }


}