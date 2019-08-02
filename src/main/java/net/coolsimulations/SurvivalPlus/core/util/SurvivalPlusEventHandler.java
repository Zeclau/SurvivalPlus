package net.coolsimulations.SurvivalPlus.core.util;

import net.coolsimulations.SurvivalPlus.api.SPCompatibilityManager;
import net.coolsimulations.SurvivalPlus.api.SPConfig;
import net.coolsimulations.SurvivalPlus.api.SPReference;
import net.coolsimulations.SurvivalPlus.core.SurvivalPlus;
import net.coolsimulations.SurvivalPlus.core.config.SurvivalPlusConfig;
import net.coolsimulations.SurvivalPlus.core.init.SurvivalPlusArmor;
import net.coolsimulations.SurvivalPlus.core.init.SurvivalPlusBlocks;
import net.coolsimulations.SurvivalPlus.core.init.SurvivalPlusFood;
import net.coolsimulations.SurvivalPlus.core.init.SurvivalPlusItems;
import net.coolsimulations.SurvivalPlus.core.init.SurvivalPlusTools;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class SurvivalPlusEventHandler {
	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent event)
    {
        if (event.getModID().equals(SPReference.MOD_ID))
        {
            SurvivalPlusConfig.syncConfig(false);
        }
    }
	
	@SubscribeEvent
	public void onplayerLogin(PlayerLoggedInEvent event)
    {
		EntityPlayerMP player = (EntityPlayerMP) event.player;
		NBTTagCompound entityData = player.getEntityData();
		
		if(!entityData.getBoolean("sp.firstJoin")) {
			
			entityData.setBoolean("sp.firstJoin", true);
		
			if(!player.world.isRemote) {
        		
        		TextComponentTranslation installInfo = new TextComponentTranslation("advancements.sp.install.display1");
        		installInfo.getStyle().setColor(TextFormatting.GOLD);
        		installInfo.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentTranslation("advancements.sp.install.display2")));
            	installInfo.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://minecraft.curseforge/projects/survivalplus"));
				player.sendMessage(installInfo);
        		
        	}
		}
		
		if(SurvivalPlusUpdateHandler.isOld == true && SPConfig.disableUpdateCheck == false) {
        	player.sendMessage(SurvivalPlusUpdateHandler.updateInfo);
        }
    }
	
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event)
    {
		SurvivalPlusItems.registerItems(event.getRegistry());
		SurvivalPlusFood.registerItems(event.getRegistry());
		SurvivalPlusArmor.registerItems(event.getRegistry());
		SurvivalPlusTools.registerItems(event.getRegistry());
		SurvivalPlusOreDict.PreInit();
    }
	
	@SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event)
    {
        SurvivalPlusBlocks.registerBlocks(event.getRegistry());
        
    }
	
	@SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event)
    {
		IForgeRegistryModifiable modRegistry = (IForgeRegistryModifiable) event.getRegistry();
		
		if(!SPConfig.enableSponge) {
			modRegistry.remove(new ResourceLocation(SPReference.MOD_ID + ":" + "sponge"));
		}
		
		if(SPCompatibilityManager.isIc2Loaded()) {
			modRegistry.remove(new ResourceLocation(SPReference.MOD_ID + ":" + "bronze_ingot_alt2"));
		} else {
			modRegistry.remove(new ResourceLocation(SPReference.MOD_ID + ":" + "campfire_rubber"));
			modRegistry.remove(new ResourceLocation(SPReference.MOD_ID + ":" + "campfire_rubber_alt1"));
			modRegistry.remove(new ResourceLocation(SPReference.MOD_ID + ":" + "campfire_rubber_alt2"));
			modRegistry.remove(new ResourceLocation(SPReference.MOD_ID + ":" + "campfire_rubber_alt3"));
			modRegistry.remove(new ResourceLocation(SPReference.MOD_ID + ":" + "campfire_rubber_alt4"));
			modRegistry.remove(new ResourceLocation(SPReference.MOD_ID + ":" + "campfire_rubber_alt5"));
			modRegistry.remove(new ResourceLocation(SPReference.MOD_ID + ":" + "campfire_rubber_alt6"));
		}
		
		if(SPCompatibilityManager.isBopLoaded()) {
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.BOP_MODID + ":" + "white_dye_from_lily_of_the_valley"));
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.BOP_MODID + ":" + "white_dye_from_white_anemone"));
			if(!SPConfig.enableReplaceBOPRecipe) {
				modRegistry.remove(new ResourceLocation(SPReference.MOD_ID + ":" + "terrestrial_artifact"));
				modRegistry.remove(new ResourceLocation(SPReference.MOD_ID + ":" + "terrestrial_artifact_ore_gc"));
			} else {
				modRegistry.remove(new ResourceLocation(SPCompatibilityManager.BOP_MODID + ":" + "terrestrial_artifact"));
				modRegistry.remove(new ResourceLocation(SPReference.MOD_ID + ":" + "terrestrial_artifact_gc"));
			}
		}
        
    }
	
	@SubscribeEvent
	public void onModelRegistry(ModelRegistryEvent event)
    {
        for(Item item : SurvivalPlus.ITEMS) {
        	SurvivalPlusItems.registerRenders();
        }
        
        for(Item item : SurvivalPlus.ITEMS_FOOD) {
        	SurvivalPlusFood.registerRenders();
        }
        
        for(Item item : SurvivalPlus.ITEMS_ARMOR) {
        	SurvivalPlusArmor.registerRenders();
        }
        
        for(Item item : SurvivalPlus.ITEMS_TOOLS) {
        	SurvivalPlusTools.registerRenders();
        }
        
        for(Block block : SurvivalPlus.BLOCKS) {
        	SurvivalPlusBlocks.registerRenders();
        }
    }

}
