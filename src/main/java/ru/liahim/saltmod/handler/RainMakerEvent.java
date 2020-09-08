package ru.liahim.saltmod.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event;

public class RainMakerEvent extends Event {

	private World world;
	private double x;
	private double y;
	private double z;
	private PlayerEntity player;
	private boolean isThunder;

	public RainMakerEvent(World world, double x, double y, double z, PlayerEntity player, boolean isThunder) {
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

	public PlayerEntity getPlayer() {
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