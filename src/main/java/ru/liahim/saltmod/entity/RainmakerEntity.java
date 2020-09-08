package ru.liahim.saltmod.entity;

import java.util.OptionalInt;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.network.NetworkHooks;
import ru.liahim.saltmod.init.ModEntities;
import ru.liahim.saltmod.init.ModItems;
import ru.liahim.saltmod.item.Rainmaker;

public class RainmakerEntity extends Entity implements IRendersAsItem, IProjectile {
	private static final DataParameter<OptionalInt> BOOSTED_ENTITY_ID = EntityDataManager.createKey(RainmakerEntity.class, DataSerializers.OPTIONAL_VARINT);
	private static final DataParameter<Boolean> SHOT_AT_ANGLE = EntityDataManager.createKey(RainmakerEntity.class, DataSerializers.BOOLEAN);
	private int fireworkAge;
	private int lifetime;
	private PlayerEntity player;
	private LivingEntity boostedEntity;

	public RainmakerEntity(EntityType<? extends RainmakerEntity> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerData() {
		this.dataManager.register(BOOSTED_ENTITY_ID, OptionalInt.empty());
		this.dataManager.register(SHOT_AT_ANGLE, false);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isInRangeToRenderDist(double distance) {
		return distance < 4096.0D && !this.isAttachedToEntity();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isInRangeToRender3d(double x, double y, double z) {
		return super.isInRangeToRender3d(x, y, z) && !this.isAttachedToEntity();
	}

	public RainmakerEntity(World world, double x, double y, double z, PlayerEntity player) {
		super(ModEntities.RAINMAKER.get(), world);
		this.fireworkAge = 0;
		this.setPosition(x, y, z);
		this.player = player;
		this.setMotion(this.rand.nextGaussian() * 0.001D, 0.05D, this.rand.nextGaussian() * 0.001D);
		this.lifetime = 45 + this.rand.nextInt(6) + this.rand.nextInt(7);
	}

	public RainmakerEntity(World world, PlayerEntity player) {
		this(world, player.getPosX(), player.getPosY(), player.getPosZ(), player);
		this.dataManager.set(BOOSTED_ENTITY_ID, OptionalInt.of(player.getEntityId()));
		this.boostedEntity = player;
	}

	public RainmakerEntity(World world, double x, double y, double z, boolean shotAtAngle, PlayerEntity player) {
		this(world, x, y, z, player);
		this.dataManager.set(SHOT_AT_ANGLE, shotAtAngle);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void setVelocity(double x, double y, double z) {
		this.setMotion(x, y, z);
		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt(x * x + z * z);
			this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180F / (float) Math.PI));
			this.rotationPitch = (float) (MathHelper.atan2(y, f) * (180F / (float) Math.PI));
			this.prevRotationYaw = this.rotationYaw;
			this.prevRotationPitch = this.rotationPitch;
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (this.isAttachedToEntity()) {
			if (this.boostedEntity == null) {
				this.dataManager.get(BOOSTED_ENTITY_ID).ifPresent((p_213891_1_) -> {
					Entity entity = this.world.getEntityByID(p_213891_1_);
					if (entity instanceof LivingEntity) {
						this.boostedEntity = (LivingEntity) entity;
					}

				});
			}
			if (this.boostedEntity != null) {
				if (this.boostedEntity.isElytraFlying()) {
					Vec3d vec3d = this.boostedEntity.getLookVec();
					Vec3d vec3d1 = this.boostedEntity.getMotion();
					this.boostedEntity.setMotion(vec3d1.add(vec3d.x * 0.1D + (vec3d.x * 1.5D - vec3d1.x) * 0.5D,
							vec3d.y * 0.1D + (vec3d.y * 1.5D - vec3d1.y) * 0.5D,
							vec3d.z * 0.1D + (vec3d.z * 1.5D - vec3d1.z) * 0.5D));
				}
				this.setPosition(this.boostedEntity.getPosX(), this.boostedEntity.getPosY(), this.boostedEntity.getPosZ());
				this.setMotion(this.boostedEntity.getMotion());
			}
		} else {
			if (!this.shotAtAngle()) {
				this.setMotion(this.getMotion().mul(1.15D, 1.0D, 1.15D).add(0.0D, 0.04D, 0.0D));
			}
			this.move(MoverType.SELF, this.getMotion());
		}

		Vec3d vec3d2 = this.getMotion();
		RayTraceResult raytraceresult = ProjectileHelper.rayTrace(this, this.getBoundingBox().expand(vec3d2).grow(1.0D),
				(p_213890_0_) -> {
					return !p_213890_0_.isSpectator() && p_213890_0_.isAlive() && p_213890_0_.canBeCollidedWith();
				}, RayTraceContext.BlockMode.COLLIDER, true);
		if (!this.noClip) {
			this.func_213892_a(raytraceresult);
			this.isAirBorne = true;
		}

		float f = MathHelper.sqrt(horizontalMag(vec3d2));
		this.rotationYaw = (float) (MathHelper.atan2(vec3d2.x, vec3d2.z) * (180F / (float) Math.PI));

		for (this.rotationPitch = (float) (MathHelper.atan2(vec3d2.y, f)
				* (180F / (float) Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
			;
		}

		while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = MathHelper.lerp(0.2F, this.prevRotationPitch, this.rotationPitch);
		this.rotationYaw = MathHelper.lerp(0.2F, this.prevRotationYaw, this.rotationYaw);
		if (this.fireworkAge == 0 && !this.isSilent()) {
			this.world.playSound((PlayerEntity) null, this.getPosX(), this.getPosY(), this.getPosZ(),
					SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.AMBIENT, 3.0F, 1.0F);
		}

		++this.fireworkAge;
		if (this.world.isRemote && this.fireworkAge % 2 < 2) {
			this.world.addParticle(ParticleTypes.FIREWORK, this.getPosX(), this.getPosY() - 0.3D, this.getPosZ(),
					this.rand.nextGaussian() * 0.05D, -this.getMotion().y * 0.5D, this.rand.nextGaussian() * 0.05D);
		}

		if (!this.world.isRemote && this.fireworkAge > this.lifetime) {
			this.expload();
		}
	}

	private void expload() {
		this.world.setEntityState(this, (byte) 17);
		this.dealExplosionDamage();
		this.world.addEntity(new RainmakerDustEntity(world, this.getPosX(), this.getPosY(), this.getPosZ(), this.player));
		this.remove();
	}

	protected void func_213892_a(RayTraceResult result) {
		if (result.getType() != RayTraceResult.Type.MISS && ForgeEventFactory.onProjectileImpact(this, result)) return;
		if (result.getType() == RayTraceResult.Type.ENTITY && !this.world.isRemote) this.expload();
		else if (this.collided) {
			BlockPos blockpos;
			if (result.getType() == RayTraceResult.Type.BLOCK) blockpos = new BlockPos(((BlockRayTraceResult) result).getPos());
			else blockpos = new BlockPos(this);
			this.world.getBlockState(blockpos).onEntityCollision(this.world, blockpos, this);
			this.expload();
		}
	}

	private void dealExplosionDamage() {
		float f = 0.0F;
		ListNBT listnbt = Rainmaker.tag.getList("Explosions", 10);
		if (listnbt != null && !listnbt.isEmpty()) f = 5.0F + listnbt.size() * 2;
		if (f > 0.0F) {
			if (this.boostedEntity != null) {
				this.boostedEntity.attackEntityFrom(DamageSource.FIREWORKS, 5.0F + listnbt.size() * 2);
			}
			Vec3d vec3d = this.getPositionVec();
			for (LivingEntity livingentity : this.world.getEntitiesWithinAABB(LivingEntity.class,
					this.getBoundingBox().grow(5.0D))) {
				if (livingentity != this.boostedEntity && !(this.getDistanceSq(livingentity) > 25.0D)) {
					boolean flag = false;
					for (int i = 0; i < 2; ++i) {
						Vec3d vec3d1 = new Vec3d(livingentity.getPosX(), livingentity.getPosYHeight(0.5D * i), livingentity.getPosZ());
						RayTraceResult raytraceresult = this.world.rayTraceBlocks(new RayTraceContext(vec3d, vec3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
						if (raytraceresult.getType() == RayTraceResult.Type.MISS) {
							flag = true;
							break;
						}
					}
					if (flag) {
						float f1 = f * (float) Math.sqrt((5.0D - this.getDistance(livingentity)) / 5.0D);
						livingentity.attackEntityFrom(DamageSource.FIREWORKS, f1);
					}
				}
			}
		}
	}

	private boolean isAttachedToEntity() {
		return this.dataManager.get(BOOSTED_ENTITY_ID).isPresent();
	}

	public boolean shotAtAngle() {
		return this.dataManager.get(SHOT_AT_ANGLE);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handleStatusUpdate(byte id) {
		if (id == 17 && this.world.isRemote) {
			Vec3d vec3d = this.getMotion();
			this.world.makeFireworks(this.getPosX(), this.getPosY(), this.getPosZ(), vec3d.x, vec3d.y, vec3d.z, Rainmaker.tag);
		}
		super.handleStatusUpdate(id);
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		compound.putInt("Life", this.fireworkAge);
		compound.putInt("LifeTime", this.lifetime);
		compound.putBoolean("ShotAtAngle", this.dataManager.get(SHOT_AT_ANGLE));
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		this.fireworkAge = compound.getInt("Life");
		this.lifetime = compound.getInt("LifeTime");
		if (compound.contains("ShotAtAngle")) {
			this.dataManager.set(SHOT_AT_ANGLE, compound.getBoolean("ShotAtAngle"));
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem() {
		return new ItemStack(ModItems.RAINMAKER);
	}

	@Override
	public boolean canBeAttackedWithItem() {
		return false;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
		float f = MathHelper.sqrt(x * x + y * y + z * z);
		x = x / f;
		y = y / f;
		z = z / f;
		x = x + this.rand.nextGaussian() * 0.0075F * inaccuracy;
		y = y + this.rand.nextGaussian() * 0.0075F * inaccuracy;
		z = z + this.rand.nextGaussian() * 0.0075F * inaccuracy;
		x = x * velocity;
		y = y * velocity;
		z = z * velocity;
		this.setMotion(x, y, z);
	}
}