package de.dietrichpaul.winkel.feature.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import de.dietrichpaul.winkel.feature.command.SimpleSuggestionBuilder;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class FileArgumentType implements ArgumentType<File> {

    private File directory;
    private String extension;
    private boolean existingFiles;
    private boolean newFiles;

    private FileArgumentType(File directory, String extension, boolean existingFiles, boolean newFiles) {
        this.directory = directory;
        this.extension = extension;
        this.existingFiles = existingFiles;
        this.newFiles = newFiles;
    }

    public static FileArgumentType file(File directory, String suffix, boolean existingFiles, boolean newFiles) {
        return new FileArgumentType(directory, suffix, existingFiles, newFiles);
    }

    public static <S> File getFile(String name, CommandContext<S> context) {
        return context.getArgument(name, File.class);
    }

    @Override
    public File parse(StringReader reader) throws CommandSyntaxException {
        String readString = reader.readString();
        String baseName = FilenameUtils.getBaseName(readString);
        if (!baseName.equals(readString))
            return null;
        File file = new File(this.directory, baseName + "." + this.extension);
        if (file.exists() && !this.existingFiles) {
            return null;
        }
        if (!file.exists() && !this.newFiles) {
            return null;
        }
        return file;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        SimpleSuggestionBuilder simpleBuilder = new SimpleSuggestionBuilder(builder);
        if (existingFiles) {
            for (File file : this.directory.listFiles()) {
                if (FilenameUtils.getExtension(file.getName()).equals(this.extension))
                    simpleBuilder.add(FilenameUtils.getBaseName(file.getName()));
            }
        }
        return simpleBuilder.build();
    }

}
