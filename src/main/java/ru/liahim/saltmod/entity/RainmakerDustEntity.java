package ru.liahim.saltmod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkHooks;
import ru.liahim.saltmod.handler.RainMakerEvent;
import ru.liahim.saltmod.init.ModAdvancements;
import ru.liahim.saltmod.init.ModEntities;
import ru.liahim.saltmod.init.ModItems;
import ru.liahim.saltmod.init.SaltConfig;

public class RainmakerDustEntity extends Entity {

	private int lifeTime;
	private boolean rain;
	private PlayerEntity player;
	private int cloud = SaltConfig.Game.cloudLevel.containsKey(this.world.dimension.getType().getId()) ?
			SaltConfig.Game.cloudLevel.get(this.world.dimension.getType().getId()) : 128;

	public RainmakerDustEntity(EntityType<? extends RainmakerDustEntity> type, World world) {
		super(type, world);
		this.setInvisible(true);
	}

	public RainmakerDustEntity(World world, double x, double y, double z, PlayerEntity player) {
		super(ModEntities.RAINMAKER_DUST.get(), world);
		this.setPosition(x, y, z);
		this.player = player;
	}

	@Override
	protected void registerData() {}

	@Override
	public void tick() {
		++this.lifeTime;
		if (this.world.isRemote && this.lifeTime > 30) {
			double x = this.getPosX() + this.rand.nextGaussian() * this.lifeTime/25;
			double y = this.getPosY() + this.rand.nextGaussian() * 4.0D - this.lifeTime/15;
			double z = this.getPosZ() + this.rand.nextGaussian() * this.lifeTime/25;
			this.world.addParticle(ParticleTypes.FIREWORK, true, x, y, z, 0.0D, 0.0D, 0.0D);
		}
		if (!rain && !this.world.isRemote && this.world.dimension.isSurfaceWorld() && this.lifeTime > 200 && this.getPosY() >= cloud && !this.world.getWorldInfo().isThundering()) {
			RainMakerEvent event = new RainMakerEvent(this.world, this.getPosX(), this.getPosY(), this.getPosZ(), this.player, this.rand.nextInt(5) == 0 || this.world.isRaining());
			MinecraftForge.EVENT_BUS.post(event);

			if (!event.isCanceled()) {
				ServerWorld world = DimensionManager.getWorld(this.getServer(), DimensionType.OVERWORLD, false, false);
				int i = (300 + world.rand.nextInt(600)) * 20;
				world.getWorldInfo().setClearWeatherTime(0);
				world.getWorldInfo().setRainTime(i);
				world.getWorldInfo().setThunderTime(i);
				world.getWorldInfo().setRaining(true);
				world.getWorldInfo().setThundering(event.isThunder());
				if (player instanceof ServerPlayerEntity) ModAdvancements.SALT_COMMON.trigger((ServerPlayerEntity)player, new ItemStack(ModItems.RAINMAKER.get()));
			}
			rain = true;
		}
		if (this.lifeTime > 250) this.remove();
    }

	@Override
	protected void readAdditional(CompoundNBT compound) {
		this.lifeTime = compound.getInt("Life");
		this.rain = compound.getBoolean("Rain");
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		compound.putInt("Life", this.lifeTime);
		compound.putBoolean("Rain", this.rain);
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public boolean canBeAttackedWithItem() {
		return false;
	}
}