package ru.liahim.saltmod.item;

import net.minecraft.item.Item;
import ru.liahim.saltmod.init.ModItemGroups;

public class SaltItem extends Item {

	public SaltItem(Properties properties) {
		super(properties.group(ModItemGroups.MOD_ITEM_GROUP));
	}

	public SaltItem() {
		this(new Item.Properties());
	}
}