package com.xlife.common.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.xlife.common.capability.world.WorldCapability;
import com.xlife.common.data.PlayerInformationData;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.dimension.DimensionType;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class SavedPlayerArgument implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Collections.singletonList("Player");

    public static SavedPlayerArgument player() {
        return new SavedPlayerArgument();
    }

    @Override
    public String parse(StringReader reader) {
        int i = reader.getCursor();

        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
        }
        return reader.getString().substring(i, reader.getCursor());
    }

    /**
     * Suggestion list is of player names stored in the world capability.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (!(context.getSource() instanceof ISuggestionProvider)) {
            return Suggestions.empty();
        } else if (context.getSource() instanceof ClientSuggestionProvider) {
            return ((ClientSuggestionProvider)context.getSource()).getSuggestionsFromServer((CommandContext<ISuggestionProvider>)context, builder);
        } else {
            PlayerList players = ((CommandSource)context.getSource()).getServer().getPlayerList();

            for (PlayerEntity player : players.getPlayers()) {
                if (player.getServer() != null) {
                    player.getServer().getWorld(DimensionType.OVERWORLD).getCapability(WorldCapability.WORLD_DATA).ifPresent((world) -> {

                        for (PlayerInformationData playerData : world.getPlayers().values()) {
                            builder.suggest(TextFormatting.getTextWithoutFormattingCodes(playerData.getPlayer().getName()));
                        }
                    });
                }
            }
            return builder.buildFuture();
        }
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }

}