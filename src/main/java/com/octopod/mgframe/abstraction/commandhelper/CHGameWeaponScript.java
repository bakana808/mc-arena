package com.octopod.mgframe.abstraction.commandhelper;

import com.laytonsmith.abstraction.bukkit.BukkitMCPlayer;
import com.laytonsmith.core.Procedure;
import com.laytonsmith.core.exceptions.ConfigCompileException;
import com.octopod.mgframe.MGLoadException;
import com.octopod.mgframe.abstraction.MGamePlayer;
import com.octopod.mgframe.abstraction.MGameWeaponScript;
import com.octopod.mgframe.abstraction.bukkit.BukkitGamePlayer;
import com.octopod.mgframe.commandhelper.MethodScript;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class CHGameWeaponScript implements MGameWeaponScript {

    private MethodScript lcl_proc, rcl_proc;

    public CHGameWeaponScript(MethodScript script) throws MGLoadException
    {
        Map<String, Procedure> procs = script.getProcedures();

        if(!procs.containsKey("_left_click"))
            throw new MGLoadException("Missing _left_click() procedure!");
        if(!procs.containsKey("_right_click"))
            throw new MGLoadException("Missing _right_click() procedure!");

        try {
            //Create new script running the left click procedure
            lcl_proc = new MethodScript("_left_click()").include("_left_click", procs.get("_left_click"));

            //Create new script running the right click procedure
            rcl_proc = new MethodScript("_right_click()").include("_right_click", procs.get("_right_click"));
        } catch (ConfigCompileException e) {}
    }

    @Override
    public void left_click(MGamePlayer player) {
        if(player instanceof BukkitGamePlayer) {
            lcl_proc.executeAs(new BukkitMCPlayer((Player)player.getHandle()));
        }
    }

    @Override
    public void right_click(MGamePlayer player) {
        if(player instanceof BukkitGamePlayer) {
            rcl_proc.executeAs(new BukkitMCPlayer((Player)player.getHandle()));
        }
    }
}
