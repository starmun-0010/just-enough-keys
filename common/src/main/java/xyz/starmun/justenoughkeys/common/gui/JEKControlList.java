package xyz.starmun.justenoughkeys.common.gui;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.ArrayUtils;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;
import xyz.starmun.justenoughkeys.platform.TooltipUtilsExpectPlatform;

import java.util.*;
import java.util.stream.Collectors;

public class JEKControlList extends KeyBindsList {
    private final KeyBindsScreen controlsScreen;
    private int maxListLabelWidth;
    private List<Entry> keyEntries = new ArrayList<>();

    public JEKControlList(KeyBindsScreen controlsScreen, Minecraft minecraft) {

        super(controlsScreen, minecraft);
        this.width = controlsScreen.width + 45;
        this.height = controlsScreen.height;
        this.y0 = 43;
        this.y1 = controlsScreen.height - 60;
        this.x1 = controlsScreen.width + 45;
        this.controlsScreen = controlsScreen;
        this.children().clear();
        this.initKeyMappings();

    }

    @Override
    protected void renderDecorations(PoseStack poseStack, int mouseX, int mouseY) {
        Entry entry = this.getJEKKeyEntryAtPos(mouseY);
        if(!(entry instanceof JEKKeyEntry)) {
            return;
        }
        JEKKeyEntry keyEntry = (JEKKeyEntry) entry;
        TooltipUtilsExpectPlatform.renderTooltip(new TranslatableComponent(keyEntry.getCategory()), poseStack,mouseX,mouseY,minecraft.screen.width,minecraft.screen.height, minecraft.font);

    }

