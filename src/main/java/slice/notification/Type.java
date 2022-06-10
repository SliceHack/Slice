package slice.notification;

import lombok.Getter;

@Getter
public enum Type {
    INFO("Info"),
    WARNING("Warning"),
    ERROR("Error");

    Type(String name) {
        this.name = name;
    }

    final String name;
}
