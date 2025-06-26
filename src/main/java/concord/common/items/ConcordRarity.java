package concord.common.items;


import net.minecraft.ChatFormatting;

public enum ConcordRarity {

    COMMON(ChatFormatting.GREEN),
    UNCOMMON(ChatFormatting.GOLD),
    RARE(ChatFormatting.LIGHT_PURPLE),
    EPIC(ChatFormatting.RED);

    public final ChatFormatting color;

    ConcordRarity(ChatFormatting color) {
        this.color = color;
    }

}
