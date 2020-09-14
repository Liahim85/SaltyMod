package ru.liahim.saltmod.init;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import ru.liahim.saltmod.SaltyMod;

public class ModItemGroups {

	public static final ItemGroup MOD_ITEM_GROUP = new ModItemGroup(SaltyMod.MODID, () -> new ItemStack(ModBlocks.SALT_ORE.get()));

	public static final class ModItemGroup extends ItemGroup {

		@Nonnull
		private final Supplier<ItemStack> iconSupplier;
		private static final List<Item> itemList = Lists.newArrayList();

		public ModItemGroup(@Nonnull final String name, @Nonnull final Supplier<ItemStack> iconSupplier) {
			super(name);
			this.iconSupplier = iconSupplier;
		}

		@Override
		@Nonnull
		public ItemStack createIcon() {
			return iconSupplier.get();
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public void fill(NonNullList<ItemStack> items) {
			itemList.clear();
			for (Item item : Registry.ITEM) {
				if (item instanceof BlockItem) item.fillItemGroup(this, items);
				else itemList.add(item);
			}
			for (Item item : itemList) {
				item.fillItemGroup(this, items);
			}
		}
	}
}