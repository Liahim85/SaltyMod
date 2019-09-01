package ru.liahim.saltmod.api.advancement.criterion;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import ru.liahim.saltmod.api.advancement.BaseCriterionTrigger;
import ru.liahim.saltmod.api.advancement.ICriterionInstanceTestable;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemStackTrigger extends BaseCriterionTrigger<ItemStack, ItemStackTrigger.Instance> {

	public ItemStackTrigger(ResourceLocation res) {
		super(res);
	}

	@Override
	public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
		return new Instance(getId(), ItemPredicate.deserialize(json));
	}

	public static class Instance extends AbstractCriterionInstance implements ICriterionInstanceTestable<ItemStack> {

		private final ItemPredicate itemPredicate;

		public Instance(ResourceLocation criterionIn, ItemPredicate itemPredicate) {
			super(criterionIn);
			this.itemPredicate = itemPredicate;
		}

		@Override
		public boolean test(EntityPlayerMP player, ItemStack stack) {
			return this.itemPredicate.test(stack);
		}
	}
}