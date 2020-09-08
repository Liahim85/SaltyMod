package ru.liahim.saltmod.advancement.criterion;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import ru.liahim.saltmod.advancement.BaseCriterionTrigger;
import ru.liahim.saltmod.advancement.ICriterionInstanceTestable;

public class ItemStackTrigger extends BaseCriterionTrigger<ItemStack, ItemStackTrigger.Instance> {

	public ItemStackTrigger(ResourceLocation res) {
		super(res);
	}

	@Override
	public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
		return new Instance(getId(), ItemPredicate.deserialize(json));
	}

	public static class Instance extends CriterionInstance implements ICriterionInstanceTestable<ItemStack> {

		private final ItemPredicate itemPredicate;

		public Instance(ResourceLocation criterionIn, ItemPredicate itemPredicate) {
			super(criterionIn);
			this.itemPredicate = itemPredicate;
		}

		@Override
		public boolean test(ServerPlayerEntity player, ItemStack stack) {
			return this.itemPredicate.test(stack);
		}
	}
}