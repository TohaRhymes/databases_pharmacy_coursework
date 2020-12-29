package se.ifmo.pepe.cwdb.backend.customtype;

public enum DrugsGroup {
    group_a_prohibited_substances("Group A (prohibited substances)"),
    group_b_limited_turnover("Group B (limited turnover)"),
    group_c_free_circulation("Group C (free circulation)");
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
