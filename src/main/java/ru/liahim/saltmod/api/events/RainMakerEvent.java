package ru.liahim.saltmod.api.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class RainMakerEvent extends Event {

	private World world;
	private double x;
	private double y;
	private double z;
	private EntityPlayer player;
	private boolean isThunder;

	/**
	 * Fired when the RainMaker makes the rain.
	 * 
	 * @param world     - World.
	 * @param x, y, z   - Explosion coordinates.
	 * @param player    - The player, who launched a RainMaker. May be null.
	 * @param isThunder - There is a storm or not.
	 * 
	 */

	public RainMakerEvent(World world, double x, double y, double z, EntityPlayer player, boolean isThunder) {
		this.world = world;
		this.x = z;
		this.y = y;
		this.z = z;
		this.player = player;
		this.isThunder = isThunder;
	}

	public World getWorld() {
		return world;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public EntityPlayer getPlayer() {
		return player;
	}

	public boolean isThunder() {
		return isThunder;
	}

	public void setThunder(boolean thunder) {
		this.isThunder = thunder;
	}

	@Override
	public boolean isCancelable() {
		return true;
	}
}