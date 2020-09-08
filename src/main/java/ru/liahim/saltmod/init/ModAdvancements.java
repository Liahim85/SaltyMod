package ru.liahim.saltmod.init;

import ru.liahim.saltmod.SaltyMod;
import ru.liahim.saltmod.advancement.criterion.ItemStackTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.util.ResourceLocation;

public class ModAdvancements {

	public static final ItemStackTrigger SALT_COMMON = CriteriaTriggers.register(new ItemStackTrigger(new ResourceLocation(SaltyMod.MODID, "salt_common")));

	public static void init() {}
}