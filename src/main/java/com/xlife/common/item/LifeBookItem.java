package com.xlife.common.item;

import com.xlife.common.data.LivesData;
import com.xlife.common.data.PlayerInformationData;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.TextFormatting;

import java.util.TreeMap;

import static net.minecraft.util.text.TextFormatting.*;

/**
 * Used to add a players' life history to a book.
 */
public class LifeBookItem {
    private final TreeMap<Integer, String> map = new TreeMap<>();

    private final TextFormatting[] formatting = new TextFormatting[]{RED, BLUE, DARK_GREEN, GOLD, LIGHT_PURPLE, DARK_PURPLE, YELLOW, AQUA, GREEN, BLACK};

    /**
     * Add the pages when the book is opened.
     */
    public StringNBT addPageEntry(PlayerInformationData info, int page) {
        return new StringNBT(header(page(info, page)));
    }

    private String header(String entries) {
        return "{\"extra\":[{\"bold\":true,\"text\":\"Life History\"}" + entries + "],\"text\":\"\"}";
    }

    private String page(PlayerInformationData info, int page) {

        for (LivesData data : info.getLives().values()) {
            if (!data.getCauseOfDeath().isEmpty()) {
                map.put(data.getHearts(), lasted(info, data.getHearts()));
            } else {
                map.put(info.getCurrentLife().getHearts(), living(info));
            }
        }

        if (page == 1) {
            return map.subMap(1, 4).values().toString().replace("[", "").replace(", ", "").replace("]", "");
        } else if (page == 2) {
            return map.subMap(4, 7).values().toString().replace("[", "").replace(", ", "").replace("]", "");
        } else if (page == 3) {
            return map.subMap(7, 11).values().toString().replace("[", "").replace(", ", "").replace("]", "");
        } else if (page == 4) {
            return map.get(10);
        } else {
            return RED + "Error";
        }
    }

    private String living(PlayerInformationData info) {
        LivesData data = info.getCurrentLife();
        return ",{\"color\":\"reset\",\"text\":\"\\n\\n\"}" + hearts(data.getHearts() * 2) + ",{\"color\":\"reset\",\"text\":\"\\n\"},{\"color\":\"black\",\"text\":\"Living " + data.getTime() + "\"}";
    }

    private String lasted(PlayerInformationData info, int life) {
        LivesData data = info.getLife(life);
        return ",{\"color\":\"reset\",\"text\":\"\\n\\n\"}" + hearts(data.getHearts() * 2) + ",{\"color\":\"reset\",\"text\":\"\\n\"},{\"color\":\"black\",\"text\":\"Lasted " + data.getTime() + "\\nEnded by \"},{\"color\":\"dark_red\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"" + data.getDeathMessage() + "\"}},\"text\":\"" + data.getCauseOfDeath() + "\"}";
    }

    private String hearts(int amount) {
        StringBuilder builder = new StringBuilder();
        int hearts = Math.round(amount);

        while (hearts > 0) {
            builder.append(new Character((char)10084));
            hearts -= 2;
        }
        return ",{\"text\":\"" + formatting[(amount / 2) - 1] + builder + "\"}";
    }

}