package md.maxoneryt.javacombat.combat.util;

import cn.nukkit.item.Item;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemChangeDetector {

    public static boolean isRealItemChange(Item previousItem, Item newItem) {
        if (previousItem == null && newItem == null) return false;
        if (previousItem == null || newItem == null) return true;

        return !previousItem.equals(newItem, true, false);
    }
}