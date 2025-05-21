package src.shared;

public enum DueDateTimeframe {
    DAY(1),
    WEEK(7),
    MONTH(31),
    YEAR(365),
    RECURRING(100000);

    private final int dueDateValue;

    private DueDateTimeframe(int dueDateValue) {
        this.dueDateValue = dueDateValue;
    }

    public int toInt() {
        return dueDateValue;
    }

    @Override
    public String toString() {
        switch (dueDateValue) {
            case (1):
                return "DAY";
            case (7):
                return "WEEK";
            case (31):
                return "MONTH";
            case (365):
                return "YEAR";
            case (100000):
                return "RECURRING";
            default:
                return "N/A";
        }
    }

    public static Boolean contains(String value) {
        try {
            DueDateTimeframe.valueOf(value);
        } catch (Exception _) {
            return false;
        }

        return true;
    }
}
