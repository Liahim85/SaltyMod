package ru.liahim.saltmod.handler;

import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MagmaCubeEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import ru.liahim.saltmod.SaltyMod;
import ru.liahim.saltmod.init.ModAdvancements;
import ru.liahim.saltmod.init.ModBlocks;
import ru.liahim.saltmod.init.ModItems;
import ru.liahim.saltmod.init.SaltConfig;

@EventBusSubscriber(modid = SaltyMod.MODID)
public class ServerEventHandler {

	private static final UUID mudUUID = UUID.fromString("ca3f8f85-df1e-4fe8-8cf6-e7030f33ed8e");

	@SubscribeEvent
	public void RightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.FLOWER_POT
				&& event.getItemStack().getItem() == ModItems.SALTWORT_SEED.get()) {
			event.setUseBlock(Result.DENY);
		}
	}

	@SubscribeEvent
	public void onPlayerAttack(AttackEntityEvent e) {
		World world = e.getEntity().world;
		PlayerEntity player = e.getPlayer();
		Entity target = e.getTarget();
	    if (!world.isRemote && player != null && target != null) {
	    	ItemStack stack = player.getHeldItem(Hand.MAIN_HAND);
	    	if(Block.getBlockFromItem(stack.getItem()) == ModBlocks.SALT_CRYSTAL.get()) {
	    		boolean slime = target instanceof SlimeEntity && !(target instanceof MagmaCubeEntity);
		    	if (slime || target instanceof WitchEntity) {
    				target.attackEntityFrom(DamageSource.CACTUS, 30.0F);
    				world.playSound(null, new BlockPos(target.getPosX(), target.getPosY(), target.getPosZ()), SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
    				world.playSound(null, new BlockPos(target.getPosX(), target.getPosY(), target.getPosZ()), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.5F, 1.8F);
    				if (!player.isCreative()) {
    					stack.shrink(1);
    					if (stack.getCount() == 0) {
    						player.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
    					}
    					ItemEntity EI = new ItemEntity(world, target.getPosX(), target.getPosY(), target.getPosZ(), new ItemStack(ModItems.SALT_PINCH.get()));
    					EI.setPickupDelay(10);
    					world.addEntity(EI);
    					if (slime) {
	    					ItemEntity EIS = new ItemEntity(world, target.getPosX(), target.getPosY(), target.getPosZ(), new ItemStack(ModItems.ESCARGOT.get()));
	    					EI.setPickupDelay(10);
	    					world.addEntity(EIS);
	    					if (player instanceof ServerPlayerEntity) ModAdvancements.SALT_COMMON.trigger((ServerPlayerEntity)player, new ItemStack(ModItems.ESCARGOT.get()));
    					} else if (player instanceof ServerPlayerEntity) ModAdvancements.SALT_COMMON.trigger((ServerPlayerEntity)player, new ItemStack(Items.GLASS_BOTTLE));
    				}
	    		}
	    	}
	    }
	}

	@SubscribeEvent
	public void updateArmor(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.START && event.side == LogicalSide.SERVER) {
			PlayerEntity player = event.player;
			if (player != null) {
				int health = 0;
				int check = 0;
				if (player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == ModItems.MUD_HELMET.get()) {
					health += 4;
					check = 1;
				}
				if (player.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() == ModItems.MUD_CHESTPLATE.get()) {
					health += 6;
					check = 2;
				}
				if (player.getItemStackFromSlot(EquipmentSlotType.LEGS).getItem() == ModItems.MUD_LEGGINGS.get()) {
					health += 6;
					check = 2;
				}
				if (player.getItemStackFromSlot(EquipmentSlotType.FEET).getItem() == ModItems.MUD_BOOTS.get() ||
						player.world.getBlockState(new BlockPos(MathHelper.floor(player.getPosX()), MathHelper.floor(player.getPosY()), MathHelper.floor(player.getPosZ()))).getBlock() == ModBlocks.MUD_BLOCK.get()) {
					health += 4;
					check = 1;
				}
				IAttributeInstance boost = event.player.getAttribute(SharedMonsterAttributes.MAX_HEALTH);
				AttributeModifier am = boost.getModifier(mudUUID);
				int current = (int) (am != null ? am.getAmount() : 0);
				if (current != health) {
					boost.removeModifier(mudUUID);
					boost.applyModifier(new AttributeModifier(mudUUID, "mudHealth", health, AttributeModifier.Operation.ADDITION));
				}
				if (check > 0 && player.getHealth() < player.getMaxHealth() && player.getFoodStats().getFoodLevel() > 0) {
					if (player.ticksExisted % ((10 - check) * SaltConfig.Game.mudRegenSpeed.get()) == 0) player.heal(1);
					if (check == 6) {
						if (player.isBurning()) player.extinguish();
						if (player instanceof ServerPlayerEntity) ModAdvancements.SALT_COMMON.trigger((ServerPlayerEntity)player, new ItemStack(ModItems.MUD_HELMET.get()));
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void addTempt(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof AnimalEntity) {
			AnimalEntity animal = (AnimalEntity) event.getEntity();
			if (animal instanceof CowEntity || animal instanceof HorseEntity) {
				animal.goalSelector.addGoal(3, new TemptGoal(animal, 1.25D, Ingredient.fromItems(ModItems.SALT.get()), false));
			}
		}
	}
}