package com.dikiytechies.rotp_battleroyale.command;

import com.github.standobyte.jojo.command.JojoCommandsCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.*;

public class Teams {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("team").requires(ctx -> ctx.hasPermission(2))
                .then(Commands.literal("clear")
                        .executes(ctx -> clearTeams(ctx.getSource()))
                ).then(Commands.literal("fill").executes(ctx -> fillTeams(ctx.getSource(), getSurvivalPlayers(ctx.getSource()))).then(Commands.argument("amount", IntegerArgumentType.integer(1, Integer.MAX_VALUE))
                        .executes(ctx -> fillTeams(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "amount")))))
        );
        JojoCommandsCommand.addCommand("teams");
    }

    protected static int clearTeams(CommandSource source) {
        ServerScoreboard scoreboard = source.getLevel().getScoreboard();
        if (!scoreboard.getPlayerTeams().isEmpty()) {
            ArrayList<ScorePlayerTeam> toRemove = new ArrayList<>();
            for (ScorePlayerTeam t : scoreboard.getPlayerTeams()) toRemove.add(t);
            for (ScorePlayerTeam t : toRemove) scoreboard.removePlayerTeam(t);
            source.sendSuccess(new TranslationTextComponent("commands.cleared.success"), true);
            return toRemove.size();
        }
        source.sendFailure(new TranslationTextComponent("commands.cleared.failed"));
        return 0;
    }

    protected static int fillTeams(CommandSource source, int amount) {
        ServerScoreboard scoreboard = source.getLevel().getScoreboard();
        if (scoreboard.getPlayerTeam("br_0") == null) {
            Map<ScorePlayerTeam, Integer> spaces = new HashMap<>();
            for (int i = 0; i < amount; i++) {
                scoreboard.addPlayerTeam("br_" + i);
                scoreboard.getPlayerTeam("br_" + i).setAllowFriendlyFire(false);
                scoreboard.getPlayerTeam("br_" + i).setNameTagVisibility(Team.Visible.HIDE_FOR_OTHER_TEAMS);
                List<ServerPlayerEntity> players = source.getLevel().getPlayers(ServerPlayerEntity::isAlive);
                List<ServerPlayerEntity> toRemoveNonSurv = new ArrayList<>();
                for (ServerPlayerEntity s : players) {
                    if (!s.gameMode.isSurvival()) toRemoveNonSurv.add(s);
                }
                if (i != amount - 1 || players.size() % amount == 0) {
                    spaces.put(scoreboard.getPlayerTeam("br_" + i), (int) Math.floor((double) players.size() / amount));
                } else {
                    spaces.put(scoreboard.getPlayerTeam("br_" + i), players.size() % amount);
                }
            }
            ArrayList<Integer> spacesArray = new ArrayList<>();
            int spacesI = 0;
            for (Map.Entry<ScorePlayerTeam, Integer> entry : spaces.entrySet()) {
                spacesArray.add(spacesI, entry.getValue());
                spacesI++;
            }
            for (PlayerEntity p : source.getLevel().players())
                if (!scoreboard.getPlayerTeams().isEmpty()) {
                    Random random = new Random();
                    int index = random.nextInt(spacesArray.size());
                    scoreboard.addPlayerToTeam(p.getStringUUID(), scoreboard.getPlayerTeam("br_" + index));
                    spacesArray.set(index, spacesArray.get(index) - 1);
                    if (spacesArray.get(index) == 0) spacesArray.remove(index);
                }
            source.sendSuccess(new TranslationTextComponent("commands.filled.success"), true);
            return 0;
        }
        source.sendFailure(new TranslationTextComponent("commands.filled.failed"));
        return -1;
    }

    protected static int getSurvivalPlayers(CommandSource source) {
        List<ServerPlayerEntity> players = source.getLevel().getPlayers(ServerPlayerEntity::isAlive);
        for (ServerPlayerEntity p: players) {
            if (!p.gameMode.isSurvival()) players.remove(p);
        }
        if (players.isEmpty()) {
            source.sendFailure(new TranslationTextComponent("commands.filled.failed.no_survival"));
        }
        return players.size();
    }
}
