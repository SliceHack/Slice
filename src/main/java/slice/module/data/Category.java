package slice.module.data;

import lombok.Getter;

@Getter
public enum Category {
    COMBAT("Combat"), MOVEMENT("Movement"),
    PLAYER("Player"), WORLD("World"),
    RENDER("Render"), MISC("Misc");

    Category(String name) {
        this.name = name;
    }

    final String name;
}