    public Entry getJEKKeyEntryAtPos(double mouseY) {
        if(mouseY <=  y0 || mouseY >= y1) {
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
                //No query text or whitespace
                query.trim().isEmpty() ||
                        (
                                //Filter by
                                (
                                        //no filter
                                        (!query.contains("^u") && ! query.contains("^c"))
                                                //Filter by unbound
                                                || ((query.startsWith("^u") || query.contains(" ^u ")) && entry instanceof JEKKeyEntry && ((JEKKeyEntry) entry).key.isUnbound())
                                                //Filter by conflicted
                                                || ((query.startsWith("^c") || query.contains(" ^c ")) && entry instanceof JEKKeyEntry && ((JEKKeyEntry) entry).isConflicted)

                                )
                                        && //Search by
                                        (
                                                //no search query, just filters
                                                filterParametersStrippedQuery.isEmpty()
                                                        //search by name
                                                        || ((!query.contains("@c") && !query.contains("@k")) && entry instanceof JEKKeyEntry && ((JEKKeyEntry) entry).name.toLowerCase(Locale.ROOT).contains(allParametersStrippedQuery))
                                                        //search by category
                                                        ||((query.startsWith("@c ") || query.contains(" @c ")) && ((entry instanceof JKECategoryEntry && ((JKECategoryEntry) entry).labelText.toLowerCase(Locale.ROOT).contains(allParametersStrippedQuery)) || (entry instanceof JEKKeyEntry && I18n.get(((JEKKeyEntry) entry).key.getCategory()).toLowerCase(Locale.ROOT).contains(allParametersStrippedQuery))))
                                                        //search by key
                                                        ||((query.startsWith("@k ") || query.contains(" @k ")) && (entry instanceof JEKKeyEntry && !((JEKKeyEntry) entry).key.isUnbound() && (((IJEKKeyMappingExtensions)((JEKKeyEntry) entry).key).jek$getKey().getDisplayName().getString().toLowerCase(Locale.ROOT).contains(allParametersStrippedQuery) || (((IJEKKeyMappingExtensions)((JEKKeyEntry) entry).key).jek$getModifierKeyMap().search(allParametersStrippedQuery)))))

                                        ))).collect(Collectors.toList()));
    }


    public class JKECategoryEntry extends KeyBindsList.Entry {
        private final String labelText;
        private final int labelWidth;
        private final String name;

        public JKECategoryEntry(String name) {
            this.labelText = I18n.get(name);
            this.labelWidth = JEKControlList.this.minecraft.font.width(this.labelText);
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return ImmutableList.of();
        }

        public void render(PoseStack stack, int slotIndex, int y, int x, int rowLeft, int rowWidth, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            JEKControlList.this.minecraft.font.draw(stack, this.labelText, (float) (JEKControlList.this.minecraft.screen.width / 2 - this.labelWidth / 2), (float) (y + rowWidth - 9 - 1), 16777215);
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
    }

    public class JEKKeyEntry extends KeyBindsList.Entry {

        private final KeyMapping key;
        private final Button changeButton;
        private final Button resetButton;
        private final String name;
        private boolean isConflicted = false;

        private JEKKeyEntry(final KeyMapping keyMapping) {
            this.key = keyMapping;
            this.name = I18n.get(keyMapping.getName());
            this.changeButton = new Button(0, 0, 100, 20, new TextComponent(name), (button) -> {
                JEKControlList.this.controlsScreen.selectedKey = keyMapping;
                ((IJEKKeyMappingExtensions) keyMapping).jek$getModifierKeyMap().clear();
                keyMapping.setKey(InputConstants.UNKNOWN);
            }) {
                @Override
                protected MutableComponent createNarrationMessage() {
                    return keyMapping.isUnbound() ? new TranslatableComponent("narrator.controls.unbound", JEKKeyEntry.this.name) : new TranslatableComponent("narrator.controls.bound", JEKKeyEntry.this.name, super.createNarrationMessage());
                }
            };
            this.resetButton = new Button(0, 0, 50, 20, new TranslatableComponent("controls.reset"), (button) -> {
                JEKControlList.this.minecraft.options.setKey(keyMapping, keyMapping.getDefaultKey());
                IJEKKeyMappingExtensions.resetMapping();
                ((IJEKKeyMappingExtensions) keyMapping).jek$getModifierKeyMap().clear();
                ((IJEKKeyMappingExtensions) keyMapping).jek$getModifierKeyMap().set(((IJEKKeyMappingExtensions)keyMapping).getPlatformDefaultModifierKey(),true);

            }) {
                @Override
                protected MutableComponent createNarrationMessage() {
                    return new TranslatableComponent("narrator.controls.reset", JEKKeyEntry.this.name);
                }
            };
        }

        public String getCategory(){
            return this.key.getCategory();
        }
        @Override
        public void render(PoseStack poseStack, int slotIndex, int y, int x, int rowLeft, int rowWidth, int mouseX, int mouseY, boolean hovered, float partialTicks) {

            int margin = 20;
            JEKControlList.this.minecraft.font.getClass();
            JEKControlList.this.minecraft.font.draw(poseStack, this.name, (float) margin, (float) (y + rowWidth / 2), 16777215);
            this.resetButton.x = width - (this.resetButton.getWidth() + margin + 45);
            this.resetButton.y = y;
            this.resetButton.active = !this.key.isDefault();
            this.resetButton.render(poseStack, mouseX, mouseY, partialTicks);
            this.changeButton.x = width - (this.resetButton.getWidth() + 5 + this.changeButton.getWidth() + margin + 45);
            this.changeButton.y = y;
            this.changeButton.setMessage(this.key.getTranslatedKeyMessage());
            this.setChangeButtonMessageStyle();
            this.changeButton.render(poseStack, mouseX, mouseY, partialTicks);
        }

        private void setChangeButtonMessageStyle() {
            //If it is the selected key
            this.isConflicted = false;
            if (JEKControlList.this.controlsScreen.selectedKey == this.key) {
                this.changeButton.setMessage((new TextComponent("> "))
                        .append(this.changeButton.getMessage().copy().withStyle(ChatFormatting.YELLOW))
                        .append(" <").withStyle(ChatFormatting.YELLOW));
            } else if (this.key.isUnbound()) {
                this.changeButton.setMessage(this.changeButton.getMessage().copy().withStyle(ChatFormatting.GRAY));
                //if it is a conflicted key
            } else if (Arrays.stream(JEKControlList.this.minecraft.options.keyMappings)
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
        public boolean mouseClicked(double d, double e, int i) {
            if (super.mouseClicked(d, e, i)) return true;
            if (changeButton.mouseClicked(d, e, i)) return true;
            else return resetButton.mouseClicked(d, e, i);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(this.changeButton, this.resetButton);// 166
        }
    }
}
