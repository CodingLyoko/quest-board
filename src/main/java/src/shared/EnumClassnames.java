package src.shared;

public enum EnumClassnames {
    OCCURRENCE_TYPE("src.shared.OccurrenceType"),
    DUE_DATE("src.shared.DueDateTimeframe");

    private final String enumClassname;

    private EnumClassnames(String enumClassname) {
        this.enumClassname = enumClassname;
    }

    @Override
    public String toString() {
        return enumClassname;
    }
}
