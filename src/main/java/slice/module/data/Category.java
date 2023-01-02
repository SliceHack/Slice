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

    public static Category translate(com.sliceclient.api.module.data.Category category) {
        switch (category) {
            case COMBAT: return COMBAT;
            case MOVEMENT: return MOVEMENT;
            case PLAYER: return PLAYER;
            case WORLD: return WORLD;
            case RENDER: return RENDER;
            case MISC: return MISC;
            default: return null;
        }
    }
}
