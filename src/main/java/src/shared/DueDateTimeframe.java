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
        switch(dueDateValue){
            case(1):
                return "day";
            case(7):
                return "week";
            case(31):
                return "month";
            case(365):
                return "year";
            case(100000):
                return "recurring";
            default:
                return "N/A";
        }
    }
}
