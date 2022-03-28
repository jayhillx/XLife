package com.xlife.common.util;

import com.xlife.common.capability.ILifeCapability;
import com.xlife.common.capability.LifeCapability;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.StringNBT;

/**
 * Adds the players life information to a book.
 *
 * THIS IS ONLY TEMPORARY!!
 * I am working on changing this code in the future :)
 */
public class LifeBookUtils {

    /** These pages are set when the players hearts increase. */
    public static StringNBT setPages(PlayerEntity player, int pages) {
        return StringNBT.valueOf(title(getPages(player, pages)));
    }

    /** Gets the pages written in the book. */
    private static String getPages(PlayerEntity player, int pages) {
        ILifeCapability stats = player.getCapability(LifeCapability.X_LIFE_CAPABILITIES).orElse(null);

        String[] time = stats.getTimeLasted();
        String[] cause = stats.getCause();
        String[] message = stats.getMessage();

        if (pages == 1) {
            if (stats.getMaxHealth() == 2.0F) {
                return (living(player, 1));
            } else if (stats.getMaxHealth() == 4.0F) {
                return lasted(1, time[0], message[0], cause[0]) + "," + living(player, 2);
            } else if (stats.getMaxHealth() == 6.0F) {
                return lasted(1, time[0], message[0], cause[0]) + "," + lasted(2, time[1], message[1], cause[1]) + "," + living(player, 3);
            } else {
                return lasted(1, time[0], message[0], cause[0]) + "," + lasted(2, time[1], message[1], cause[1]) + "," + lasted(3, time[2], message[2], cause[2]);
            }
        } else if (pages == 2) {
            if (stats.getMaxHealth() == 8.0F) {
                return (living(player, 4));
            } else if (stats.getMaxHealth() == 10.0F) {
                return lasted(4, time[3], message[3], cause[3]) + "," + living(player, 5);
            } else if (stats.getMaxHealth() == 12.0F) {
                return lasted(4, time[3], message[3], cause[3]) + "," + lasted(5, time[4], message[4], cause[4]) + "," + living(player, 6);
            } else {
                return lasted(4, time[3], message[3], cause[3]) + "," + lasted(5, time[4], message[4], cause[4]) + "," + lasted(6, time[5], message[5], cause[5]);
            }
        } else if (pages == 3) {
            if (stats.getMaxHealth() == 14.0F) {
                return (living(player, 7));
            } else if (stats.getMaxHealth() == 16.0F) {
                return lasted(7, time[6], message[6], cause[6]) + "," + living(player, 8);
            } else if (stats.getMaxHealth() == 18.0F) {
                return lasted(7, time[6], message[6], cause[6]) + "," + lasted(8, time[7], message[7], cause[7]) + "," + living(player, 9);
            } else {
                return lasted(7, time[6], message[6], cause[6]) + "," + lasted(8, time[7], message[7], cause[7]) + "," + lasted(9, time[8], message[8], cause[8]);
            }
        } else if (pages == 4) {
            if (stats.getMaxHealth() == 20.0F) {
                return (living(player, 10));
            } else {
                return lasted(10, time[9], message[9], cause[9]);
            }
        } else {
            return null;
        }
    }

    private static String title(String page) {
        return "{\"extra\":[{\"bold\":true,\"text\":\"Life History\"},{\"text\":\"\\n\\n\"}," + page + "],\"text\":\"\"}";
    }

    private static String living(PlayerEntity player, int hearts) {
        return hearts(hearts) + ",{\"text\":\"\\n\"},{\"color\":\"black\",\"text\":\"" + time(player) + "\"}";
    }

    private static String lasted(int hearts, String timeArray, String messageArray, String causeArray) {
        return hearts(hearts) + ",{\"text\":\"\\n\"},{\"color\":\"black\",\"text\":\"Lasted " + timeArray + "\\nEnded by \"},{\"color\":\"dark_red\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"" + messageArray + "\"}},\"text\":\"" + causeArray + "\"},{\"text\":\"\\n\\n\"}";
    }

    /** Returns how long the player has been living. */
    private static String time(PlayerEntity player) {
        return "Living " + LifeTextUtils.getTimeLiving(player);
    }

    private static String hearts(float hearts) {
        char h = 10084;

        if (hearts == 1) {
            return "{\"color\":\"red\",\"text\":\"" + h + "\"}";
        } else if (hearts == 2) {
            return "{\"color\":\"blue\",\"text\":\"" + h+h + "\"}";
        } else if (hearts == 3) {
            return "{\"color\":\"dark_green\",\"text\":\"" + h+h+h + "\"}";
        } else if (hearts == 4) {
            return "{\"color\":\"gold\",\"text\":\"" + h+h+h+h + "\"}";
        } else if (hearts == 5) {
            return "{\"color\":\"light_purple\",\"text\":\"" + h+h+h+h+h + "\"}";
        } else if (hearts == 6) {
            return "{\"color\":\"dark_purple\",\"text\":\"" + h+h+h+h+h+h + "\"}";
        } else if (hearts == 7) {
            return "{\"color\":\"yellow\",\"text\":\"" + h+h+h+h+h+h+h + "\"}";
        } else if (hearts == 8) {
            return "{\"color\":\"dark_aqua\",\"text\":\"" + h+h+h+h+h+h+h+h + "\"}";
        } else if (hearts == 9) {
            return "{\"color\":\"green\",\"text\":\"" + h+h+h+h+h+h+h+h+h + "\"}";
        } else if (hearts == 10) {
            return "{\"color\":\"black\",\"text\":\"" + h+h+h+h+h+h+h+h+h+h + "\"}";
        } else {
            return "";
        }
    }

}