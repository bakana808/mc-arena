package com.octopod.arenacore;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.WitherSkull;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class Hologram
{
	public static Map<Integer, Hologram> holograms = new HashMap<>();

	int id;
	Location loc;
	double spacing;
	String[] lines;

	List<Entity[]> entity_pairs = new ArrayList<>();

	public static void killAll()
	{
		for(Hologram h: holograms.values())
		{
			h.kill();
		}
		holograms = new HashMap<>();
	}

	public static void kill(int id)
	{
		Hologram h = holograms.get(id);
		if(h != null) h.kill();
		holograms.remove(id);
	}

	public static int nextID()
	{
		int id = 0;
		while(holograms.containsKey(id)) id++;
		return id;
	}

	public Hologram(Location loc, double spacing, String... lines)
	{
		this.loc = loc;
		this.spacing = spacing;
		this.lines = lines;
		this.id = nextID();

		Location clonedLoc = loc.clone();
		clonedLoc.add(0, 56, 0);

		//Read lines in reverse
		for(int i = lines.length - 1; i >= 0; i--)
		{
			makeline(clonedLoc, lines[i]);
			clonedLoc.add(0, spacing, 0);
		}

		holograms.put(id, this);
	}

	public int getId() {return id;}

	private void makeline(Location loc, String line)
	{
		//The base
		WitherSkull skull = loc.getWorld().spawn(loc, WitherSkull.class);
		//The rider (which will carry the text)
		Horse horse = loc.getWorld().spawn(loc, Horse.class);
		//Stop the skull from moving
		skull.setPassenger(horse);
		skull.setDirection(new Vector(0, 0, 0));
		//Set age in the negatives to make it invisible
		horse.setAge(-1700000);
		horse.setAgeLock(true);
		horse.setCustomNameVisible(true);
		horse.setCustomName(line);
		entity_pairs.add(new Entity[]{skull, horse});
	}

	public void edit(String... lines)
	{
		Location clonedLoc = this.loc.clone();
		while(entity_pairs.size() > lines.length)
		{
			int i = entity_pairs.size() - 1;
			entity_pairs.get(i)[0].remove();
			entity_pairs.get(i)[1].remove();
			entity_pairs.remove(i);
		}
		for(int i = lines.length - 1; i >= 0; i--)
		{
			if(i > entity_pairs.size() - 1)
			{
				//Create Line
				makeline(clonedLoc, lines[i]);
			} else {
				//Edit Line
				((Horse)entity_pairs.get(i)[1]).setCustomName(lines[i]);
			}
			clonedLoc.add(0, spacing, 0);
		}
	}

	public void kill()
	{
		for(Entity[] ep: entity_pairs)
		{
			ep[0].remove();
			ep[1].remove();
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		return !(obj instanceof Hologram) || ((Hologram)obj).id == this.id;
	}

}
