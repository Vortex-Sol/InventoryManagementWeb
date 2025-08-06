package vortex.imwp.Models;

public class ReportType {
    public enum Type {
        SALES,
        EMPLOYEES,
        INVENTORY,
        GENERAL
    }

    public static Type fromString(String type) {
        for (Type t : Type.values()) {
            if (t.name().equalsIgnoreCase(type)) return t;
        }
        return null;
    }
}
