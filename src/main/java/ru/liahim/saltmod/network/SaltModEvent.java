package ru.liahim.saltmod.network;

import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.liahim.saltmod.SaltyMod;
import ru.liahim.saltmod.api.block.SaltBlocks;
import ru.liahim.saltmod.api.item.SaltItems;
import ru.liahim.saltmod.init.ModAdvancements;
import ru.liahim.saltmod.init.SaltConfig;

public class SaltModEvent {

	private static final UUID uuid1 = UUID.fromString("ca3f8f85-df1e-4fe8-8cf6-e7030f33ed8e");
	private static final UUID uuid2 = UUID.fromString("42e70891-8397-4cf0-aca3-1a1d237768eb");
	private static final UUID uuid3 = UUID.fromString("b94a045b-f0e9-413a-86fe-2a8473f9ce9d");
	private static final UUID uuid4 = UUID.fromString("8fc1c0b4-350a-45d8-83c7-c788ec55b501");

	private static final AttributeModifier headModifierUP = (new AttributeModifier(uuid1, "mudBoostUP", 4F, 0));
	private static final AttributeModifier bodyModifierUP = (new AttributeModifier(uuid2, "mudBoostUP", 6F, 0));
	private static final AttributeModifier legsModifierUP = (new AttributeModifier(uuid3, "mudBoostUP", 6F, 0));
	private static final AttributeModifier feetModifierUP = (new AttributeModifier(uuid4, "mudBoostUP", 4F, 0));

	@SubscribeEvent
	public void onPlayerAttack(AttackEntityEvent e) {
		
		World world = e.getEntityPlayer().world;
		
	    if (!world.isRemote && e.getEntityPlayer() != null && e.getTarget() != null && e.getTarget() instanceof EntityLivingBase) {

	    	EntityPlayer player = e.getEntityPlayer();
			EntityLivingBase target = (EntityLivingBase)e.getTarget();
	        ItemStack is = player.getHeldItem(EnumHand.MAIN_HAND);
	        Block block = null;
	    	
	    	if (!is.isEmpty() && EntityList.getEntityString(target) != null &&
	    	  ((EntityList.getEntityString(target).toLowerCase().contains("slime") &&
	    		!EntityList.getEntityString(target).toLowerCase().contains("lava")) ||
	    		EntityList.getEntityString(target).toLowerCase().contains("witch"))) {

	    		if (is.getItem() instanceof ItemBlock && Block.getBlockFromItem(is.getItem()) != Blocks.AIR) {
	                block = Block.getBlockFromItem(is.getItem());	                
	            }

	    		if (block != null) {
	    			if (block == SaltBlocks.SALT_CRYSTAL) {
	    				target.attackEntityFrom(DamageSource.CACTUS, 30.0F);
	    				world.playSound(null, new BlockPos(target.posX, target.posY, target.posZ), SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
	    				world.playSound(null, new BlockPos(target.posX, target.posY, target.posZ), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.5F, 1.8F);

	    				if (!player.isCreative()) {

	    					is.setCount(is.getCount() - 1);
	    					if (is.getCount() == 0) {player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);}

	    					EntityItem EI = new EntityItem(world, target.posX, target.posY, target.posZ, new ItemStack(SaltItems.SALT_PINCH));
	    					EI.setPickupDelay(10);
	    					world.spawnEntity(EI);

	    					if (EntityList.getEntityString(target).toLowerCase().contains("witch") && player instanceof EntityPlayerMP)
	    						ModAdvancements.SALT_COMMON.trigger((EntityPlayerMP)player, new ItemStack(Items.GLASS_BOTTLE));

	    					if (target instanceof EntitySlime) {
		    					EntityItem EIS = new EntityItem(world, target.posX, target.posY, target.posZ, new ItemStack(SaltItems.ESCARGOT));
		    					EI.setPickupDelay(10);
		    					world.spawnEntity(EIS);
		    					if (player instanceof EntityPlayerMP) ModAdvancements.SALT_COMMON.trigger((EntityPlayerMP)player, new ItemStack(SaltItems.ESCARGOT));
	    					}
	    				}
	    			}
	    		}
	    	}
	    }
	}

