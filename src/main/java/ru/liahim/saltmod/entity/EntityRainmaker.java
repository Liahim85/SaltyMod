package ru.liahim.saltmod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.liahim.saltmod.item.Rainmaker;

public class EntityRainmaker extends Entity {

	private int fireworkAge;
	private int lifetime;
	private EntityPlayer player;

	public EntityRainmaker(World world) {
		super(world);
		this.setSize(0.25F, 0.25F);
	}

	@Override
	protected void entityInit() {}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance) {
		return distance < 4096.0D;
	}

	public EntityRainmaker(World world, double x, double y, double z, EntityPlayer player) {
		super(world);
		this.fireworkAge = 0;
		this.player = player;
		this.setSize(0.25F, 0.25F);
		this.setPosition(x, y, z);

		this.motionX = this.rand.nextGaussian() * 0.0005D;
		this.motionZ = this.rand.nextGaussian() * 0.0005D;
		this.motionY = 0.05D;
		this.lifetime = 45 + this.rand.nextInt(6) + this.rand.nextInt(7);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z) {
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt(x * x + z * z);
			this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(x, z) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(y, f) * 180.0D / Math.PI);
		}
	}

	@Override
	public void onUpdate() {
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
		super.onUpdate();
		this.motionX *= 1.15D;
		this.motionZ *= 1.15D;
		this.motionY += 0.04D;
		this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
		float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

		for (this.rotationPitch = (float) (Math.atan2(this.motionY, f) * 180.0D / Math.PI); this.rotationPitch
				- this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {}

		while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

		if (this.fireworkAge == 0 && !this.isSilent()) {
			this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ,
					SoundEvents.ENTITY_FIREWORK_LAUNCH, SoundCategory.AMBIENT, 3.0F, 1.0F);
		}

		++this.fireworkAge;

		if (this.world.isRemote && this.fireworkAge % 2 < 2) {
			this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, true, this.posX, this.posY - 0.3D, this.posZ,
					this.rand.nextGaussian() * 0.05D, -this.motionY * 0.5D, this.rand.nextGaussian() * 0.05D, new int[0]);
		}

		if (!this.world.isRemote && this.fireworkAge > this.lifetime) {
			this.world.setEntityState(this, (byte) 17);
			EntityRainmakerDust dust = new EntityRainmakerDust(this.world, this.posX, this.posY, this.posZ, this.player);
			this.world.spawnEntity(dust);
			this.setDead();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id) {
		if (id == 17 && this.world.isRemote) {
			this.world.makeFireworks(this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ, Rainmaker.tag);
		}
		super.handleStatusUpdate(id);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		tag.setInteger("Life", this.fireworkAge);
		tag.setInteger("LifeTime", this.lifetime);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		this.fireworkAge = tag.getInteger("Life");
		this.lifetime = tag.getInteger("LifeTime");
	}

	@Override
	public boolean canBeAttackedWithItem() {
		return false;
	}
}