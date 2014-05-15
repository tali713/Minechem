package pixlepix.minechem.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;

import org.modstats.ModstatInfo;
import org.modstats.Modstats;

import pixlepix.minechem.client.TickHandler;
import pixlepix.minechem.client.gui.GuiHandler;
import pixlepix.minechem.client.gui.tabs.*;
import pixlepix.minechem.common.blueprint.MinechemBlueprint;
import pixlepix.minechem.common.coating.CoatingRecipe;
import pixlepix.minechem.common.coating.CoatingSubscribe;
import pixlepix.minechem.common.coating.EnchantmentCoated;
import pixlepix.minechem.common.network.PacketHandler;
import pixlepix.minechem.common.polytool.PolytoolEventHandler;
import pixlepix.minechem.common.recipe.MinechemRecipes;
import pixlepix.minechem.common.utils.ConstantValue;
import pixlepix.minechem.computercraft.ICCMain;
import pixlepix.minechem.fluid.FluidHelper;

import java.util.logging.Logger;

@Mod(modid = ModMinechem.ID, name = ModMinechem.NAME, version = "5.0.3", useMetadata = false, acceptedMinecraftVersions = "[1.6.4,)", dependencies = "required-after:Forge@[9.11.1.953,);after:BuildCraft|Energy;after:factorization;after:IC2;after:Railcraft;after:ThermalExpansion")
@ModstatInfo(prefix = ModMinechem.ID)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { ModMinechem.CHANNEL_NAME }, packetHandler = PacketHandler.class)
public class ModMinechem
{
    /** Internal mod name used for reference purposes and resource gathering. **/
    public static final String ID = "minechem";
    
    /** Network name that is used in the NetworkMod. **/
    public static final String CHANNEL_NAME = ID;
    
    /** User friendly version of our mods name. **/
    public static final String NAME = "MineChem";
    
    /** Reference to how many ticks make up a second in Minecraft. **/
    public static final int SECOND_IN_TICKS = 20;
    
    /** Provides logging **/
    @Instance(value = CHANNEL_NAME)
    public static ModMinechem INSTANCE;

    /** Provides standard logging from the Forge. **/
    public static Logger LOGGER;

    @SidedProxy(clientSide = "pixlepix.minechem.client.ClientProxy", serverSide = "pixlepix.minechem.common.CommonProxy")
    public static CommonProxy PROXY;
    
    /** Creative mode tab that shows up in Minecraft. **/
    public static CreativeTabs CREATIVE_TAB = new CreativeTabMinechem(ModMinechem.NAME);
    
    /** Provides standardized configuration file offered by the Forge. **/
    private static Configuration CONFIG;
    
    /** Custom block and item loader from what once was Particle Physics mod. **/
    public static BetterLoader particlePhysicsLoader;
    
    /** List of supported languages. **/
    private static final String[] LANGUAGES_SUPPORTED = new String[] { "en_US", "zh_CN", "de_DE" };
    
    public static final ResourceLocation ICON_ENERGY = new ResourceLocation(ModMinechem.ID, ConstantValue.ICON_BASE + "i_power.png");
    public static final ResourceLocation ICON_FULL_ENERGY = new ResourceLocation(ModMinechem.ID, ConstantValue.ICON_BASE + "i_fullEower.png");
    public static final ResourceLocation ICON_HELP = new ResourceLocation(ModMinechem.ID, ConstantValue.ICON_BASE + "i_help.png");
    public static final ResourceLocation ICON_JAMMED = new ResourceLocation(ModMinechem.ID, ConstantValue.ICON_BASE + "i_jammed.png");
    public static final ResourceLocation ICON_NO_BOTTLES = new ResourceLocation(ModMinechem.ID, ConstantValue.ICON_BASE + "i_noBottles.png");
    public static final ResourceLocation ICON_NO_RECIPE = new ResourceLocation(ModMinechem.ID, ConstantValue.ICON_BASE + "i_noRecipe.png");
    public static final ResourceLocation ICON_NO_ENERGY = new ResourceLocation(ModMinechem.ID, ConstantValue.ICON_BASE + "i_unpowered.png");

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        // Register instance.
        INSTANCE = this;
        
        // Setup logging.
        LOGGER = event.getModLog();
        LOGGER.setParent(FMLLog.getLogger());

        // Load configuration.
        LOGGER.info("Loading configuration...");
        CONFIG = new Configuration(event.getSuggestedConfigurationFile());
        Settings.load(CONFIG);

        // Register items and blocks.
        LOGGER.info("Registering Items...");
        MinechemItems.registerItems();
        
        LOGGER.info("Registering Blocks...");
        MinechemBlocks.registerBlocks();
        
        LOGGER.info("Registering Blueprints...");
        MinechemBlueprint.registerBlueprints();
        