	@SubscribeEvent
	public void updateArmor(TickEvent.PlayerTickEvent event) {
		
		if (event.phase == TickEvent.Phase.START && event.side == Side.SERVER) {
			EntityPlayer player = event.player;
		
			if (player != null) {
				ItemStack head = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
				ItemStack body = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
				ItemStack legs = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
				ItemStack feet = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
				
				boolean mud = player.world.getBlockState(new BlockPos(MathHelper.floor(player.posX), MathHelper.floor(player.posY), MathHelper.floor(player.posZ))).getBlock() == SaltBlocks.MUD_BLOCK;

				IAttributeInstance boost = event.player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);

				if (!head.isEmpty() && boost.getModifier(uuid1) == null && head.getItem() == SaltItems.MUD_HELMET) {

					boost.applyModifier(headModifierUP);}

				if ((head.isEmpty() || head.getItem() != SaltItems.MUD_HELMET) && boost.getModifier(uuid1) != null) {

					boost.removeModifier(headModifierUP);
					if (player.getHealth() > player.getMaxHealth()) {player.setHealth(player.getMaxHealth());}}

				if (!body.isEmpty() && boost.getModifier(uuid2) == null && body.getItem() == SaltItems.MUD_CHESTPLATE) {

					boost.applyModifier(bodyModifierUP);}
				
				if ((body.isEmpty() || body.getItem() != SaltItems.MUD_CHESTPLATE) && boost.getModifier(uuid2) != null) {

					boost.removeModifier(bodyModifierUP);
					if (player.getHealth() > player.getMaxHealth()) {player.setHealth(player.getMaxHealth());}}

				if (!legs.isEmpty() && boost.getModifier(uuid3) == null && legs.getItem() == SaltItems.MUD_LEGGINGS) {

					boost.applyModifier(legsModifierUP);}
				
				if ((legs.isEmpty() || legs.getItem() != SaltItems.MUD_LEGGINGS) && boost.getModifier(uuid3) != null) {

					boost.removeModifier(legsModifierUP);
					if (player.getHealth() > player.getMaxHealth()) {player.setHealth(player.getMaxHealth());}}


				if (((!feet.isEmpty() && feet.getItem() == SaltItems.MUD_BOOTS) || mud) && boost.getModifier(uuid4) == null) {

					boost.applyModifier(feetModifierUP);}

				if ((feet.isEmpty() || feet.getItem() != SaltItems.MUD_BOOTS) && !mud && boost.getModifier(uuid4) != null) {

					boost.removeModifier(feetModifierUP);
					if (player.getHealth() > player.getMaxHealth()) {player.setHealth(player.getMaxHealth());}}	
				
				if (player.getHealth() < player.getMaxHealth() && player.getFoodStats().getFoodLevel() > 0) {
					int chek = 0;
					
					if (!head.isEmpty() && head.getItem() == SaltItems.MUD_HELMET)
					{chek = chek + 1;}					
					if (!body.isEmpty() && body.getItem() == SaltItems.MUD_CHESTPLATE)
					{chek = chek + 2;}
					if (!legs.isEmpty() && legs.getItem() == SaltItems.MUD_LEGGINGS)
					{chek = chek + 2;}
					if ((!feet.isEmpty() && feet.getItem() == SaltItems.MUD_BOOTS) || mud)
					{chek = chek + 1;}
				
					if (chek > 0) {
						if (player.ticksExisted % ((10 - chek)*SaltConfig.mudRegenSpeed) == 0) {
							player.heal(1);
						} 
						if (chek == 6) {
							if (player.isBurning()) player.extinguish();
							if (player instanceof EntityPlayerMP) ModAdvancements.SALT_COMMON.trigger((EntityPlayerMP)player, new ItemStack(SaltItems.MUD_HELMET));
						}
					}
				}
			}
		}
	}

	// Farming
	@SubscribeEvent
	public void addTempt(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof EntityAnimal) {
			EntityAnimal animal = (EntityAnimal) event.getEntity();
			if (animal instanceof EntityCow || animal instanceof EntityHorse) {
				animal.tasks.addTask(3, new EntityAITempt(animal, 1.25D, SaltItems.SALT, false));
			}
		}
	}

	// MilkIconRegister
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerIcons(TextureStitchEvent.Pre event) {
		event.getMap().registerSprite(new ResourceLocation(SaltyMod.MODID, "blocks/milk"));
	}

	@SubscribeEvent
	public void onLootTablesLoaded(LootTableLoadEvent event) {
		if (event.getName().equals(LootTableList.CHESTS_SPAWN_BONUS_CHEST)) {
			final LootPool pool2 = event.getTable().getPool("pool2");
			if (pool2 != null) {
				pool2.addEntry(new LootEntryItem(SaltItems.SALT, 5, 0, new LootFunction[] { new SetCount(new LootCondition[0], new RandomValueRange(2, 5)) }, new LootCondition[0], "saltmod:salt"));
			}
		} else if (event.getName().equals(LootTableList.CHESTS_SIMPLE_DUNGEON)) {
			final LootPool pool2 = event.getTable().getPool("pool2");
			if (pool2 != null) {
				pool2.addEntry(new LootEntryItem(SaltItems.SALT, 5, 0, new LootFunction[] { new SetCount(new LootCondition[0], new RandomValueRange(2, 5)) }, new LootCondition[0], "saltmod:salt"));
				pool2.addEntry(new LootEntryItem(SaltItems.SALTWORT_SEED, 5, 0, new LootFunction[] { new SetCount(new LootCondition[0], new RandomValueRange(2, 3)) }, new LootCondition[0], "saltmod:saltwort"));
			}
		} else if (event.getName().equals(LootTableList.CHESTS_STRONGHOLD_CORRIDOR)) {
			final LootPool pool2 = event.getTable().getPool("pool2");
			if (pool2 != null) {
				pool2.addEntry(new LootEntryItem(SaltItems.SALT, 5, 0, new LootFunction[] { new SetCount(new LootCondition[0], new RandomValueRange(2, 5)) }, new LootCondition[0], "saltmod:salt"));
				pool2.addEntry(new LootEntryItem(SaltItems.SALTWORT_SEED, 5, 0, new LootFunction[] { new SetCount(new LootCondition[0], new RandomValueRange(2, 5)) }, new LootCondition[0], "saltmod:saltwort"));
			}
		} else if (event.getName().equals(LootTableList.CHESTS_ABANDONED_MINESHAFT)) {
			final LootPool pool2 = event.getTable().getPool("pool2");
			if (pool2 != null) {
				pool2.addEntry(new LootEntryItem(SaltItems.SALT, 10, 0, new LootFunction[] { new SetCount(new LootCondition[0], new RandomValueRange(2, 5)) }, new LootCondition[0], "saltmod:salt"));
			}
		} else if (event.getName().equals(LootTableList.CHESTS_VILLAGE_BLACKSMITH)) {
			final LootPool pool2 = event.getTable().getPool("pool2");
			if (pool2 != null) {
				pool2.addEntry(new LootEntryItem(SaltItems.SALT, 10, 0, new LootFunction[] { new SetCount(new LootCondition[0], new RandomValueRange(2, 5)) }, new LootCondition[0], "saltmod:salt"));
			}
		} else if (event.getName().equals(LootTableList.CHESTS_IGLOO_CHEST)) {
			final LootPool pool2 = event.getTable().getPool("pool2");
			if (pool2 != null) {
				pool2.addEntry(new LootEntryItem(SaltItems.SALT, 5, 0, new LootFunction[] { new SetCount(new LootCondition[0], new RandomValueRange(2, 5)) }, new LootCondition[0], "saltmod:salt"));
			}
		} else if (event.getName().equals(LootTableList.CHESTS_DESERT_PYRAMID)) {
			final LootPool pool2 = event.getTable().getPool("pool2");
			if (pool2 != null) {
				pool2.addEntry(new LootEntryItem(SaltItems.SALTWORT_SEED, 5, 0, new LootFunction[] { new SetCount(new LootCondition[0], new RandomValueRange(2, 3)) }, new LootCondition[0], "saltmod:saltwort"));
			}
		} else if (event.getName().equals(LootTableList.CHESTS_JUNGLE_TEMPLE)) {
			final LootPool pool2 = event.getTable().getPool("pool2");
			if (pool2 != null) {
				pool2.addEntry(new LootEntryItem(SaltItems.SALTWORT_SEED, 10, 0, new LootFunction[] { new SetCount(new LootCondition[0], new RandomValueRange(2, 5)) }, new LootCondition[0], "saltmod:saltwort"));
			}
		}
	}
}