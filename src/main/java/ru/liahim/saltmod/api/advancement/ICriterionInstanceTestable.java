package ru.liahim.saltmod.api.advancement;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;

public interface ICriterionInstanceTestable<D> extends ICriterionInstance {

	public boolean test(EntityPlayerMP player, D criterionData);
}