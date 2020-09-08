package ru.liahim.saltmod.init;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import ru.liahim.saltmod.SaltyMod;
import ru.liahim.saltmod.tileentity.EvaporatorTileEntity;

public class ModTileEntities {

	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, SaltyMod.MODID);

	public static final RegistryObject<TileEntityType<EvaporatorTileEntity>> EVAPORATOR = TILE_ENTITIES.register("evaporator", () -> TileEntityType.Builder.create(EvaporatorTileEntity::new, ModBlocks.EVAPORATOR.get()).build(null));
}