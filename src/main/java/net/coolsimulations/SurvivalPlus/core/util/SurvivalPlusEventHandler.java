package net.coolsimulations.SurvivalPlus.core.util;

import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import net.coolsimulations.SurvivalPlus.api.SPCompatibilityManager;
import net.coolsimulations.SurvivalPlus.api.SPConfig;
import net.coolsimulations.SurvivalPlus.api.SPItems;
import net.coolsimulations.SurvivalPlus.api.SPReference;
import net.coolsimulations.SurvivalPlus.api.item.SPItemIngot;
import net.coolsimulations.SurvivalPlus.core.SurvivalPlus;
import net.coolsimulations.SurvivalPlus.core.config.SurvivalPlusConfig;
import net.coolsimulations.SurvivalPlus.core.init.SurvivalPlusArmor;
import net.coolsimulations.SurvivalPlus.core.init.SurvivalPlusBlocks;
import net.coolsimulations.SurvivalPlus.core.init.SurvivalPlusFood;
import net.coolsimulations.SurvivalPlus.core.init.SurvivalPlusItems;
import net.coolsimulations.SurvivalPlus.core.init.SurvivalPlusTools;
import net.coolsimulations.SurvivalPlus.core.recipes.SPShieldRecipes;
import net.insane96mcp.carbonado.lib.Properties;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import thedarkcolour.futuremc.block.villagepillage.CampfireBlock;
import thedarkcolour.futuremc.config.FConfig;
import thedarkcolour.futuremc.registry.FSounds;

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

		AdvancementManager manager = player.getServer().getAdvancementManager();
		Advancement install = manager.getAdvancement(new ResourceLocation(SPReference.MOD_ID, SPReference.MOD_ID + "/install"));

		boolean isDone = false;

		Timer timer = new Timer();

		if(install !=null && player.getAdvancements().getProgress(install).hasProgress()) {
			isDone = true;
		}

		if(!entityData.getBoolean("sp.firstJoin") && !isDone && !SPConfig.disableThanks) {

			entityData.setBoolean("sp.firstJoin", true); 

			if(!player.world.isRemote) {

				TextComponentTranslation installInfo = new TextComponentTranslation("advancements.sp.install.display1");
				installInfo.getStyle().setColor(TextFormatting.GOLD);
				installInfo.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentTranslation("advancements.sp.install.display2")));
				installInfo.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://curseforge.com/minecraft/mc-mods/survivalplus"));
				player.sendMessage(installInfo);

				TextComponentTranslation discord = new TextComponentTranslation("sp.discord.display1");
				discord.getStyle().setColor(TextFormatting.DARK_GREEN);
				discord.getStyle().setBold(true);

				for(int i = 0; i < SPReference.MOD_ADDON_NAMES.size(); i++) {
					String name = I18n.translateToLocal(SPReference.MOD_ADDON_NAMES.get(i));

					TextComponentString formatted = new TextComponentString(name);
					formatted.getStyle().setColor(TextFormatting.BLUE);
					formatted.getStyle().setBold(true);

					TextComponentString gap = new TextComponentString(", ");
					gap.getStyle().setColor(TextFormatting.WHITE);

					discord.appendSibling(formatted);
					if(i + 1 != SPReference.MOD_ADDON_NAMES.size()) {
						discord.appendSibling(gap);
					}
				}
				discord.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentTranslation("sp.discord.display2")));
				discord.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/7DDsHfQ"));

				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						player.sendMessage(discord);
					}
				}, 30000);
			}
		}

		if(SurvivalPlusUpdateHandler.isOld == true && SPConfig.disableUpdateCheck == false) {
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					player.sendMessage(SurvivalPlusUpdateHandler.updateInfo);
					player.sendMessage(SurvivalPlusUpdateHandler.updateVersionInfo);
				}
			}, 15000);
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
	public void tripWireBreak(BreakEvent event) {

		Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
		IBlockState state = event.getWorld().getBlockState(event.getPos());

		EntityPlayer entityplayer = event.getPlayer();

		if (block instanceof BlockTripWire && !event.getWorld().isRemote && !entityplayer.getHeldItemMainhand().isEmpty() && entityplayer.getHeldItemMainhand().getItem() instanceof ItemShears && entityplayer.getHeldItemMainhand().getItem() != Items.SHEARS) {
			event.getWorld().setBlockState(event.getPos(), state.withProperty(BlockTripWire.DISARMED, Boolean.valueOf(true)), 4);
		}
	}

	@SubscribeEvent
	public void registerRecipes(RegistryEvent.Register<IRecipe> event)
	{
		IForgeRegistryModifiable modRegistry = (IForgeRegistryModifiable) event.getRegistry();

		event.getRegistry().register(new SPShieldRecipes.Decoration().setRegistryName(SPReference.MOD_ID, "SPShieldDecoration"));

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

		if(SPCompatibilityManager.isHammerTimeLoaded()) {
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.HAMMER_TIME_MODID + ":" + "itemaxediamond"));
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.HAMMER_TIME_MODID + ":" + "itemaxegold"));
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.HAMMER_TIME_MODID + ":" + "itemaxeiron"));
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.HAMMER_TIME_MODID + ":" + "itemaxestone"));
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.HAMMER_TIME_MODID + ":" + "itemaxewood"));
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.HAMMER_TIME_MODID + ":" + "itemhammerdiamond"));
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.HAMMER_TIME_MODID + ":" + "itemhammergold"));
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.HAMMER_TIME_MODID + ":" + "itemhammeriron"));
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.HAMMER_TIME_MODID + ":" + "itemhammerstone"));
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.HAMMER_TIME_MODID + ":" + "itemhammerwood"));
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.HAMMER_TIME_MODID + ":" + "itemshoveldiamond"));
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.HAMMER_TIME_MODID + ":" + "itemshovelgold"));
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.HAMMER_TIME_MODID + ":" + "itemshoveliron"));
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.HAMMER_TIME_MODID + ":" + "itemshovelstone"));
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.HAMMER_TIME_MODID + ":" + "itemshovelwood"));
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.HAMMER_TIME_MODID + ":" + "itemsicklediamond"));
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.HAMMER_TIME_MODID + ":" + "itemsicklegold"));
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.HAMMER_TIME_MODID + ":" + "itemsickleiron"));
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.HAMMER_TIME_MODID + ":" + "itemsicklestone"));
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.HAMMER_TIME_MODID + ":" + "itemsicklewood"));
		}

		if(SPCompatibilityManager.isLumberjackLoaded() && SPCompatibilityManager.isCarbonadoLoaded()) {
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.LUMBERJACK_MODID + ":" + "carbonado"));
		}

		if(SPCompatibilityManager.isLumberjackLoaded() && SPCompatibilityManager.isNoTreePunchingLoaded()) {
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.LUMBERJACK_MODID + ":" + "wood"));
		}

		if(SPCompatibilityManager.isFutureMCLoaded()) {
			modRegistry.remove(new ResourceLocation(SPReference.MOD_ID + ":" + "campfire"));
			if(SPCompatibilityManager.isIc2Loaded()) {
				modRegistry.remove(new ResourceLocation(SPReference.MOD_ID + ":" + "campfire_rubber"));
				modRegistry.remove(new ResourceLocation(SPReference.MOD_ID + ":" + "campfire_rubber_alt1"));
				modRegistry.remove(new ResourceLocation(SPReference.MOD_ID + ":" + "campfire_rubber_alt2"));
				modRegistry.remove(new ResourceLocation(SPReference.MOD_ID + ":" + "campfire_rubber_alt3"));
				modRegistry.remove(new ResourceLocation(SPReference.MOD_ID + ":" + "campfire_rubber_alt4"));
				modRegistry.remove(new ResourceLocation(SPReference.MOD_ID + ":" + "campfire_rubber_alt5"));
				modRegistry.remove(new ResourceLocation(SPReference.MOD_ID + ":" + "campfire_rubber_alt6"));
			}
		}

		if(SPCompatibilityManager.isBambooziedLoaded()) {
			modRegistry.remove(new ResourceLocation(SPCompatibilityManager.BAMBOOZIED_MODID + ":" + "bamboo_bundle"));
		}

	}

	@SubscribeEvent
	public void anvilRecipe(AnvilUpdateEvent event) {
		if(SPCompatibilityManager.isLumberjackLoaded() && SPCompatibilityManager.isCarbonadoLoaded()) {

			ItemStack left = event.getLeft();
			ItemStack right = event.getRight();
			ItemStack output = null;
			int carbonadoAmount = 4;

			if (Properties.config.tools.enableAnvilCrafting && left.isItemEqualIgnoreDurability(new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(SPCompatibilityManager.LUMBERJACK_MODID, "diamond_lumberaxe")))) && right.getItem().equals(net.insane96mcp.carbonado.init.ModItems.carbonadoItem) && right.getCount() >= carbonadoAmount) {

				output = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(SPCompatibilityManager.LUMBERJACK_MODID, "carbonado_lumberaxe")));
				NBTTagCompound tags = left.getTagCompound();
				output.setTagCompound(tags);
				event.setOutput(output);
				event.setMaterialCost(carbonadoAmount);

				int cost = 0;
				Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(left);
				for (Enchantment enchantment : enchantments.keySet()) {
					int lvl = enchantments.get(enchantment);
					int baseCost = 0;
					switch (enchantment.getRarity())
					{
					case COMMON:
						baseCost = 1;
						break;
					case UNCOMMON:
						baseCost = 2;
						break;
					case RARE:
						baseCost = 4;
						break;
					case VERY_RARE:
						baseCost = 8;
					}
					cost += baseCost * lvl;
				}
				cost *= 0.5f;
				event.setCost(MathHelper.clamp(cost, 1, 39));
			}
		}
	}

	@SubscribeEvent
	public void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
		IBlockState state = event.getWorld().getBlockState(event.getPos());

		EntityPlayer entityplayer = event.getEntityPlayer();
		ItemStack itemStackIn = entityplayer.getHeldItem(event.getHand());
		Item item = itemStackIn.getItem();

		if(SPCompatibilityManager.isFutureMCLoaded() && block instanceof CampfireBlock) {
			PropertyBool LIT = ObfuscationReflectionHelper.getPrivateValue(CampfireBlock.class, (CampfireBlock) block, "LIT");
			PropertyBool SIGNAL = ObfuscationReflectionHelper.getPrivateValue(CampfireBlock.class, (CampfireBlock) block, "SIGNAL");
			if(state.getValue(LIT) && item == Items.BUCKET  && !entityplayer.isCreative()) {
				event.getWorld().setBlockState(event.getPos(), state.withProperty(LIT, false));
				if (event.getWorld().isRemote) {
					for (int i = 0; i < 20; ++i) {
						CampfireBlock.Companion.spawnSmokeParticles(event.getWorld(), event.getPos(), (Boolean) state.getValue(SIGNAL), true);
					}
				} else {
					event.getWorld().playSound((EntityPlayer) null, event.getPos(), SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
				if(itemStackIn.getCount() == 1) {
					if (ItemStack.areItemStacksEqual(entityplayer.getHeldItemOffhand(), itemStackIn))
					{
						entityplayer.setHeldItem(EnumHand.OFF_HAND, new ItemStack(SPItems.charcoal_bucket));
					}
					else
					{
						entityplayer.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(SPItems.charcoal_bucket));
					}
				} else  if(itemStackIn.getCount() >= 2){
					itemStackIn.shrink(1);
					boolean flag = entityplayer.inventory.addItemStackToInventory(new ItemStack(SPItems.charcoal_bucket));
					if(!flag) {
						entityplayer.dropItem(new ItemStack(SPItems.charcoal_bucket), false);
					}		
				}
			}
		}

		if(block == Blocks.CAKE) {
			
			if((entityplayer.getHeldItemMainhand().getItem() == SPItems.paper_cup || entityplayer.getHeldItemOffhand().getItem() == SPItems.paper_cup) || entityplayer.getHeldItemMainhand().getItem() == SPItems.cupcake || entityplayer.getHeldItemOffhand().getItem() == SPItems.cupcake) {
				event.setCanceled(true);
			}
			
			if(entityplayer.getHeldItem(event.getHand()).getItem() == SPItems.paper_cup) {

				if(!event.getWorld().isRemote) {

					int i = ((Integer)state.getValue(BlockCake.BITES)).intValue();

					if (i < 6)
					{
						event.getWorld().setBlockState(event.getPos(), state.withProperty(BlockCake.BITES, Integer.valueOf(i + 1)), 3);
					}
					else
					{
						event.getWorld().setBlockToAir(event.getPos());
					}

					if(!entityplayer.capabilities.isCreativeMode) {
						if(itemStackIn.getCount() == 1) {
							if (ItemStack.areItemStacksEqual(entityplayer.getHeldItemOffhand(), itemStackIn))
							{
								entityplayer.setHeldItem(EnumHand.OFF_HAND, new ItemStack(SPItems.cupcake));
							}
							else
							{
								entityplayer.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(SPItems.cupcake));
							}
						} else  if(itemStackIn.getCount() >= 2){
							itemStackIn.shrink(1);
							boolean flag = entityplayer.inventory.addItemStackToInventory(new ItemStack(SPItems.cupcake));
							if(!flag) {
								entityplayer.dropItem(new ItemStack(SPItems.cupcake), false);
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void golemHealth(EntityInteract event) {
		ItemStack itemstack = event.getItemStack();
		Item item = itemstack.getItem();
		if(SPCompatibilityManager.isFutureMCLoaded() && FConfig.INSTANCE.getBuzzyBees().ironGolem.ironBarHealing && event.getTarget() instanceof EntityIronGolem) {
			if (((EntityLiving) event.getTarget()).getHealth() < ((EntityLiving) event.getTarget()).getMaxHealth()) {
				if(item instanceof SPItemIngot && ((SPItemIngot) item).healsGolem()) {
					Random rand = ((EntityLivingBase) event.getTarget()).getRNG();
					((EntityLiving) event.getTarget()).heal(((SPItemIngot) item).getGolemHealth());
					float f1 = 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.2F;
					((EntityLiving) event.getTarget()).playSound(FSounds.INSTANCE.getIRON_GOLEM_REPAIR(), 1.0F, f1);
					if (!event.getEntityPlayer().isCreative()) {
						itemstack.shrink(1);
					}
				}
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


	@SubscribeEvent
	public void coolsimChat(ServerChatEvent event) {

		TextComponentTranslation coolsim = new TextComponentTranslation("sp.coolsim.creator");
		coolsim.getStyle().setColor(TextFormatting.GOLD);

		if(event.getPlayer().getPersistentID().equals(UUID.fromString("54481257-7b6d-4c8e-8aac-ca6f864e1412"))) {
			if(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(event.getUsername()) != null)
				event.setComponent(new TextComponentString(coolsim.getFormattedText() + " <" + FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(event.getUsername()).getDisplayName().getFormattedText() + "> " + event.getMessage()));
		}
	}

	@SubscribeEvent
	public void coolsimReceivedChat(ClientChatReceivedEvent event) {

		TextComponentTranslation coolsim = new TextComponentTranslation("sp.coolsim.creator");
		coolsim.getStyle().setColor(TextFormatting.GOLD);

		TextComponentTranslation playerJoined = new TextComponentTranslation("multiplayer.player.joined", new Object[] {"coolsim"});

		TextComponentTranslation playerLeft = new TextComponentTranslation("multiplayer.player.left", new Object[] {"coolsim"});
		
		String coolsimFormatted = "coolsim";
		
		if(event.getMessage().getFormattedText().contains("coolsim")) {
			int index = event.getMessage().getFormattedText().indexOf("coolsim");
			coolsimFormatted = event.getMessage().getFormattedText().substring(index - 2, index + 9);
		}
		
		TextComponentTranslation coolsimJoined = new TextComponentTranslation("sp.coolsim.joined", new Object[] {coolsimFormatted});
		coolsimJoined.getStyle().setColor(TextFormatting.YELLOW);
		
		TextComponentTranslation coolsimLeft = new TextComponentTranslation("sp.coolsim.left", new Object[] {coolsimFormatted});
		coolsimLeft.getStyle().setColor(TextFormatting.YELLOW);

		if(replaceFormattingCodes(event.getMessage()).equals(playerJoined.getUnformattedText())) {
			event.setMessage(coolsimJoined);
		}

		if(replaceFormattingCodes(event.getMessage()).equals(playerLeft.getUnformattedText())) {
			event.setMessage(coolsimLeft);
		}

		if(replaceFormattingCodes(event.getMessage()).startsWith("[coolsim]")) {
			event.setMessage(new TextComponentString(event.getMessage().getFormattedText().replaceFirst("\\[", coolsim.getFormattedText() + " [")));
		}
	}

	@SubscribeEvent
	public void coolsimDeath(LivingDeathEvent event) {

		if(event.getEntity() instanceof EntityPlayer && event.getEntity().getPersistentID().equals(UUID.fromString("54481257-7b6d-4c8e-8aac-ca6f864e1412")) && event.getSource().getTrueSource() instanceof EntityPlayer) {

			EntityPlayerMP attacker = (EntityPlayerMP) event.getSource().getTrueSource();
			ItemStack coolsimHead = getcoolsimHead();

			if(coolsimHead != null) {
				dropItem(coolsimHead, attacker);
			} else {
				TextComponentTranslation error = new TextComponentTranslation("sp.coolsim.error");
				error.getStyle().setColor(TextFormatting.RED);
				attacker.sendMessage(error);
			}
		}

	}

	public static ItemStack getcoolsimHead()
	{
		String texture = "eyJ0aW1lc3RhbXAiOjE1NzYxMTM5OTc5ODUsInByb2ZpbGVJZCI6IjU0NDgxMjU3N2I2ZDRjOGU4YWFjY2E2Zjg2NGUxNDEyIiwicHJvZmlsZU5hbWUiOiJjb29sc2ltIiwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzdmMDkwM2QxOGMyZTE4YmQzYzBiMDk5YmIzZGFkNmVjYTQ2ZDBjMzdkZjJkM2FlMjljYzAwOWYwN2I5OTM3NmYifX19";
		String id = new UUID(texture.hashCode(), texture.hashCode()).toString();

		ItemStack playerhead = new ItemStack(Items.SKULL, 1, 3);

		TextComponentTranslation headName = new TextComponentTranslation("item.skull.player.name", new Object[] {"coolsim"});
		headName.getStyle().setItalic(true);
		NBTTagCompound skullOwner = new NBTTagCompound();
		skullOwner.setString("Id", id);
		NBTTagCompound properties = new NBTTagCompound();
		NBTTagList textures = new NBTTagList();
		NBTTagCompound tex = new NBTTagCompound();
		tex.setString("Value", texture);
		textures.appendTag(tex);
		properties.setTag("textures", textures);
		skullOwner.setTag("Properties", properties);
		playerhead.setTagInfo("SkullOwner", skullOwner);
		playerhead.setStackDisplayName(headName.getFormattedText());

		return playerhead;
	}

	public static void dropItem(ItemStack stack, EntityPlayer player) {

		boolean bl = player.inventory.addItemStackToInventory(stack);
		EntityItem itemEntity;
		if (bl && stack.isEmpty()) {
			stack.setCount(1);
			itemEntity = player.dropItem(stack, false);
			if (itemEntity != null) {
				itemEntity.makeFakeItem();
			}

			player.world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
			player.inventoryContainer.detectAndSendChanges();
		} else {
			itemEntity = player.dropItem(stack, false);
			if (itemEntity != null) {
				itemEntity.setNoPickupDelay();
				itemEntity.setOwner(player.getName());
			}
		}
	}

	public static String replaceFormattingCodes(ITextComponent component) {

		String text = component.getUnformattedText();

		if(text.contains("§")) {
			System.out.println(text);
			for(int i = 0; i <= StringUtils.countMatches(text, "§"); i++) {
				text = text.substring(0, text.indexOf("§")) + text.substring(text.indexOf("§") + 2);
			}
		}

		return text;
	}
}
