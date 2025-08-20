package vortex.imwp.models;

public class Country {
    public enum Name {
        DENMARK,
        IRELAND,
        POLAND,
        AUSTRIA,
        GERMANY,
        GREECE
    }

    public static Name fromString(String name) {
        for (Name n : Name.values()) {
            if (n.name().equals(name)) {return n;}
        }
        return null;
    }
}
