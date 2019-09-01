package ru.liahim.saltmod.init;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ru.liahim.saltmod.SaltyMod;
import ru.liahim.saltmod.api.advancement.criterion.ItemStackTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ModAdvancements {

	public static final ItemStackTrigger SALT_COMMON = registerCriteriaTrigger(new ItemStackTrigger(new ResourceLocation(SaltyMod.MODID, "salt_common")));

	public static void load() {}

	/**
	 * Call the private static `register` from @link{CriteriaTriggers}
	 * @param criterion The criterion.
	 * @param <T> The criterion type.
	 * @return The registered instance.
	 */
	private static <T extends ICriterionTrigger> T registerCriteriaTrigger(T criterion) {
		Method method = ReflectionHelper.findMethod(CriteriaTriggers.class,
				"register", "func_192118_a", ICriterionTrigger.class);
		try {
			return (T) method.invoke(null, criterion);
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}