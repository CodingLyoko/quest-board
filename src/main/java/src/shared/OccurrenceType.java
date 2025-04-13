package src.shared;

import java.time.YearMonth;
import java.util.Calendar;

public enum OccurrenceType {
    RECURRING("Recurring"),
    ONCE("Once"),
    DAILY("Daily"),
    WEEKLY("Weekly"),
    BIWEEKLY("Biweekly"),
    MONTHLY("Monthly"),
    BIMONTHLY("Bimonthly"),
    QUARTERLY("Quarterly"),
    BIANNUAL("Biannual"),
    YEARLY("Yearly");

    private final String occurrenceTypeValue;

    private OccurrenceType(String occurenceType) {
        this.occurrenceTypeValue = occurenceType;
    }

    @Override
    public String toString() {
        return occurrenceTypeValue;
    }

    public int toInt() {
        // Used to calculate the number of Days to add to a Quest's Due Date
        Calendar cal = Calendar.getInstance();
        YearMonth ym = YearMonth.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);

        switch (occurrenceTypeValue) {
            case ("Recurring"):
                return 0;
            case ("Daily"):
                return 1;
            case ("Weekly"):
                return 7;
            case ("Biweekly"):
                return 14;
            case ("Monthly"):
                return ym.lengthOfMonth();
            case ("Bimonthly"):
                return daysInMonths(ym, 2);
            case ("Quarterly"):
                return daysInMonths(ym, 3);
            case ("Biannual"):
                return daysInMonths(ym, 6);
            case ("Yearly"):
                return ym.lengthOfYear();
            default:
                return 0;
        }
    }

    /**
     * Calculate the number of days within a given number of months (past the
     * current month).
     * 
     * @param ym          - the YearMonth object being used to calculate the number
     *                    of days
     * @param numOfMonths - the number of months past the current month
     * @return
     */
    private int daysInMonths(YearMonth ym, int numOfMonths) {
        int totalDays = 0;

        for (int i = 0; i < numOfMonths; i++) {
            totalDays += ym.lengthOfMonth();
            ym = ym.plusMonths(1); // Advance to next month
        }

        return totalDays;
    }
}
