package de.dietrichpaul.winkel.feature.command;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.context.SuggestionContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.dietrichpaul.winkel.WinkelClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CustomCommandSuggestor {
    private static final Pattern BACKSLASH_S_PATTERN = Pattern.compile("(\\s+)");
    private static final Style ERROR_STYLE = Style.EMPTY.withColor(Formatting.RED);
    private static final Style INFO_STYLE = Style.EMPTY.withColor(Formatting.GRAY);
    private static final List<Style> HIGHLIGHT_STYLES = Stream.of(Formatting.AQUA, Formatting.YELLOW, Formatting.GREEN, Formatting.LIGHT_PURPLE, Formatting.GOLD).map(Style.EMPTY::withColor).collect(ImmutableList.toImmutableList());
    final MinecraftClient client;
    final Screen owner;
    final TextFieldWidget textField;
    final TextRenderer textRenderer;
    private final boolean slashOptional;
    private final boolean suggestingWhenEmpty;
    final int inWindowIndexOffset;
    final int maxSuggestionSize;
    final boolean chatScreenSized;
    final int color;
    private final List<OrderedText> messages = Lists.newArrayList();
    private int x;
    private int width;
    private boolean isAxiom;
    private ParseResults<InternalCommandSource> axiomParseResults;
    @Nullable
    private ParseResults<CommandSource> parse;
    @Nullable
    private CompletableFuture<Suggestions> pendingSuggestions;
    @Nullable
    CustomCommandSuggestor.SuggestionWindow window;
    private boolean windowActive;
    boolean completingSuggestions;

    public CustomCommandSuggestor(MinecraftClient client, Screen owner, TextFieldWidget textField, TextRenderer textRenderer, boolean slashOptional, boolean suggestingWhenEmpty, int inWindowIndexOffset, int maxSuggestionSize, boolean chatScreenSized, int color) {
        this.client = client;
        this.owner = owner;
        this.textField = textField;
        this.textRenderer = textRenderer;
        this.slashOptional = slashOptional;
        this.suggestingWhenEmpty = suggestingWhenEmpty;
        this.inWindowIndexOffset = inWindowIndexOffset;
        this.maxSuggestionSize = maxSuggestionSize;
        this.chatScreenSized = chatScreenSized;
        this.color = color;
        textField.setRenderTextProvider(this::provideRenderText);
    }

    public void setWindowActive(boolean windowActive) {
        this.windowActive = windowActive;
        if (!windowActive) {
            this.window = null;
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.window != null && this.window.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if (this.owner.getFocused() == this.textField && keyCode == 258) {
            this.showSuggestions(true);
            return true;
        }
        return false;
    }

    public boolean mouseScrolled(double amount) {
        return this.window != null && this.window.mouseScrolled(MathHelper.clamp(amount, -1.0, 1.0));
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.window != null && this.window.mouseClicked((int)mouseX, (int)mouseY, button);
    }

    public void showSuggestions(boolean narrateFirstSuggestion) {
        Suggestions suggestions;
        if (this.pendingSuggestions != null && this.pendingSuggestions.isDone() && !(suggestions = this.pendingSuggestions.join()).isEmpty()) {
            int i = 0;
            for (Suggestion suggestion : suggestions.getList()) {
                i = Math.max(i, this.textRenderer.getWidth(suggestion.getText()));
            }
            int j = MathHelper.clamp(this.textField.getCharacterX(suggestions.getRange().getStart()), 0, this.textField.getCharacterX(0) + this.textField.getInnerWidth() - i);
            int suggestion = this.chatScreenSized ? this.owner.height - 12 : 72;
            this.window = new SuggestionWindow(j, suggestion, i, this.sortSuggestions(suggestions), narrateFirstSuggestion);
        }
    }

    private List<Suggestion> sortSuggestions(Suggestions suggestions) {
        String string = this.textField.getText().substring(0, this.textField.getCursor());
        int i = CustomCommandSuggestor.getLastPlayerNameStart(string);
        String string2 = string.substring(i).toLowerCase(Locale.ROOT);
        ArrayList<Suggestion> list = Lists.newArrayList();
        ArrayList<Suggestion> list2 = Lists.newArrayList();
        for (Suggestion suggestion : suggestions.getList()) {
            if (suggestion.getText().startsWith(string2) || suggestion.getText().startsWith("minecraft:" + string2)) {
                list.add(suggestion);
                continue;
            }
            list2.add(suggestion);
        }
        list.addAll(list2);
        return list;
    }

    public void refresh() {
        String string = this.textField.getText();
        if (this.parse != null && !this.parse.getReader().getString().equals(string)) {
            this.parse = null;
        }
        if (this.axiomParseResults != null && !this.axiomParseResults.getReader().getString().equals(string)) {
            this.axiomParseResults = null;
        }
        if (!this.completingSuggestions) {
            this.textField.setSuggestion(null);
            this.window = null;
        }
        this.messages.clear();
        StringReader stringReader = new StringReader(string);
        boolean bl = stringReader.canRead() && (stringReader.peek() == '/' || stringReader.peek() == '#');
        if (bl) {
            this.isAxiom = stringReader.peek() =='#';
            stringReader.skip();
        }
        boolean bl22 = this.slashOptional || bl;
        int i = this.textField.getCursor();
        if (bl22) {
            if (isAxiom) {
                int j;
                CommandDispatcher<InternalCommandSource> commandDispatcher =
                        WinkelClient.INSTANCE.getCommandManager().getDispatcher();
                if (this.axiomParseResults == null) {
                    this.axiomParseResults
                            = commandDispatcher.parse(stringReader,
                            WinkelClient.INSTANCE.getCommandManager().getCommandListener().getInternalSource());
                }
                int n = j = this.suggestingWhenEmpty ? stringReader.getCursor() : 1;
                if (!(i < j || this.window != null && this.completingSuggestions)) {
                    this.pendingSuggestions = commandDispatcher.getCompletionSuggestions(this.axiomParseResults, i);
                    this.pendingSuggestions.thenRun(() -> {
                        if (!this.pendingSuggestions.isDone()) {
                            return;
                        }
                        this.show();
                    });
                }
            } else {
                int j;
                CommandDispatcher<CommandSource> commandDispatcher = this.client.player.networkHandler.getCommandDispatcher();
                if (this.parse == null) {
                    this.parse = commandDispatcher.parse(stringReader, (CommandSource)this.client.player.networkHandler.getCommandSource());
                }
                int n = j = this.suggestingWhenEmpty ? stringReader.getCursor() : 1;
                if (!(i < j || this.window != null && this.completingSuggestions)) {
                    this.pendingSuggestions = commandDispatcher.getCompletionSuggestions(this.parse, i);
                    this.pendingSuggestions.thenRun(() -> {
                        if (!this.pendingSuggestions.isDone()) {
                            return;
                        }
                        this.show();
                    });
                }
            }
        } else {
            String commandDispatcher = string.substring(0, i);
            int j = CustomCommandSuggestor.getLastPlayerNameStart(commandDispatcher);
            Collection<String> collection = this.client.player.networkHandler.getCommandSource().getPlayerNames();
            this.pendingSuggestions = CommandSource.suggestMatching(collection, new SuggestionsBuilder(commandDispatcher, j));
        }
    }

    private static int getLastPlayerNameStart(String input) {
        if (Strings.isNullOrEmpty(input)) {
            return 0;
        }
        int i = 0;
        Matcher matcher = BACKSLASH_S_PATTERN.matcher(input);
        while (matcher.find()) {
            i = matcher.end();
        }
        return i;
    }

    private static OrderedText formatException(CommandSyntaxException exception) {
        Text text = Texts.toText(exception.getRawMessage());
        String string = exception.getContext();
        if (string == null) {
            return text.asOrderedText();
        }
        return Text.translatable("command.context.parse_error", text, exception.getCursor(), string).asOrderedText();
    }


    private void show() {
        ParseResults<? extends CommandSource> parse = isAxiom ? this.axiomParseResults : this.parse;
        if (this.textField.getCursor() == this.textField.getText().length()) {
            if (this.pendingSuggestions.join().isEmpty() && !parse.getExceptions().isEmpty()) {
                int i = 0;
                for (Map.Entry<? extends CommandNode<? extends CommandSource>, CommandSyntaxException> entry : parse.getExceptions().entrySet()) {
                    CommandSyntaxException commandSyntaxException = entry.getValue();
                    if (commandSyntaxException.getType() == CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect()) {
                        ++i;
                        continue;
                    }
                    this.messages.add(CustomCommandSuggestor.formatException(commandSyntaxException));
                }
                if (i > 0) {
                    this.messages.add(CustomCommandSuggestor.formatException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().create()));
                }
            } else if (parse.getReader().canRead()) {
                this.messages.add(CustomCommandSuggestor.formatException(CommandManager.getException(parse)));
            }
        }
        this.x = 0;
        this.width = this.owner.width;
        if (this.messages.isEmpty()) {
            this.showUsages(Formatting.GRAY);
        }
        this.window = null;
        if (this.windowActive && this.client.options.getAutoSuggestions().getValue()) {
            this.showSuggestions(false);
        }
    }

    private void showUsages(Formatting formatting) {
        if (this.isAxiom) {
            CommandContextBuilder<InternalCommandSource> commandContextBuilder = this.axiomParseResults.getContext();
            SuggestionContext<InternalCommandSource> suggestionContext = commandContextBuilder.findSuggestionContext(this.textField.getCursor());
            Map<CommandNode<InternalCommandSource>, String> map = WinkelClient.INSTANCE.getCommandManager().getDispatcher().getSmartUsage(suggestionContext.parent, WinkelClient.INSTANCE.getCommandManager().getCommandListener().getInternalSource());
            ArrayList<OrderedText> list = Lists.newArrayList();
            int i = 0;
            Style style = Style.EMPTY.withColor(formatting);
            for (Map.Entry<CommandNode<InternalCommandSource>, String> entry : map.entrySet()) {
                if (entry.getKey() instanceof LiteralCommandNode) continue;
                list.add(OrderedText.styledForwardsVisitedString(entry.getValue(), style));
                i = Math.max(i, this.textRenderer.getWidth(entry.getValue()));
            }
            if (!list.isEmpty()) {
                this.messages.addAll(list);
                this.x = MathHelper.clamp(this.textField.getCharacterX(suggestionContext.startPos), 0, this.textField.getCharacterX(0) + this.textField.getInnerWidth() - i);
                this.width = i;
            }
        } else {
            CommandContextBuilder<CommandSource> commandContextBuilder = this.parse.getContext();
            SuggestionContext<CommandSource> suggestionContext = commandContextBuilder.findSuggestionContext(this.textField.getCursor());
            Map<CommandNode<CommandSource>, String> map = this.client.player.networkHandler.getCommandDispatcher().getSmartUsage(suggestionContext.parent, this.client.player.networkHandler.getCommandSource());
            ArrayList<OrderedText> list = Lists.newArrayList();
            int i = 0;
            Style style = Style.EMPTY.withColor(formatting);
            for (Map.Entry<CommandNode<CommandSource>, String> entry : map.entrySet()) {
                if (entry.getKey() instanceof LiteralCommandNode) continue;
                list.add(OrderedText.styledForwardsVisitedString(entry.getValue(), style));
                i = Math.max(i, this.textRenderer.getWidth(entry.getValue()));
            }
            if (!list.isEmpty()) {
                this.messages.addAll(list);
                this.x = MathHelper.clamp(this.textField.getCharacterX(suggestionContext.startPos), 0, this.textField.getCharacterX(0) + this.textField.getInnerWidth() - i);
                this.width = i;
            }
        }
    }

    private OrderedText provideRenderText(String original, int firstCharacterIndex) {
        if (this.parse != null || this.axiomParseResults != null) {
            return CustomCommandSuggestor.highlight(this.parse == null ? this.axiomParseResults : this.parse, original, firstCharacterIndex);
        }
        return OrderedText.styledForwardsVisitedString(original, Style.EMPTY);
    }

    @Nullable
    static String getSuggestionSuffix(String original, String suggestion) {
        if (suggestion.startsWith(original)) {
            return suggestion.substring(original.length());
        }
        return null;
    }

    private static OrderedText highlight(ParseResults<?> parse, String original, int firstCharacterIndex) {
        int m;
        ArrayList<OrderedText> list = Lists.newArrayList();
        int i = 0;
        int j = -1;
        CommandContextBuilder<?> commandContextBuilder = parse.getContext().getLastChild();
        for (ParsedArgument<?, ?> parsedArgument : commandContextBuilder.getArguments().values()) {
            int k;
            if (++j >= HIGHLIGHT_STYLES.size()) {
                j = 0;
            }
            if ((k = Math.max(parsedArgument.getRange().getStart() - firstCharacterIndex, 0)) >= original.length()) break;
            int l = Math.min(parsedArgument.getRange().getEnd() - firstCharacterIndex, original.length());
            if (l <= 0) continue;
            list.add(OrderedText.styledForwardsVisitedString(original.substring(i, k), INFO_STYLE));
            list.add(OrderedText.styledForwardsVisitedString(original.substring(k, l), HIGHLIGHT_STYLES.get(j)));
            i = l;
        }
        if (parse.getReader().canRead() && (m = Math.max(parse.getReader().getCursor() - firstCharacterIndex, 0)) < original.length()) {
            int parsedArgument = Math.min(m + parse.getReader().getRemainingLength(), original.length());
            list.add(OrderedText.styledForwardsVisitedString(original.substring(i, m), INFO_STYLE));
            list.add(OrderedText.styledForwardsVisitedString(original.substring(m, parsedArgument), ERROR_STYLE));
            i = parsedArgument;
        }
        list.add(OrderedText.styledForwardsVisitedString(original.substring(i), INFO_STYLE));
        return OrderedText.concat(list);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY) {
        if (this.window != null) {
            this.window.render(matrices, mouseX, mouseY);
        } else {
            int i = 0;
            for (OrderedText orderedText : this.messages) {
                int j = this.chatScreenSized ? this.owner.height - 14 - 13 - 12 * i : 72 + 12 * i;
                DrawableHelper.fill(matrices, this.x - 1, j, this.x + this.width + 1, j + 12, this.color);
                this.textRenderer.drawWithShadow(matrices, orderedText, (float)this.x, (float)(j + 2), -1);
                ++i;
            }
        }
    }

    public String getNarration() {
        if (this.window != null) {
            return "\n" + this.window.getNarration();
        }
        return "";
    }

    @Environment(value= EnvType.CLIENT)
    public class SuggestionWindow {
        private final Rect2i area;
        private final String typedText;
        private final List<Suggestion> suggestions;
        private int inWindowIndex;
        private int selection;
        private Vec2f mouse = Vec2f.ZERO;
        private boolean completed;
        private int lastNarrationIndex;

        SuggestionWindow(int x, int y, int width, List<Suggestion> suggestions, boolean narrateFirstSuggestion) {
            int i = x - 1;
            int j = CustomCommandSuggestor.this.chatScreenSized ? y - 3 - Math.min(suggestions.size(), CustomCommandSuggestor.this.maxSuggestionSize) * 12 : y;
            this.area = new Rect2i(i, j, width + 1, Math.min(suggestions.size(), CustomCommandSuggestor.this.maxSuggestionSize) * 12);
            this.typedText = CustomCommandSuggestor.this.textField.getText();
            this.lastNarrationIndex = narrateFirstSuggestion ? -1 : 0;
            this.suggestions = suggestions;
            this.select(0);
        }

        public void render(MatrixStack matrices, int mouseX, int mouseY) {
            Message l;
            int k;
            boolean bl4;
            int i = Math.min(this.suggestions.size(), CustomCommandSuggestor.this.maxSuggestionSize);
            int j = -5592406;
            boolean bl = this.inWindowIndex > 0;
            boolean bl2 = this.suggestions.size() > this.inWindowIndex + i;
            boolean bl3 = bl || bl2;
            boolean bl5 = bl4 = this.mouse.x != (float)mouseX || this.mouse.y != (float)mouseY;
            if (bl4) {
                this.mouse = new Vec2f(mouseX, mouseY);
            }
            if (bl3) {
                DrawableHelper.fill(matrices, this.area.getX(), this.area.getY() - 1, this.area.getX() + this.area.getWidth(), this.area.getY(), CustomCommandSuggestor.this.color);
                DrawableHelper.fill(matrices, this.area.getX(), this.area.getY() + this.area.getHeight(), this.area.getX() + this.area.getWidth(), this.area.getY() + this.area.getHeight() + 1, CustomCommandSuggestor.this.color);
                if (bl) {
                    for (k = 0; k < this.area.getWidth(); ++k) {
                        if (k % 2 != 0) continue;
                        DrawableHelper.fill(matrices, this.area.getX() + k, this.area.getY() - 1, this.area.getX() + k + 1, this.area.getY(), -1);
                    }
                }
                if (bl2) {
                    for (k = 0; k < this.area.getWidth(); ++k) {
                        if (k % 2 != 0) continue;
                        DrawableHelper.fill(matrices, this.area.getX() + k, this.area.getY() + this.area.getHeight(), this.area.getX() + k + 1, this.area.getY() + this.area.getHeight() + 1, -1);
                    }
                }
            }
            k = 0;
            for (int l2 = 0; l2 < i; ++l2) {
                Suggestion suggestion = this.suggestions.get(l2 + this.inWindowIndex);
                DrawableHelper.fill(matrices, this.area.getX(), this.area.getY() + 12 * l2, this.area.getX() + this.area.getWidth(), this.area.getY() + 12 * l2 + 12, CustomCommandSuggestor.this.color);
                if (mouseX > this.area.getX() && mouseX < this.area.getX() + this.area.getWidth() && mouseY > this.area.getY() + 12 * l2 && mouseY < this.area.getY() + 12 * l2 + 12) {
                    if (bl4) {
                        this.select(l2 + this.inWindowIndex);
                    }
                    k = 1;
                }
                CustomCommandSuggestor.this.textRenderer.drawWithShadow(matrices, suggestion.getText(), (float)(this.area.getX() + 1), (float)(this.area.getY() + 2 + 12 * l2), l2 + this.inWindowIndex == this.selection ? -256 : -5592406);
            }
            if (k != 0 && (l = this.suggestions.get(this.selection).getTooltip()) != null) {
                CustomCommandSuggestor.this.owner.renderTooltip(matrices, Texts.toText(l), mouseX, mouseY);
            }
        }

        public boolean mouseClicked(int x, int y, int button) {
            if (!this.area.contains(x, y)) {
                return false;
            }
            int i = (y - this.area.getY()) / 12 + this.inWindowIndex;
            if (i >= 0 && i < this.suggestions.size()) {
                this.select(i);
                this.complete();
            }
            return true;
        }

        public boolean mouseScrolled(double amount) {
            int j;
            int i = (int)(CustomCommandSuggestor.this.client.mouse.getX() * (double)CustomCommandSuggestor.this.client.getWindow().getScaledWidth() / (double)CustomCommandSuggestor.this.client.getWindow().getWidth());
            if (this.area.contains(i, j = (int)(CustomCommandSuggestor.this.client.mouse.getY() * (double)CustomCommandSuggestor.this.client.getWindow().getScaledHeight() / (double)CustomCommandSuggestor.this.client.getWindow().getHeight()))) {
                this.inWindowIndex = MathHelper.clamp((int)((double)this.inWindowIndex - amount), 0, Math.max(this.suggestions.size() - CustomCommandSuggestor.this.maxSuggestionSize, 0));
                return true;
            }
            return false;
        }

        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (keyCode == 265) {
                this.scroll(-1);
                this.completed = false;
                return true;
            }
            if (keyCode == 264) {
                this.scroll(1);
                this.completed = false;
                return true;
            }
            if (keyCode == 258) {
                if (this.completed) {
                    this.scroll(Screen.hasShiftDown() ? -1 : 1);
                }
                this.complete();
                return true;
            }
            if (keyCode == 256) {
                this.discard();
                return true;
            }
            return false;
        }

        public void scroll(int offset) {
            this.select(this.selection + offset);
            int i = this.inWindowIndex;
            int j = this.inWindowIndex + CustomCommandSuggestor.this.maxSuggestionSize - 1;
            if (this.selection < i) {
                this.inWindowIndex = MathHelper.clamp(this.selection, 0, Math.max(this.suggestions.size() - CustomCommandSuggestor.this.maxSuggestionSize, 0));
            } else if (this.selection > j) {
                this.inWindowIndex = MathHelper.clamp(this.selection + CustomCommandSuggestor.this.inWindowIndexOffset - CustomCommandSuggestor.this.maxSuggestionSize, 0, Math.max(this.suggestions.size() - CustomCommandSuggestor.this.maxSuggestionSize, 0));
            }
        }

        public void select(int index) {
            this.selection = index;
            if (this.selection < 0) {
                this.selection += this.suggestions.size();
            }
            if (this.selection >= this.suggestions.size()) {
                this.selection -= this.suggestions.size();
            }
            Suggestion suggestion = this.suggestions.get(this.selection);
            CustomCommandSuggestor.this.textField.setSuggestion(CustomCommandSuggestor.getSuggestionSuffix(CustomCommandSuggestor.this.textField.getText(), suggestion.apply(this.typedText)));
            if (this.lastNarrationIndex != this.selection) {
                NarratorManager.INSTANCE.narrate(this.getNarration());
            }
        }

        public void complete() {
            Suggestion suggestion = this.suggestions.get(this.selection);
            CustomCommandSuggestor.this.completingSuggestions = true;
            CustomCommandSuggestor.this.textField.setText(suggestion.apply(this.typedText));
            int i = suggestion.getRange().getStart() + suggestion.getText().length();
            CustomCommandSuggestor.this.textField.setSelectionStart(i);
            CustomCommandSuggestor.this.textField.setSelectionEnd(i);
            this.select(this.selection);
            CustomCommandSuggestor.this.completingSuggestions = false;
            this.completed = true;
        }

        Text getNarration() {
            this.lastNarrationIndex = this.selection;
            Suggestion suggestion = this.suggestions.get(this.selection);
            Message message = suggestion.getTooltip();
            if (message != null) {
                return Text.translatable("narration.suggestion.tooltip", this.selection + 1, this.suggestions.size(), suggestion.getText(), message);
            }
            return Text.translatable("narration.suggestion", this.selection + 1, this.suggestions.size(), suggestion.getText());
        }

        public void discard() {
            CustomCommandSuggestor.this.window = null;
        }
    }
}
