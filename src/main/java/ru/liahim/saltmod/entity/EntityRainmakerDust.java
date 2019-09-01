package ru.liahim.saltmod.entity;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import ru.liahim.saltmod.api.events.RainMakerEvent;
import ru.liahim.saltmod.api.item.SaltItems;
import ru.liahim.saltmod.init.ModAdvancements;
import ru.liahim.saltmod.init.SaltConfig;

public class EntityRainmakerDust extends Entity {

	private int lifeTime;
	private boolean rain;
	private EntityPlayer player;
	private int cloud = SaltConfig.cloudLevel.containsKey(this.world.provider.getDimension()) ?
			SaltConfig.cloudLevel.get(this.world.provider.getDimension()) : 128;

	public EntityRainmakerDust(World world) {
		super(world);
		this.setInvisible(true);
	}

	public EntityRainmakerDust(World world, double x, double y, double z, EntityPlayer player) {
		super(world);
		this.setPosition(x, y, z);
		this.player = player;
	}

	@Override
	protected void entityInit() {}

	@Override
	public void onUpdate() {
		++this.lifeTime;
		if (this.world.isRemote && this.lifeTime > 30) {
			double x = this.posX + this.rand.nextGaussian() * this.lifeTime/25;
			double y = this.posY + this.rand.nextGaussian() * 4.0D - this.lifeTime/15;
			double z = this.posZ + this.rand.nextGaussian() * this.lifeTime/25;
			this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, true, x, y, z, 0.0D, 0.0D, 0.0D);
		}
		if (!rain && !this.world.isRemote && this.world.provider.isSurfaceWorld() && this.lifeTime > 200 && this.posY >= cloud && !this.world.getWorldInfo().isThundering()) {
			RainMakerEvent event = new RainMakerEvent(this.world, this.posX, this.posY, this.posZ, this.player, this.rand.nextInt(5) == 0 || this.world.isRaining());
			MinecraftForge.EVENT_BUS.post(event);

			if (!event.isCanceled()) {
				World world = DimensionManager.getWorld(0);
				int i = (300 + (new Random()).nextInt(600)) * 20;
				world.getWorldInfo().setCleanWeatherTime(0);
				world.getWorldInfo().setRainTime(i);
				world.getWorldInfo().setThunderTime(i);
				world.getWorldInfo().setRaining(true);
				world.getWorldInfo().setThundering(event.isThunder());
				if (player instanceof EntityPlayerMP) ModAdvancements.SALT_COMMON.trigger((EntityPlayerMP)player, new ItemStack(SaltItems.RAINMAKER));
			}
			rain = true;
		}
		if (this.lifeTime > 250) this.setDead();
    }

	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		tag.setInteger("Life", this.lifeTime);
		tag.setBoolean("Rain", this.rain);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		this.lifeTime = tag.getInteger("Life");
		this.rain = tag.getBoolean("Rain");
	}

	@Override
	public boolean canBeAttackedWithItem() {
		return false;
	}
}