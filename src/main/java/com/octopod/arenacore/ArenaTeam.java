package com.octopod.arenacore;

import org.bukkit.ChatColor;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public enum ArenaTeam {

    SPEC(0, "Spectator", ChatColor.WHITE),

    RED(1, "Red", ChatColor.RED),
    BLUE(2, "Blue", ChatColor.BLUE),
    GREEN(3, "Green", ChatColor.GREEN),
    YELLOW(4, "Yellow", ChatColor.YELLOW),
    ORANGE(5, "Gold", ChatColor.GOLD),
    SILVER(6, "Silver", ChatColor.GRAY);

    private int id;
    private String name;
    //TODO: abstract this classes
    private ChatColor color;

    private ArenaTeam(int id, String name, ChatColor color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public int getID() {return id;}
    public String getName() {return name;}
    public ChatColor getColor() {return color;}

}