        LOGGER.info("Registering Recipe Handlers...");
        MinechemRecipes.getInstance().RegisterHandlers();
        MinechemRecipes.getInstance().RegisterRecipes();
        MinechemRecipes.getInstance().registerFluidRecipies();
        
        LOGGER.info("Registering OreDict Compatability...");
        MinechemItems.registerToOreDictionary();

        LOGGER.info("Registering Minechem Recipes...");
        MinecraftForge.EVENT_BUS.register(MinechemRecipes.getInstance());
        
        LOGGER.info("Registering Chemical Effects...");
        MinecraftForge.EVENT_BUS.register(new CoatingSubscribe());
        
        LOGGER.info("Registering Polytool Event Handler...");
        MinecraftForge.EVENT_BUS.register(new PolytoolEventHandler());
        
        LOGGER.info("Populating Particle List...");
        ParticleRegistry.populateParticleList();
        
        LOGGER.info("Registering Particle Physics Entities...");
        ParticleRegistry.registerEntities();
        
        LOGGER.info("Creating Particle Physics blocks and items...");
        particlePhysicsLoader = new BetterLoader();
        particlePhysicsLoader.loadBlocks();
        particlePhysicsLoader.mainload();

        LOGGER.info("PREINT PASSED");
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        LOGGER.info("Registering Proxy Hooks...");
        PROXY.registerHooks();
        
        LOGGER.info("Activating Potion Injector...");
        PotionInjector.inject();
        
        LOGGER.info("Matching Pharmacology Effects to Chemicals...");
        CraftingManager.getInstance().getRecipeList().add(new CoatingRecipe());
        
        LOGGER.info("Registering fluids...");
        FluidHelper.registerFluids();
        
        LOGGER.info("Registering Ore Generation...");
        GameRegistry.registerWorldGenerator(new MinechemGeneration());
        
        LOGGER.info("Registering GUI and Container handlers...");
        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
        
        LOGGER.info("Register Tick Handler for chemical effects tracking...");
        TickRegistry.registerScheduledTickHandler(new ScheduledTickHandler(), Side.SERVER);
        
        LOGGER.info("Registering ClientProxy Rendering Hooks...");
        PROXY.registerRenderers();
        
        LOGGER.info("Registering ModStats Usage Tracking...");
        Modstats.instance().getReporter().registerMod(this);
        
        LOGGER.info("INIT PASSED");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        // Activate mod plugins.
        addonComputerCraft(event);
        addonDungeonLoot();

        LOGGER.info("Activating Chemical Effect Layering (Coatings)...");
        EnchantmentCoated.registerCoatings();

        LOGGER.info("POSTINIT PASSED");
    }

    private void addonDungeonLoot()
    {
        LOGGER.info("Adding rare chemicals to dungeon loot...");
        ChestGenHooks ChestProvider = ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST);
        ItemStack A = new ItemStack(MinechemItems.blueprint, 1, 0);
        ItemStack B = new ItemStack(MinechemItems.blueprint, 1, 1);
        ChestProvider.addItem(new WeightedRandomChestContent(A, 10, 80, 1));
        ChestProvider.addItem(new WeightedRandomChestContent(B, 10, 80, 1));
    }

    private void addonComputerCraft(FMLPostInitializationEvent event)
    {
        if (Loader.isModLoaded("ComputerCraft"))
        {
            LOGGER.info("Initilizing ComputerCraft Addon...");
            Object ccMain = event.buildSoftDependProxy("CCTurtle", "pixlepix.minechem.computercraft.CCMain");
            if (ccMain != null)
            {
                ICCMain iCCMain = (ICCMain) ccMain;
                iCCMain.loadConfig(CONFIG);
                iCCMain.init();
                LOGGER.info("ComputerCraft interface loaded!");
            }
            else
            {
                LOGGER.warning("Unable to load ComputerCraft interface.");
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void textureHook(IconRegister icon)
    {
        TabStateControl.unpoweredIcon = icon.registerIcon(ConstantValue.UNPOWERED_ICON);
        TabStateControlSynthesis.noRecipeIcon = icon.registerIcon(ConstantValue.NO_RECIPE_ICON);
        TabEnergy.powerIcon = icon.registerIcon(ConstantValue.POWER_ICON);
        TabHelp.helpIcon = icon.registerIcon(ConstantValue.HELP_ICON);
        TabTable.helpIcon = icon.registerIcon(ConstantValue.HELP_ICON);
        TabJournal.helpIcon = icon.registerIcon(ConstantValue.POWER_ICON);
    }

    @ForgeSubscribe
    public void onPreRender(RenderGameOverlayEvent.Pre e)
    {
        TickHandler.renderEffects();
    }
}
