package vortex.imwp.models;

public class Country {
    public enum Name {
        AUSTRIA,
        BELGIUM,
        BULGARIA,
        CROATIA,
        CYPRUS,
        CZECHIA,
        DENMARK,
        ESTONIA,
        FINLAND,
        FRANCE,
        GERMANY,
        GREECE,
        HUNGARY,
        IRELAND,
        ITALY,
        LATVIA,
        LITHUANIA,
        LUXEMBOURG,
        MALTA,
        NETHERLANDS,
        POLAND,
        PORTUGAL,
        ROMANIA,
        SLOVAKIA,
        SLOVENIA,
        SPAIN,
        SWEDEN
    }

    public static Name fromString(String name) {
        for (Name n : Name.values()) {
            if (n.name().equals(name)) {return n;}
        }
        return null;
    }
}
