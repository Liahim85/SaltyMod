package ru.liahim.saltmod.advancement;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.entity.player.ServerPlayerEntity;

public interface ICriterionInstanceTestable<D> extends ICriterionInstance {

	public boolean test(ServerPlayerEntity player, D criterionData);
}