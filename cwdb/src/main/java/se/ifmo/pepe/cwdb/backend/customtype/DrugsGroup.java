package se.ifmo.pepe.cwdb.backend.customtype;

public enum DrugsGroup {
    GROUP_A("Group A (prohibited substances)"),
    GROUP_B("Group B (limited turnover)"),
    GROUP_C("Group C (free circulation)");
    private final String code;

    DrugsGroup(String s) {
        this.code = s;
    }

    public String getCode() {
        return code;
    }

    public static DrugsGroup parse(String code) {
        DrugsGroup right = null; // Default
        for (DrugsGroup item : DrugsGroup.values()) {
            if (item.getCode().equals(code)) {
                right = item;
                break;
            }
        }
        return right;
    }
}
