package ru.liahim.saltmod.proxy;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ClientProxy extends IProxy {

	public static final Set<Item> colorSet = Sets.newHashSet();
	public static final Map<ResourceLocation, Pair<String,Integer>> saltyFoods = Maps.newHashMap();

	@Override
	public void addToColorSet(Item item) {
		colorSet.add(item);
	}

	@Override
	public void registerSaltyFoodModel(ResourceLocation food, String prefix, int texture) {
		saltyFoods.put(food, Pair.of(prefix, texture));
	}
}