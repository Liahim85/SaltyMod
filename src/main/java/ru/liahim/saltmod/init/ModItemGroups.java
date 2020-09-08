package ru.liahim.saltmod.init;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import ru.liahim.saltmod.SaltyMod;

public class ModItemGroups {

	public static final ItemGroup MOD_ITEM_GROUP = new ModItemGroup(SaltyMod.MODID, () -> new ItemStack(ModBlocks.SALT_ORE.get()));

	public static final class ModItemGroup extends ItemGroup {

		@Nonnull
		private final Supplier<ItemStack> iconSupplier;

		public ModItemGroup(@Nonnull final String name, @Nonnull final Supplier<ItemStack> iconSupplier) {
			super(name);
			this.iconSupplier = iconSupplier;
		}

		@Override
		@Nonnull
		public ItemStack createIcon() {
			return iconSupplier.get();
		}
	}
}