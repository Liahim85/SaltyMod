package ru.liahim.saltmod.init;

import java.util.function.Supplier;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum ModArmorMaterials implements IArmorMaterial {

	MUD_MATERIAL("saltmod:mud_material", 4, new int[] { 1, 1, 1, 1 }, 15, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0, () -> Ingredient.fromItems(ModItems.MINERAL_MUD));

	private static final int[] MAX_DAMAGE_ARRAY = new int[] { 13, 15, 16, 11 };
	private final String name;
	private final int durability;
	private final int[] damageReduction;
	private final int enchantability;
	private final SoundEvent equipSound;
	private final float toughness;
	private final Supplier<Ingredient> repairMaterial;

	ModArmorMaterials(String name, int durability, int damageReduction[], int enchantability, SoundEvent sound, float toughness, Supplier<Ingredient> repairMaterial) {
		this.name = name;
		this.durability = durability;
		this.damageReduction = damageReduction;
		this.enchantability = enchantability;
		this.equipSound = sound;
		this.toughness = toughness;
		this.repairMaterial = repairMaterial;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getDurability(EquipmentSlotType slot) {
		return MAX_DAMAGE_ARRAY[slot.getIndex()] * durability;
	}

	@Override
	public int getDamageReductionAmount(EquipmentSlotType slot) {
		return damageReduction[slot.getIndex()];
	}

	@Override
	public int getEnchantability() {
		return enchantability;
	}

	@Override
	public SoundEvent getSoundEvent() {
		return equipSound;
	}

	@Override
	public float getToughness() {
		return toughness;
	}

	@Override
	public Ingredient getRepairMaterial() {
		return repairMaterial.get();
	}
}