package com.jayhill.xlife.common.util;

import com.jayhill.xlife.common.capability.stats.IStatsCapability;
import com.jayhill.xlife.common.capability.stats.StatsCapability;
import com.jayhill.xlife.common.capability.time.ITimeCapability;
import com.jayhill.xlife.common.capability.time.TimeCapability;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.StringNBT;

/** This adds the players life information to a book. */
@SuppressWarnings("all")
public class HealthBookUtils {

    /** These pages are set when the players hearts increase. */
    public static StringNBT setPages(PlayerEntity player, int pages) {
        return StringNBT.valueOf(title(getPages(player, pages)));
    }

    /** Gets the pages written in the book. */
    private static String getPages(PlayerEntity player, int pages) {
        IStatsCapability stats = player.getCapability(StatsCapability.STATS_CAPABILITY).orElse(null);
        String[] time = stats.getTime();
        String[] cause = stats.getCause();

        if (pages == 1) {
            if (player.getMaxHealth() == 2.0F) {
                return (living(player, 1));
            } else if (player.getMaxHealth() == 4.0F) {
                return lasted(1, time[0], cause[0]) + "," + living(player, 2);
            } else if (player.getMaxHealth() == 6.0F) {
                return lasted(1, time[0], cause[0]) + "," + lasted(2, time[1], cause[1]) + "," + living(player, 3);
            } else {
                return lasted(1, time[0], cause[0]) + "," + lasted(2, time[1], cause[1]) + "," + lasted(3, time[2], cause[2]);
            }
        } else if (pages == 2) {
            if (player.getMaxHealth() == 8.0F) {
                return (living(player, 4));
            } else if (player.getMaxHealth() == 10.0F) {
                return lasted(4, time[3], cause[3]) + "," + living(player, 5);
            } else if (player.getMaxHealth() == 12.0F) {
                return lasted(4, time[3], cause[3]) + "," + lasted(5, time[4], cause[4]) + "," + living(player, 6);
            } else {
                return lasted(4, time[3], cause[3]) + "," + lasted(5, time[4], cause[4]) + "," + lasted(6, time[5], cause[5]);
            }
        } else if (pages == 3) {
            if (player.getMaxHealth() == 14.0F) {
                return (living(player, 7));
            } else if (player.getMaxHealth() == 16.0F) {
                return lasted(7, time[6], cause[6]) + "," + living(player, 8);
            } else if (player.getMaxHealth() == 18.0F) {
                return lasted(7, time[6], cause[6]) + "," + lasted(8, time[7], cause[7]) + "," + living(player, 9);
            } else {
                return lasted(7, time[6], cause[6]) + "," + lasted(8, time[7], cause[7]) + "," + lasted(9, time[8], cause[8]);
            }
        } else if (pages == 4) {
            if (player.getMaxHealth() == 20.0F) {
                return (living(player, 10));
            } else {
                return lasted(10, time[9], cause[9]);
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

    private static String lasted(int hearts, String timeArray, String causeArray) {
        return hearts(hearts) + ",{\"text\":\"\\n\"},{\"color\":\"black\",\"text\":\"Lasted " + timeArray + "\\nEnded by \"},{\"color\":\"dark_red\",\"text\":\"" + causeArray + "\"},{\"text\":\"\\n\\n\"}";
    }

    /** Returns how long the player has been living. */
    private static String time(PlayerEntity player) {
        ITimeCapability time = player.getCapability(TimeCapability.TIME_CAPABILITY).orElse(null);

        if (time.getTime() == 1) {
            return "Living 1 second";
        } else if (time.getTime() < 60) {
            return "Living " + time.getTime() + " seconds";
        } else if (time.getTime() >= 60 && time.getTime() < 120) {
            return "Living 1 minute";
        } else if (time.getTime() >= 120 && time.getTime() < 3600) {
            return "Living " + time.getTime() / 60 + " minutes";
        } else if (time.getTime() >= 3600 && time.getTime() < 7200) {
            return "Living 1 hour";
        } else {
            return "Living " + time.getTime() / 3600 + " hours";
        }
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