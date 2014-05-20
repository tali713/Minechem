package minechem.network.client;

<<<<<<< HEAD:src/main/java/minechem/network/client/ClientProxy.java
import minechem.MinechemBlockGeneration;
import minechem.MinechemItemGeneration;
=======
import minechem.MinechemBlocksGeneration;
import minechem.MinechemItemsGeneration;
>>>>>>> MaxwolfRewrite:src/main/java/minechem/network/client/ClientProxy.java
import minechem.item.element.ItemElementRenderer;
import minechem.item.molecule.ItemMoleculeRenderer;
import minechem.network.server.CommonProxy;
import minechem.sound.MinechemSoundEvent;
<<<<<<< HEAD:src/main/java/minechem/network/client/ClientProxy.java
import minechem.tick.TickHandler;
=======
import minechem.tickhandler.TickHandler;
>>>>>>> MaxwolfRewrite:src/main/java/minechem/network/client/ClientProxy.java
import minechem.tileentity.blueprintprojector.ItemBlueprintProjectorRenderer;
import minechem.tileentity.blueprintprojector.TileEntityBlueprintProjector;
import minechem.tileentity.blueprintprojector.TileEntityBlueprintProjectorRenderer;
import minechem.tileentity.decomposer.ItemDecomposerRenderer;
import minechem.tileentity.decomposer.TileEntityDecomposer;
import minechem.tileentity.decomposer.TileEntityDecomposerRenderer;
import minechem.tileentity.leadedchest.ItemLeadedChestRenderer;
import minechem.tileentity.leadedchest.TileEntityLeadedChest;
import minechem.tileentity.leadedchest.TileEntityLeadedChestRenderer;
import minechem.tileentity.microscope.ItemMicroscopeRenderer;
import minechem.tileentity.microscope.TileEntityMicroscope;
import minechem.tileentity.microscope.TileEntityMicroscopeRenderer;
import minechem.tileentity.synthesis.ItemSynthesisRenderer;
import minechem.tileentity.synthesis.TileEntitySynthesis;
import minechem.tileentity.synthesis.TileEntitySynthesisRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenderers()
    {
        RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

<<<<<<< HEAD:src/main/java/minechem/network/client/ClientProxy.java
        MinecraftForgeClient.registerItemRenderer(MinechemItemGeneration.element.itemID, new ItemElementRenderer());
        MinecraftForgeClient.registerItemRenderer(MinechemItemGeneration.molecule.itemID, new ItemMoleculeRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.itemsList[MinechemBlockGeneration.microscope.blockID].itemID, new ItemMicroscopeRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.itemsList[MinechemBlockGeneration.decomposer.blockID].itemID, new ItemDecomposerRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.itemsList[MinechemBlockGeneration.synthesis.blockID].itemID, new ItemSynthesisRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.itemsList[MinechemBlockGeneration.blueprintProjector.blockID].itemID, new ItemBlueprintProjectorRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.itemsList[MinechemBlockGeneration.leadedChest.blockID].itemID, new ItemLeadedChestRenderer());
=======
        MinecraftForgeClient.registerItemRenderer(MinechemItemsGeneration.element.itemID, new ItemElementRenderer());
        MinecraftForgeClient.registerItemRenderer(MinechemItemsGeneration.molecule.itemID, new ItemMoleculeRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.itemsList[MinechemBlocksGeneration.microscope.blockID].itemID, new ItemMicroscopeRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.itemsList[MinechemBlocksGeneration.decomposer.blockID].itemID, new ItemDecomposerRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.itemsList[MinechemBlocksGeneration.synthesis.blockID].itemID, new ItemSynthesisRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.itemsList[MinechemBlocksGeneration.blueprintProjector.blockID].itemID, new ItemBlueprintProjectorRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.itemsList[MinechemBlocksGeneration.chemicalStorage.blockID].itemID, new ItemChemicalStorageRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.itemsList[MinechemBlocksGeneration.leadedChest.blockID].itemID, new ItemLeadedChestRenderer());
>>>>>>> MaxwolfRewrite:src/main/java/minechem/network/client/ClientProxy.java

        TickRegistry.registerTickHandler(new TickHandler(), Side.CLIENT);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMicroscope.class, new TileEntityMicroscopeRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDecomposer.class, new TileEntityDecomposerRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySynthesis.class, new TileEntitySynthesisRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBlueprintProjector.class, new TileEntityBlueprintProjectorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLeadedChest.class, new TileEntityLeadedChestRenderer());
    }

    @Override
    public void registerHooks()
    {
        MinecraftForge.EVENT_BUS.register(new MinechemSoundEvent());
    }

    @Override
    public World getClientWorld()
    {
        return FMLClientHandler.instance().getClient().theWorld;
    }

}
