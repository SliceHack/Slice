package slice.module.modules.player;

import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.NumberValue;

@ModuleInfo(name = "Manager", description = "Manage your inventory", category = Category.PLAYER)
public class Manager extends Module {

    NumberValue swordSlot = new NumberValue("Sword Slot", 1, 1, 9, NumberValue.Type.INTEGER);
    NumberValue pickaxeSlot = new NumberValue("Pickaxe Slot", 2, 1, 9, NumberValue.Type.INTEGER);
    NumberValue axeSlot = new NumberValue("Axe Slot", 3, 1, 9, NumberValue.Type.INTEGER);
    NumberValue shovelSlot = new NumberValue("Shovel Slot", 4, 1, 9, NumberValue.Type.INTEGER);
}
