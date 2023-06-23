package com.xlife.common.command.argument;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.xlife.common.capability.world.StoredWorldInformation;
import com.xlife.common.data.PlayerInformationData;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SavedPlayerArgument implements ArgumentType<SavedPlayerArgument.IProfileProvider> {
    private static final Collection<String> EXAMPLES = Collections.singletonList("Player");

    public static SavedPlayerArgument player() {
        return new SavedPlayerArgument();
    }

    public static Collection<GameProfile> getPlayer(CommandContext<CommandSource> context, String name) {
        return context.getArgument(name, IProfileProvider.class).getNames(context.getSource());
    }

    public IProfileProvider parse(StringReader reader) {
        int i = reader.getCursor();

        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
        }

        String s = reader.getString().substring(i, reader.getCursor());
        return (source) -> {
            return Collections.singleton(source.getServer().getPlayerProfileCache().getGameProfileForUsername(s));
        };
    }

    /**
     * Suggestion list is of player names stored in the world capability.
     */
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return ISuggestionProvider.suggest(((ISuggestionProvider)context.getSource()).getPlayerNames(), builder);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    @FunctionalInterface
    public interface IProfileProvider {
        Collection<GameProfile> getNames(CommandSource source);
    }

    public static class ProfileProvider implements IProfileProvider {
        /**
         * @return the names of players saved in the world capability as a list.
         */
        public Collection<GameProfile> getNames(CommandSource source) {
            List<GameProfile> list = Lists.newArrayList();

            for (PlayerInformationData data : new StoredWorldInformation().getPlayers().values()) {
                list.add(data.getPlayer());
            }
            return list;
        }
    }

}