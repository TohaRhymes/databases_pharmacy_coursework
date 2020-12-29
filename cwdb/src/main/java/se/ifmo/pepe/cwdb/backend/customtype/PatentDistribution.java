package se.ifmo.pepe.cwdb.backend.customtype;

public enum PatentDistribution {
    free_to_use("free_to_use"),
    usage_with_constraints("usage_with_constraints"),
    restricted_to_use("restricted_to_use");
    final private String key;

    PatentDistribution(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static PatentDistribution parse(String code) {
        PatentDistribution right = null; // Default
        for (PatentDistribution item : PatentDistribution.values()) {
            if (item.getKey().equals(code)) {
                right = item;
                break;
            }
        }
        return right;
    }
}
