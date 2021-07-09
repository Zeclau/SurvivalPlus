package net.coolsimulations.SurvivalPlus.core.commands;

import java.util.Collection;
import java.util.Iterator;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

import net.coolsimulations.SurvivalPlus.api.SPCompatibilityManager;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class CommandSleep {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("sleep")
				.then(Commands.argument("targets", EntityArgument.players())
						.requires(s -> s.hasPermission(0))
						.executes(sleep -> sleep(sleep.getSource(), EntityArgument.getPlayers(sleep, "targets")))));

		dispatcher.register(Commands.literal("sleep")
				.requires(s -> s.hasPermission(0))
				.executes(sleep -> sleepSingle(sleep.getSource())));

		dispatcher.register(Commands.literal("sloop")
				.then(Commands.argument("targets", EntityArgument.players())
						.requires(s -> s.hasPermission(0))
						.executes(sleep -> sleep(sleep.getSource(), EntityArgument.getPlayers(sleep, "targets")))));

		dispatcher.register(Commands.literal("sloop")
				.requires(s -> s.hasPermission(0))
				.executes(sleep -> sleepSingle(sleep.getSource())));
	}

	private static int sleep(CommandSourceStack sender, Collection<ServerPlayer> players) {
		Iterator var3 = players.iterator();

		while (var3.hasNext()) {
			ServerPlayer entityplayer = (ServerPlayer) var3.next();

			TranslatableComponent dimension = null;
			if (!SPCompatibilityManager.isGCLoaded() && sender.getLevel().dimension() == Level.OVERWORLD) {
				dimension = new TranslatableComponent("createWorld.customize.preset.overworld", new Object[]{});
			}

			if (sender.getLevel().dimension() == Level.END) {
				throw new CommandRuntimeException(new TranslatableComponent("sp.commands.sleep.invalid"));
			} else {
				if (!sender.getLevel().isDay()) {
					if (entityplayer == sender.getEntity()) {
						if (sender.getEntity() instanceof Player && ((Player) sender.getEntity()).isSleeping()) {

							if (dimension == null) {
								TranslatableComponent sleep = new TranslatableComponent("sp.commands.sleep.display3", new Object[]{sender.getDisplayName(), sender.getDisplayName(), sender.getLevel().dimension().location()});
								sleep.withStyle(ChatFormatting.LIGHT_PURPLE);
								if(sender.getEntity() != null)
									sender.getServer().getPlayerList().broadcastMessage(sleep, ChatType.CHAT, sender.getEntity().getUUID());
								else
									sender.getServer().getPlayerList().broadcastMessage(sleep, ChatType.SYSTEM, Util.NIL_UUID);
							} else {
								TranslatableComponent sleep = new TranslatableComponent("sp.commands.sleep.display3", new Object[]{sender.getDisplayName(), dimension});
								sleep.withStyle(ChatFormatting.LIGHT_PURPLE);
								if(sender.getEntity() != null)
									sender.getServer().getPlayerList().broadcastMessage(sleep, ChatType.CHAT, sender.getEntity().getUUID());
								else
									sender.getServer().getPlayerList().broadcastMessage(sleep, ChatType.SYSTEM, Util.NIL_UUID);
							}
						} else {
							throw new CommandRuntimeException(new TranslatableComponent("sp.commands.sleep.display4", new Object[]{sender.getDisplayName(), dimension}));
						}
					} else {
						if (entityplayer.level.dimension() == sender.getLevel().dimension()) {

							if (sender.getLevel().dimension() == Level.NETHER || sender.getLevel().dimension() == Level.END) {
								throw new CommandRuntimeException(new TranslatableComponent("sp.commands.sleep.invalid"));
							} else {
								if (dimension == null) {
									TranslatableComponent sleep = new TranslatableComponent("sp.commands.sleep.display1", new Object[]{entityplayer.getDisplayName(), sender.getDisplayName(), sender.getLevel().dimension().location()});
									sleep.withStyle(ChatFormatting.LIGHT_PURPLE);
									if(sender.getEntity() != null)
										sender.getServer().getPlayerList().broadcastMessage(sleep, ChatType.CHAT, sender.getEntity().getUUID());
									else
										sender.getServer().getPlayerList().broadcastMessage(sleep, ChatType.SYSTEM, Util.NIL_UUID);
								} else {
									TranslatableComponent sleep = new TranslatableComponent("sp.commands.sleep.display1", new Object[]{entityplayer.getDisplayName(), sender.getDisplayName(), dimension});
									sleep.withStyle(ChatFormatting.LIGHT_PURPLE);
									if(sender.getEntity() != null)
										sender.getServer().getPlayerList().broadcastMessage(sleep, ChatType.CHAT, sender.getEntity().getUUID());
									else
										sender.getServer().getPlayerList().broadcastMessage(sleep, ChatType.SYSTEM, Util.NIL_UUID);
								}
							}
						} else {
							throw new CommandRuntimeException(new TranslatableComponent("sp.commands.sleep.dimension"));
						}
					}
				} else {
					throw new CommandRuntimeException(new TranslatableComponent("sp.commands.sleep.night"));
				}
			}
		}

		return players.size();
	}

	private static int sleepSingle(CommandSourceStack sender) {

		TranslatableComponent dimension = null;
		if(!SPCompatibilityManager.isGCLoaded() && sender.getLevel().dimension() == Level.OVERWORLD) {
			dimension = new TranslatableComponent("createWorld.customize.preset.overworld", new Object[] {});
		}

		if(sender.getLevel().dimension() == Level.END) {
			throw new CommandRuntimeException(new TranslatableComponent("sp.commands.sleep.invalid"));
		} else {
			if (!sender.getLevel().isDay()) {

				if (sender.getLevel().dimension() == Level.NETHER) {
					throw new CommandRuntimeException(new TranslatableComponent("sp.commands.sleep.invalid"));
				} else {
					if (dimension == null) {
						TranslatableComponent sleep = new TranslatableComponent("sp.commands.sleep.display2", new Object[]{sender.getDisplayName(), sender.getLevel().dimension().location()});
						sleep.withStyle(ChatFormatting.LIGHT_PURPLE);
						if(sender.getEntity() != null)
							sender.getServer().getPlayerList().broadcastMessage(sleep, ChatType.CHAT, sender.getEntity().getUUID());
						else
							sender.getServer().getPlayerList().broadcastMessage(sleep, ChatType.SYSTEM, Util.NIL_UUID);
					} else {
						TranslatableComponent sleep = new TranslatableComponent("sp.commands.sleep.display2", new Object[]{sender.getDisplayName(), dimension});
						sleep.withStyle(ChatFormatting.LIGHT_PURPLE);
						if(sender.getEntity() != null)
							sender.getServer().getPlayerList().broadcastMessage(sleep, ChatType.CHAT, sender.getEntity().getUUID());
						else
							sender.getServer().getPlayerList().broadcastMessage(sleep, ChatType.SYSTEM, Util.NIL_UUID);
					}
				}
			} else {
				throw new CommandRuntimeException(new TranslatableComponent("sp.commands.sleep.night"));
			}
		}

		return Command.SINGLE_SUCCESS;
	}
}
