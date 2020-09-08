package ru.liahim.saltmod.item.crafting;

import java.util.Iterator;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fluids.FluidStack;

public class IngredientFluid implements Predicate<FluidStack> {

	private final FluidStack fluid;

	public IngredientFluid(FluidStack fluid) {
		this.fluid = fluid;
	}

	@Override
	public boolean test(FluidStack fluid) {
		return this.fluid.isFluidEqual(fluid);
	}

	public FluidStack getFluid() {
		return fluid;
	}

	public final void write(PacketBuffer buffer) {
		this.fluid.writeToPacket(buffer);
	}

	public static IngredientFluid read(PacketBuffer buffer) {
		return new IngredientFluid(FluidStack.readFromPacket(buffer));
	}

	@SuppressWarnings("deprecation")
	public static IngredientFluid deserialize(@Nullable JsonElement json) {
		if (json == null || json.isJsonNull()) throw new JsonSyntaxException("Item cannot be null");
		if (!json.isJsonObject()) throw new JsonSyntaxException("Expected fluid to be an object");
        JsonObject obj = (JsonObject)json;
        String name = JSONUtils.getString(obj, "fluid", "");
        if (!name.isEmpty()) {
            int amount = JSONUtils.getInt(obj, "amount", 1000);
        	if (name.contains(":")) {
	        	ResourceLocation res = new ResourceLocation(name);
		        Fluid fluid = Registry.FLUID.getValue(res).orElseThrow(() -> {
		        	return new JsonSyntaxException("Unknown fluid '" + res + "'");
		        });
	    		return new IngredientFluid(new FluidStack(fluid, amount));
        	} else {
            	Fluid fluid;
				Iterator<Fluid> itr = Registry.FLUID.iterator();
            	while (itr.hasNext()) {
            		fluid = itr.next();
            		if (fluid.getRegistryName().getPath().equals(name)) {
            			return new IngredientFluid(new FluidStack(fluid, amount));
        			}
            	}
            	throw new com.google.gson.JsonSyntaxException("Unknown fluid '" + name + "'");
        	}
        }
		return null;
	}
}