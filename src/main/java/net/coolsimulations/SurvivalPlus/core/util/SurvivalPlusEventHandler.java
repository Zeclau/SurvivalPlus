package net.coolsimulations.SurvivalPlus.core.util;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.coolsimulations.SurvivalPlus.api.SPConfig;
import net.coolsimulations.SurvivalPlus.api.SPItems;
import net.coolsimulations.SurvivalPlus.api.SPReference;
import net.coolsimulations.SurvivalPlus.api.item.SPItemIngot;
import net.coolsimulations.SurvivalPlus.core.world.SurvivalPlusOreGenerator;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.block.TripWireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades.ITrade;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.BeehiveTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.BasicTrade;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.items.ItemHandlerHelper;

public class SurvivalPlusEventHandler {

	@SubscribeEvent
	public void onplayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
		CompoundNBT entityData = player.getPersistentData();

		AdvancementManager manager = player.getServer().getAdvancementManager();
		Advancement install = manager.getAdvancement(new ResourceLocation(SPReference.MOD_ID, SPReference.MOD_ID + "/install"));

		boolean isDone = false;

		Timer timer = new Timer();

		if(install !=null && player.getAdvancements().getProgress(install).hasProgress()) {
			isDone = true;
		}

		if(!entityData.getBoolean("sp.firstJoin") && !isDone && !SPConfig.disableThanks.get()) {

			entityData.putBoolean("sp.firstJoin", true);

			if(!player.world.isRemote) {

				TranslationTextComponent installInfo = new TranslationTextComponent("advancements.sp.install.display1");
				installInfo.func_240699_a_(TextFormatting.GOLD);
				player.func_241151_a_(installInfo.func_240700_a_((style) -> {return style.func_240716_a_(new HoverEvent(HoverEvent.Action.field_230550_a_, new TranslationTextComponent("advancements.sp.install.display2"))).func_240715_a_(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://curseforge.com/minecraft/mc-mods/survivalplus"));}), ChatType.SYSTEM, Util.field_240973_b_);

				TranslationTextComponent discord = new TranslationTextComponent("sp.discord.display1");
				discord.func_240699_a_(TextFormatting.DARK_GREEN);
				discord.func_240699_a_(TextFormatting.BOLD);

				for(int i = 0; i < SPReference.MOD_ADDON_NAMES.size(); i++) {
					String name = LanguageMap.getInstance().func_230503_a_(SPReference.MOD_ADDON_NAMES.get(i));

					StringTextComponent formatted = new StringTextComponent(name);
					formatted.func_240699_a_(TextFormatting.BLUE);
					formatted.func_240699_a_(TextFormatting.BOLD);

					StringTextComponent gap = new StringTextComponent(", ");
					gap.func_240699_a_(TextFormatting.WHITE);

					discord.func_230529_a_(formatted);
					if(i + 1 != SPReference.MOD_ADDON_NAMES.size()) {
						discord.func_230529_a_(gap);
					}
				}

				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						player.func_241151_a_(discord.func_240700_a_((style) -> {return style.func_240716_a_(new HoverEvent(HoverEvent.Action.field_230550_a_, new TranslationTextComponent("sp.discord.display2"))).func_240715_a_(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/7DDsHfQ"));}), ChatType.SYSTEM, Util.field_240973_b_);
					}
				}, 30000);
			}
		}

		if(SurvivalPlusUpdateHandler.isOld == true && SPConfig.disableUpdateCheck.get() == false) {
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					player.func_241151_a_(SurvivalPlusUpdateHandler.updateInfo.func_240700_a_((style) -> {return style.func_240716_a_(new HoverEvent(HoverEvent.Action.field_230550_a_, new TranslationTextComponent("sp.update.display2"))).func_240715_a_(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://curseforge.com/minecraft/mc-mods/survivalplus"));}), ChatType.SYSTEM, Util.field_240973_b_);
					player.func_241151_a_(SurvivalPlusUpdateHandler.updateVersionInfo.func_240700_a_((style) -> {return style.func_240716_a_(new HoverEvent(HoverEvent.Action.field_230550_a_, new TranslationTextComponent("sp.update.display2"))).func_240715_a_(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://curseforge.com/minecraft/mc-mods/survivalplus"));}), ChatType.SYSTEM, Util.field_240973_b_);
				}
			}, 15000);
		}
	}

	@SubscribeEvent
	public void villagerTrades(VillagerTradesEvent event) {
		Int2ObjectMap<List<ITrade>> trades = event.getTrades();

		if(event.getType() == VillagerProfession.BUTCHER) {
			trades.get(3).add(new BasicTrade(2, new ItemStack(SPItems.beef_pie, 4), 16, 20));
		}

		if(event.getType() == VillagerProfession.FARMER) {
			trades.get(1).add(new BasicTrade(1, new ItemStack(SPItems.onion_seeds, 4), 12, 2)); //temp till forge pull request #6142 is resolved
			trades.get(2).add(new BasicTrade(new ItemStack(SPItems.raw_onion, 13), new ItemStack(Items.EMERALD), 16, 5, 0.05F));
		}

		if(event.getType() == VillagerProfession.ARMORER) {
			trades.get(2).add(new BasicTrade(6, new ItemStack(SPItems.bronze_chestplate), 12, 5, 0.2F));
		}

		if(event.getType() == VillagerProfession.ARMORER  || event.getType() == VillagerProfession.WEAPONSMITH) {
			trades.get(2).add(new BasicTrade(new ItemStack(SPItems.bronze_ingot, 12), new ItemStack(Items.EMERALD), 12, 5, 0.05F));
			trades.get(2).add(new BasicTrade(new ItemStack(SPItems.titanium_ingot, 18), new ItemStack(Items.EMERALD), 12, 10, 0.05F));
		}

	}

	@SubscribeEvent
	public void villagerTrades(WandererTradesEvent event) {
		List<ITrade> trades = (List<ITrade>) event.getGenericTrades();

		trades.add(new BasicTrade(1, new ItemStack(SPItems.onion_seeds, 4), 12, 20));		
	}

	@SubscribeEvent
	public void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
		BlockState state = event.getWorld().getBlockState(event.getPos());

		PlayerEntity entityplayer = event.getPlayer();
		ItemStack itemStackIn = entityplayer.getHeldItem(event.getHand());
		Item item = itemStackIn.getItem();
		ItemStack itemStackIn1 = itemStackIn.copy();

		if(block instanceof CampfireBlock) {
			if(state.get(CampfireBlock.LIT) && item == Items.BUCKET  && !entityplayer.abilities.isCreativeMode) {
				event.getWorld().setBlockState(event.getPos(), state.with(CampfireBlock.LIT, false));
				if (event.getWorld().isRemote()) {
					for (int i = 0; i < 20; ++i) {
						CampfireBlock.spawnSmokeParticles(event.getWorld(), event.getPos(), (Boolean) state.get(CampfireBlock.SIGNAL_FIRE),true);
					}
				} else {
					event.getWorld().playSound((PlayerEntity) null, event.getPos(), SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
				if(itemStackIn.getCount() == 1) {
					if (ItemStack.areItemStacksEqual(entityplayer.getHeldItemOffhand(), itemStackIn))
					{
						entityplayer.setHeldItem(Hand.OFF_HAND, new ItemStack(SPItems.charcoal_bucket));
					}
					else
					{
						entityplayer.setHeldItem(Hand.MAIN_HAND, new ItemStack(SPItems.charcoal_bucket));
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

		if(block == Blocks.PUMPKIN) {

			if (item instanceof ShearsItem && item != Items.SHEARS) {
				if (!event.getWorld().isRemote) {
					Direction direction = event.getFace();
					Direction direction1 = direction.getAxis() == Direction.Axis.Y ? entityplayer.getHorizontalFacing().getOpposite() : direction;
					event.getWorld().playSound((PlayerEntity)null, event.getPos(), SoundEvents.BLOCK_PUMPKIN_CARVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
					event.getWorld().setBlockState(event.getPos(), Blocks.CARVED_PUMPKIN.getDefaultState().with(CarvedPumpkinBlock.FACING, direction1), 11);
					ItemEntity itementity = new ItemEntity(event.getWorld(), (double)event.getPos().getX() + 0.5D + (double)direction1.getXOffset() * 0.65D, (double)event.getPos().getY() + 0.1D, (double)event.getPos().getZ() + 0.5D + (double)direction1.getZOffset() * 0.65D, new ItemStack(Items.PUMPKIN_SEEDS, 4));
					itementity.setMotion(0.05D * (double)direction1.getXOffset() + event.getWorld().rand.nextDouble() * 0.02D, 0.05D, 0.05D * (double)direction1.getZOffset() + event.getWorld().rand.nextDouble() * 0.02D);
					event.getWorld().addEntity(itementity);
					itemStackIn.damageItem(1, entityplayer, (livingentity) -> {
						livingentity.sendBreakAnimation(event.getHand());
					});
				}
			}
		}

		if(block instanceof BeehiveBlock) {
			int level = state.get(BeehiveBlock.HONEY_LEVEL);
			boolean flag = false;

			if (level >= 5 && item instanceof ShearsItem && item != Items.SHEARS) {
				event.getWorld().playSound(entityplayer, entityplayer.getPosX(), entityplayer.getPosY(), entityplayer.getPosZ(), SoundEvents.BLOCK_BEEHIVE_SHEAR, SoundCategory.NEUTRAL, 1.0F, 1.0F);
				BeehiveBlock.dropHoneyComb(event.getWorld(), event.getPos());
				itemStackIn.damageItem(1, entityplayer, (livingentity) -> {livingentity.sendBreakAnimation(event.getHand());});
				flag = true;
			}

			if (flag) {
				if (!CampfireBlock.func_235474_a_(event.getWorld(), event.getPos())) {
					if (hasBees(event.getWorld(), event.getPos())) {
						angerNearbyBees(event.getWorld(), event.getPos());
					}

					((BeehiveBlock) block).takeHoney(event.getWorld(), state, event.getPos(), entityplayer, BeehiveTileEntity.State.EMERGENCY);
				} else {
					((BeehiveBlock) block).takeHoney(event.getWorld(), state, event.getPos());
				}
			}
		}

	}

	@SubscribeEvent
	public void tripWireBreak(BreakEvent event) {

		Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
		BlockState state = event.getWorld().getBlockState(event.getPos());

		PlayerEntity entityplayer = event.getPlayer();

		if (block instanceof TripWireBlock && !event.getWorld().isRemote() && !entityplayer.getHeldItemMainhand().isEmpty() && entityplayer.getHeldItemMainhand().getItem() instanceof ShearsItem && entityplayer.getHeldItemMainhand().getItem() != Items.SHEARS) {
			event.getWorld().setBlockState(event.getPos(), state.with(TripWireBlock.DISARMED, Boolean.valueOf(true)), 4);
		}
	}

	@SubscribeEvent
	public void golemHealth(EntityInteract event) {
		ItemStack itemstack = event.getItemStack();
		Item item = itemstack.getItem();
		Random rand = new Random();
		if(event.getTarget() instanceof IronGolemEntity) {
			if (((LivingEntity) event.getTarget()).getHealth() < ((LivingEntity) event.getTarget()).getMaxHealth()) {
				if(item instanceof SPItemIngot && ((SPItemIngot) item).healsGolem()) {
					((LivingEntity) event.getTarget()).heal(((SPItemIngot) item).getGolemHealth());
					float f1 = 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.2F;
					((LivingEntity) event.getTarget()).playSound(SoundEvents.field_226143_fP_, 1.0F, f1);
					if (!event.getPlayer().abilities.isCreativeMode) {
						itemstack.shrink(1);
					}
				}
			}
		}
	}

	@SubscribeEvent(priority =  EventPriority.HIGH)
	public void genOres(BiomeLoadingEvent event) {
		SurvivalPlusOreGenerator.generateOres(event.getName(), event.getClimate(), event.getCategory(), event.getDepth(), event.getScale(), event.getEffects(), event.getGeneration(), event.getSpawns());
	}

	/**@SubscribeEvent
    public static <T extends IForgeRegistryEntry<T>> void registerRecipes(RegistryEvent.Register<T> event)
    {
		IForgeRegistryModifiable modRegistry = (IForgeRegistryModifiable) event.getRegistry();
		final RecipeManager recipeManager = event.getServer().getRecipeManager();

		if(!SPConfig.enableSponge.get()) {
			modRegistry.remove(new ResourceLocation(SPReference.MOD_ID + ":" + "sponge"));
		}

		if(SPCompatibilityManager.isIc2Loaded()) {
			modRegistry.remove(new ResourceLocation(SPReference.MOD_ID + ":" + "bronze_ingot_alt2"));
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

    }**/

	@SubscribeEvent
	public void coolsimChat(ServerChatEvent event) {

		TranslationTextComponent coolsim = new TranslationTextComponent("sp.coolsim.creator");
		coolsim.func_240699_a_(TextFormatting.GOLD);

		if(event.getPlayer().getUniqueID().equals(UUID.fromString("54481257-7b6d-4c8e-8aac-ca6f864e1412"))) {
			if(ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUsername(event.getUsername()) != null)
				event.setComponent(new TranslationTextComponent("%s <%s> %s", new Object[] {coolsim, ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUsername(event.getUsername()).getDisplayName(), event.getMessage()}));
		}
	}

	@SubscribeEvent
	public void coolsimReceivedChat(ClientChatReceivedEvent event) {

		TranslationTextComponent coolsim = new TranslationTextComponent("sp.coolsim.creator");
		coolsim.func_240699_a_(TextFormatting.GOLD);

		TranslationTextComponent playerJoined = new TranslationTextComponent("multiplayer.player.joined", new Object[] {"coolsim"});
		playerJoined.func_240699_a_(TextFormatting.YELLOW);

		TranslationTextComponent playerLeft = new TranslationTextComponent("multiplayer.player.left", new Object[] {"coolsim"});
		playerLeft.func_240699_a_(TextFormatting.YELLOW);

		TranslationTextComponent coolsimJoined = new TranslationTextComponent("sp.coolsim.joined");
		coolsimJoined.func_240699_a_(TextFormatting.YELLOW);

		TranslationTextComponent coolsimLeft = new TranslationTextComponent("sp.coolsim.left");
		coolsimLeft.func_240699_a_(TextFormatting.YELLOW);

		if(event.getMessage().getString().equals(playerJoined.getString())) {
			event.setMessage(coolsimJoined);
		}

		if(event.getMessage().getString().equals(playerLeft.getString())) {
			event.setMessage(coolsimLeft);
		}

		if(event.getMessage().getString().startsWith("[coolsim]")) {
			event.setMessage(new TranslationTextComponent("%s %s", new Object[] {coolsim, event.getMessage()}));
		}
	}

	@SubscribeEvent
	public void coolsimDeath(LivingDeathEvent event) {

		if(event.getEntity() instanceof ServerPlayerEntity && event.getEntity().getUniqueID().equals(UUID.fromString("54481257-7b6d-4c8e-8aac-ca6f864e1412")) && event.getSource().getTrueSource() instanceof ServerPlayerEntity) {

			ServerPlayerEntity attacker = (ServerPlayerEntity) event.getSource().getTrueSource();
			ItemStack coolsimHead = getcoolsimHead((PlayerEntity) event.getEntity());

			if(coolsimHead != null) {
				ItemHandlerHelper.giveItemToPlayer(attacker, coolsimHead);
			} else {
				TranslationTextComponent error = new TranslationTextComponent("sp.coolsim.error");
				error.func_240699_a_(TextFormatting.RED);
				attacker.func_241151_a_(error, ChatType.SYSTEM, Util.field_240973_b_);
			}
		}

	}

	public static ItemStack getcoolsimHead(PlayerEntity coolsim)
	{
		String texture = "eyJ0aW1lc3RhbXAiOjE1NzYxMTM5OTc5ODUsInByb2ZpbGVJZCI6IjU0NDgxMjU3N2I2ZDRjOGU4YWFjY2E2Zjg2NGUxNDEyIiwicHJvZmlsZU5hbWUiOiJjb29sc2ltIiwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzdmMDkwM2QxOGMyZTE4YmQzYzBiMDk5YmIzZGFkNmVjYTQ2ZDBjMzdkZjJkM2FlMjljYzAwOWYwN2I5OTM3NmYifX19";

		ItemStack playerhead = new ItemStack(Items.PLAYER_HEAD);

		TranslationTextComponent headName = new TranslationTextComponent("block.minecraft.player_head.named", new Object[] {"coolsim"});
		headName.func_240699_a_(TextFormatting.ITALIC);
		CompoundNBT properties = new CompoundNBT();
		ListNBT textures = new ListNBT();
		CompoundNBT tex = new CompoundNBT();
		tex.putString("Value", texture);
		textures.add(tex);
		properties.put("textures", textures);
		playerhead.setTagInfo("SkullOwner", NBTUtil.writeGameProfile(new CompoundNBT(), coolsim.getGameProfile()));
		playerhead.setDisplayName(headName);

		return playerhead;
	}

	private void angerNearbyBees(World world, BlockPos pos) {
		List<BeeEntity> list = world.getEntitiesWithinAABB(BeeEntity.class, (new AxisAlignedBB(pos)).grow(8.0D, 6.0D, 8.0D));
		if (!list.isEmpty()) {
			List<PlayerEntity> list1 = world.getEntitiesWithinAABB(PlayerEntity.class, (new AxisAlignedBB(pos)).grow(8.0D, 6.0D, 8.0D));
			int i = list1.size();

			for(BeeEntity beeentity : list) {
				if (beeentity.getAttackTarget() == null) {
					beeentity.setAttackTarget(list1.get(world.rand.nextInt(i)));
				}
			}
		}

	}

	private boolean hasBees(World world, BlockPos pos) {
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity instanceof BeehiveTileEntity) {
			BeehiveTileEntity beehivetileentity = (BeehiveTileEntity)tileentity;
			return !beehivetileentity.hasNoBees();
		} else {
			return false;
		}
	}


}
