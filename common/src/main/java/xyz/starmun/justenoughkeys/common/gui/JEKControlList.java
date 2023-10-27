package xyz.starmun.justenoughkeys.common.gui;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.ArrayUtils;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;

import java.util.*;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class JEKControlList extends KeyBindsList {
    private final KeyBindsScreen controlsScreen;
    private final List<Entry> keyEntries = new ArrayList<>();
    private int maxListLabelWidth;

    public JEKControlList(KeyBindsScreen controlsScreen, Minecraft minecraft) {
        super(controlsScreen, minecraft);

        this.width = controlsScreen.width + 45;
        this.height = controlsScreen.height;
        this.y0 = 43;
        this.y1 = controlsScreen.height - 70;
        this.x1 = controlsScreen.width + 45;
        this.controlsScreen = controlsScreen;
        this.children().clear();
        this.initKeyMappings();
    }

    @Override
    protected void renderDecorations(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (this.getJEKKeyEntryAtPos(mouseY) instanceof JEKKeyEntry keyEntry) {
            guiGraphics.renderTooltip(this.minecraft.font, Component.translatable(keyEntry.getCategory()), mouseX, mouseY);
        }
    }

    public Entry getJEKKeyEntryAtPos(double mouseY) {
        if (mouseY <=  y0 || mouseY >= y1) {
            return null;
        }

        int relativeCursorPosition = Mth.floor(mouseY - (double) this.y0) - this.headerHeight + (int) this.getScrollAmount() - 4;
        int keyEntryIndex = relativeCursorPosition / this.itemHeight;

        return relativeCursorPosition >= 0 && keyEntryIndex < this.getItemCount() ? this.children().get(keyEntryIndex) : null;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.width - 50;
    }

    @Override
    public int getRowWidth() {
        return this.width - 60;
    }

    private void initKeyMappings() {
        Arrays.stream(ArrayUtils.clone(minecraft.options.keyMappings))
                .collect(Collectors.groupingBy(KeyMapping::getCategory, LinkedHashMap::new, Collectors.toList()))
                .forEach((category, mappings) -> {
                    JKECategoryEntry categoryEntry = new JKECategoryEntry(category);
                    this.children().add(categoryEntry);
                    this.keyEntries.add(categoryEntry);
                    mappings.forEach(mapping -> {
                        JEKKeyEntry keyEntry = new JEKKeyEntry(mapping);
                        this.children().add(keyEntry);
                        this.keyEntries.add(keyEntry);
                        int i = minecraft.font.width(I18n.get(category));
                        if (i > this.maxListLabelWidth) {
                            this.maxListLabelWidth = i;
                        }
                    });
                });
    }

    public void search(String query) {
        this.children().clear();
        String allParametersStrippedQuery = query.replaceAll("\\^c|\\^u|@k|@c|@n","").trim();
        String filterParametersStrippedQuery = query.replaceAll("\\^c|\\^u","").trim();

        this.setScrollAmount(0);
        this.children().addAll(this.keyEntries.stream().filter(entry ->
                // No query text or whitespace
                query.trim().isEmpty() ||
                        (
                        // Filter by
                        (
                                // No filter
                                (!query.contains("^u") && !query.contains("^c"))
                                        // Filter by unbound
                                        || ((query.contains("^u"))
                                        && entry instanceof JEKKeyEntry && ((JEKKeyEntry) entry).key.isUnbound())
                                        // Filter by conflicted
                                        || ((query.contains("^c"))
                                        && entry instanceof JEKKeyEntry && ((JEKKeyEntry) entry).isConflicted)
                        )
                        && // Search by
                        (
                                // No search query, just filters
                                filterParametersStrippedQuery.isEmpty()
                                // Search by name
                                || ((!query.contains("@c") && !query.contains("@k"))
                                        && entry instanceof JEKKeyEntry && ((JEKKeyEntry) entry)
                                        .name.toLowerCase(Locale.ROOT).contains(allParametersStrippedQuery))
                                // Search by category
                                || ((query.contains("@c"))
                                        && ((entry instanceof JKECategoryEntry
                                        && ((JKECategoryEntry) entry).labelText.toLowerCase(Locale.ROOT)
                                        .contains(allParametersStrippedQuery))
                                        || (entry instanceof JEKKeyEntry && I18n.get(((JEKKeyEntry) entry).key.getCategory())
                                        .toLowerCase(Locale.ROOT).contains(allParametersStrippedQuery))))
                                // Search by key
                                || ((query.contains("@k"))
                                        && (entry instanceof JEKKeyEntry
                                        && !((JEKKeyEntry) entry).key.isUnbound()
                                        && (Arrays.stream(((IJEKKeyMappingExtensions) ((JEKKeyEntry) entry).key)
                                                .jek$getKey().getDisplayName().getString().toLowerCase(Locale.ROOT).split(" "))
                                        .anyMatch(substring -> substring.startsWith(allParametersStrippedQuery))
                                        || (((IJEKKeyMappingExtensions) ((JEKKeyEntry) entry).key).jek$getModifierKeyMap().search(allParametersStrippedQuery))
                                        || ((JEKKeyEntry) entry).key.getTranslatedKeyMessage().getString().toLowerCase(Locale.ROOT).startsWith(allParametersStrippedQuery)
                                )))
        ))).sorted((entry1, entry2) -> {
            if (query.isEmpty()
                    || query.contains("@c")
                    || query.contains("^u")
                    || !(query.contains("@k") || query.contains("^c"))) {
                return 0;
            }

            assert entry1 instanceof JEKKeyEntry;
            String entry1DisplayText = (((JEKKeyEntry) entry1).key).getTranslatedKeyMessage().getString().toLowerCase(Locale.ROOT);
            String entry2DisplayText = (((JEKKeyEntry) entry2).key).getTranslatedKeyMessage().getString().toLowerCase(Locale.ROOT);

            if (entry1DisplayText.length() != entry2DisplayText.length()) {
                return Integer.compare(entry1DisplayText.length(), entry2DisplayText.length());
            }

            return entry1DisplayText.compareTo(entry2DisplayText);
        }).toList());
    }

    @Environment(EnvType.CLIENT)
    public class JKECategoryEntry extends KeyBindsList.Entry {
        private final String labelText;
        private final int labelWidth;
        private final String name;

        public JKECategoryEntry(String name) {
            this.labelText = I18n.get(name);
            this.labelWidth = minecraft.font.width(this.labelText);
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return ImmutableList.of();
        }

        public void render(GuiGraphics guiGraphics, int slotIndex, int y, int x, int rowLeft, int rowWidth, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            assert minecraft.screen != null;
            guiGraphics.drawString(minecraft.font, this.labelText, minecraft.screen.width / 2 - this.labelWidth / 2, y + rowWidth - 9 - 1, 16777215);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(new NarratableEntry() {// 81
                public NarrationPriority narrationPriority() {
                    return NarrationPriority.HOVERED;// 84
                }

                public void updateNarration(NarrationElementOutput narrationElementOutput) {
                    narrationElementOutput.add(NarratedElementType.TITLE, JKECategoryEntry.this.name);// 89
                }// 90
            });
        }

        @Override
        public void refreshEntry() {
        }
    }

    @Environment(EnvType.CLIENT)
    public class JEKKeyEntry extends KeyBindsList.Entry {
        private final KeyMapping key;
        private final Button changeButton;
        private final Button resetButton;
        private final String name;
        private boolean isConflicted = false;

        private JEKKeyEntry(final KeyMapping keyMapping) {
            this.key = keyMapping;
            this.name = I18n.get(keyMapping.getName());
            this.changeButton = Button.builder(Component.literal(name), button -> {
                        controlsScreen.selectedKey = keyMapping;
                        ((IJEKKeyMappingExtensions) keyMapping).jek$getModifierKeyMap().clear();
                        keyMapping.setKey(InputConstants.UNKNOWN);
                    })
                    .bounds(0, 0, 100, 20)
                    .createNarration(
                            supplier -> keyMapping.isUnbound()
                                    ? Component.translatable("narrator.controls.unbound", keyMapping.getName())
                                    : Component.translatable("narrator.controls.bound", keyMapping.getName(), supplier.get())
                    )
                    .build();

            this.resetButton = Button.builder(Component.translatable("controls.reset"), button -> {
                    minecraft.options.setKey(keyMapping, keyMapping.getDefaultKey());
                    resetMappingAndUpdateButtons();
                    ((IJEKKeyMappingExtensions) keyMapping).jek$getModifierKeyMap().set(((IJEKKeyMappingExtensions)keyMapping).jek$getDefaultModifierKeyMap());
                })
                    .bounds(0, 0, 50, 20)
                    .createNarration(
                            supplier -> Component.translatable("narrator.controls.reset", keyMapping.getName())
                    )
                    .build();
        }

        public String getCategory() {
            return this.key.getCategory();
        }

        @Override
        public void render(GuiGraphics guiGraphics, int slotIndex, int y, int x, int rowLeft, int rowWidth, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            int margin = 20;

            guiGraphics.drawString(minecraft.font, this.name, margin, y + rowWidth / 2, 16777215, false);
            this.resetButton.setX(width - (this.resetButton.getWidth() + margin + 45));
            this.resetButton.setY(y);
            this.resetButton.active = !this.key.isDefault();
            this.resetButton.render(guiGraphics, mouseX, mouseY, partialTicks);
            this.changeButton.setX(width - (this.resetButton.getWidth() + 5 + this.changeButton.getWidth() + margin + 45));
            this.changeButton.setY(y);
            this.changeButton.setMessage(this.key.getTranslatedKeyMessage());
            this.setChangeButtonMessageStyle();
            this.changeButton.render(guiGraphics, mouseX, mouseY, partialTicks);
        }

        private void setChangeButtonMessageStyle() {
            // If it is the selected key
            this.isConflicted = false;
            if (controlsScreen.selectedKey == this.key) {
                this.changeButton.setMessage((Component.literal("> "))
                        .append(this.changeButton.getMessage().copy().withStyle(ChatFormatting.YELLOW))
                        .append(" <").withStyle(ChatFormatting.YELLOW));
            } else if (this.key.isUnbound()) {
                this.changeButton.setMessage(this.changeButton.getMessage().copy().withStyle(ChatFormatting.GRAY));
                // If it is a conflicted key
            } else if (Arrays.stream(minecraft.options.keyMappings)
                    .anyMatch(keyMapping -> keyMapping != this.key && this.key.same(keyMapping))) {
                this.isConflicted = true;
                this.changeButton.setMessage(this.changeButton.getMessage().copy().withStyle(ChatFormatting.RED));
            }
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return ImmutableList.of(this.changeButton, this.resetButton);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(this.changeButton, this.resetButton);// 166
        }

        @Override
        public boolean mouseClicked(double d, double e, int i) {
            return super.mouseClicked(d, e, i)
                    || changeButton.mouseClicked(d, e, i)
                    || resetButton.mouseClicked(d, e, i);
        }

        @Override
        public void refreshEntry() {
        }
    }
}
