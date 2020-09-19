package net.coolsimulations.SurvivalPlus.core.commands;

import java.util.Collection;
import java.util.Iterator;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

import net.coolsimulations.SurvivalPlus.api.SPCompatibilityManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;

public class CommandSleep {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(Commands.literal("sleep")
				.then(Commands.argument("targets", EntityArgument.players())
						.requires(s -> s.hasPermissionLevel(0))
						.executes(sleep -> sleep(sleep.getSource(), EntityArgument.getPlayers(sleep, "targets")))));

		dispatcher.register(Commands.literal("sleep")
				.requires(s -> s.hasPermissionLevel(0))
				.executes(sleep -> sleepSingle(sleep.getSource())));
	}

	private static int sleep(CommandSource sender, Collection<ServerPlayerEntity> players) {
		Iterator var3 = players.iterator();

		while (var3.hasNext()) {
			ServerPlayerEntity entityplayer = (ServerPlayerEntity) var3.next();

			TranslationTextComponent dimension = null;
			if (!SPCompatibilityManager.isGCLoaded() && sender.getWorld().func_234923_W_() == World.field_234918_g_) {
				dimension = new TranslationTextComponent("createWorld.customize.preset.overworld", new Object[]{});
			}

			if (sender.getWorld().func_234923_W_() == World.field_234920_i_) {
				throw new CommandException(new TranslationTextComponent("sp.commands.sleep.invalid"));
			} else {
				if (!sender.getWorld().isDaytime()) {
					if (entityplayer == sender.getEntity()) {
						if (sender.getEntity() instanceof PlayerEntity && ((PlayerEntity) sender.getEntity()).isSleeping()) {

							if (dimension == null) {
								TranslationTextComponent sleep = new TranslationTextComponent("sp.commands.sleep.display3", new Object[]{sender.getDisplayName(), sender.getDisplayName(), sender.getWorld().func_234923_W_().func_240901_a_()});
								sleep.func_240699_a_(TextFormatting.LIGHT_PURPLE);
								sender.getServer().getPlayerList().func_232641_a_(sleep, ChatType.SYSTEM, Util.field_240973_b_);
							} else {
								TranslationTextComponent sleep = new TranslationTextComponent("sp.commands.sleep.display3", new Object[]{sender.getDisplayName(), dimension});
								sleep.func_240699_a_(TextFormatting.LIGHT_PURPLE);
								sender.getServer().getPlayerList().func_232641_a_(sleep, ChatType.SYSTEM, Util.field_240973_b_);
							}
						} else {
							throw new CommandException(new TranslationTextComponent("sp.commands.sleep.display4", new Object[]{sender.getDisplayName(), dimension}));
						}
					} else {
						if (entityplayer.world.func_234923_W_() == sender.getWorld().func_234923_W_()) {

							if (sender.getWorld().func_234923_W_() == World.field_234919_h_ || sender.getWorld().func_234923_W_() == World.field_234920_i_) {
								throw new CommandException(new TranslationTextComponent("sp.commands.sleep.invalid"));
							} else {
								if (dimension == null) {
									TranslationTextComponent sleep = new TranslationTextComponent("sp.commands.sleep.display1", new Object[]{entityplayer.getDisplayName(), sender.getDisplayName(), sender.getWorld().func_234923_W_().func_240901_a_()});
									sleep.func_240699_a_(TextFormatting.LIGHT_PURPLE);
									sender.getServer().getPlayerList().func_232641_a_(sleep, ChatType.SYSTEM, Util.field_240973_b_);
								} else {
									TranslationTextComponent sleep = new TranslationTextComponent("sp.commands.sleep.display1", new Object[]{entityplayer.getDisplayName(), sender.getDisplayName(), dimension});
									sleep.func_240699_a_(TextFormatting.LIGHT_PURPLE);
									sender.getServer().getPlayerList().func_232641_a_(sleep, ChatType.SYSTEM, Util.field_240973_b_);
								}
							}
						} else {
							throw new CommandException(new TranslationTextComponent("sp.commands.sleep.dimension"));
						}
					}
				} else {
					throw new CommandException(new TranslationTextComponent("sp.commands.sleep.night"));
				}
			}
		}

		return players.size();
	}

	private static int sleepSingle(CommandSource sender) {

		TranslationTextComponent dimension = null;
		if(!SPCompatibilityManager.isGCLoaded() && sender.getWorld().func_234923_W_() == World.field_234918_g_) {
			dimension = new TranslationTextComponent("createWorld.customize.preset.overworld", new Object[] {});
		}

		if(sender.getWorld().func_234923_W_() == World.field_234920_i_) {
			throw new CommandException(new TranslationTextComponent("sp.commands.sleep.invalid"));
		} else {
			if (!sender.getWorld().isDaytime()) {

				if (sender.getWorld().func_234923_W_() == World.field_234919_h_) {
					throw new CommandException(new TranslationTextComponent("sp.commands.sleep.invalid"));
				} else {
					if (dimension == null) {
						TranslationTextComponent sleep = new TranslationTextComponent("sp.commands.sleep.display2", new Object[]{sender.getDisplayName(), sender.getWorld().func_234923_W_().func_240901_a_()});
						sleep.func_240699_a_(TextFormatting.LIGHT_PURPLE);
						sender.getServer().getPlayerList().func_232641_a_(sleep, ChatType.SYSTEM, Util.field_240973_b_);
					} else {
						TranslationTextComponent sleep = new TranslationTextComponent("sp.commands.sleep.display2", new Object[]{sender.getDisplayName(), dimension});
						sleep.func_240699_a_(TextFormatting.LIGHT_PURPLE);
						sender.getServer().getPlayerList().func_232641_a_(sleep, ChatType.SYSTEM, Util.field_240973_b_);
					}
				}
			} else {
				throw new CommandException(new TranslationTextComponent("sp.commands.sleep.night"));
			}
		}

		return Command.SINGLE_SUCCESS;
	}
}
