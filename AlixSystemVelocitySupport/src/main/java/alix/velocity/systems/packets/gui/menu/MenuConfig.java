package alix.velocity.systems.packets.gui.menu;

import alix.common.utils.config.alix.AlixYamlConfig;
import alix.common.utils.file.AlixFileManager;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A GUI menu's configurable title/background/items, loaded from "gui-menus/&lt;name&gt;.yml" in the
 * plugin's data folder (auto-created from a bundled default the first time it's needed) and cached in
 * memory afterward. A title/name/lore value starting with "@" is resolved as a language key (see
 * MenuItemDef#resolveText) so menu text follows the plugin's selected language like everything else.
 */
public final class MenuConfig {

    private static final ConcurrentMap<String, MenuConfig> CACHE = new ConcurrentHashMap<>();

    public static MenuConfig get(String name) {
        return CACHE.computeIfAbsent(name, MenuConfig::load);
    }

    private final String name;
    private final String title;
    private final ItemStack backgroundItem;
    private final List<MenuItemDef> items;

    private MenuConfig(String name, String title, ItemStack backgroundItem, List<MenuItemDef> items) {
        this.name = name;
        this.title = title;
        this.backgroundItem = backgroundItem;
        this.items = items;
    }

    private static MenuConfig load(String name) {
        AlixYamlConfig config = AlixYamlConfig.getOrCreatePluginFile("gui-menus/" + name + ".yml", AlixFileManager.FileType.CONFIG);

        String title = MenuItemDef.resolveText(config.getString("title", ""), name);
        ItemStack background = MenuItemDef.buildIcon(config, "background", ItemTypes.GRAY_STAINED_GLASS_PANE, " ");

        List<MenuItemDef> items = new ArrayList<>();
        for (String id : config.getStringList("item-order")) {
            if (id == null || id.isBlank()) continue;
            items.add(MenuItemDef.load(config, id.trim()));
        }

        return new MenuConfig(name, title, background, Collections.unmodifiableList(items));
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public ItemStack getBackgroundItem() {
        return backgroundItem;
    }

    public List<MenuItemDef> getItems() {
        return items;
    }

    /**
     * The slot(s) configured for a given "internal" item id (see MenuItemDef), or an empty array
     * if that id isn't placed anywhere in this menu's config.
     */
    public int[] getSlotsForInternal(String internalId) {
        for (MenuItemDef def : items) {
            if (def.isInternal() && def.getInternalId().equals(internalId)) return def.getSlots();
        }
        return new int[0];
    }
}
