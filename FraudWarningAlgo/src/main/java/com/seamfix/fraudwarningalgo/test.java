package com.seamfix.fraudwarningalgo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class test {

        public static void main(String[] args) {
            int noOfNotifications = getNumberOfNotifications(
                    "9 5",
                    "2 3 4 2 3 6 8 4 5"
            );
            System.out.println("1. Number of notifications: " + noOfNotifications);
            assertValues(2, noOfNotifications);

            noOfNotifications = getNumberOfNotifications(
                    "5 2",
                    "3 6 7 1 3"
            );
            System.out.println("2. Number of notifications: " + noOfNotifications);
            assertValues(0, noOfNotifications);
        }

        private static void assertValues(Object expected, Object actual) {
            if(!expected.equals(actual)) {
                throw new RuntimeException(String.format("Expected value %s is not equal to actual value %s.", expected, actual));
            }
        }

        private static int getNumberOfNotifications(String daysText, String expendituresText) {
            List<Integer> days = getSpaceSeparatedNumbers(daysText);
            int noOfTransactionsDays = days.get(0);
            int daysRequiredForNotifying = days.get(1);

            List<Integer> expenditures = getSpaceSeparatedNumbers(expendituresText);
            if (noOfTransactionsDays != expenditures.size()) {
                throw new RuntimeException("Number of days does not match supplied days information");
            }

            //2 3 4 2 3 6 8 4 5
            int noOfNotifications = 0;
            for (int currentDay = daysRequiredForNotifying + 1; currentDay <= noOfTransactionsDays; currentDay++) {
                int currentDayExpenditure = expenditures.get(currentDay - 1);
                int medianStartDay = currentDay - daysRequiredForNotifying;
                int medianEndDay = currentDay - 1;

                int doubledMedianExpenditure = getDoubledMedianExpenditureForPastDays(
                        expenditures, medianStartDay, medianEndDay
                );

                if (currentDayExpenditure >= doubledMedianExpenditure) {
                    noOfNotifications++;
                }
            }

            return noOfNotifications;
        }

        private static List<Integer> getSpaceSeparatedNumbers(String spaceSeparatedNumbersText) {
            String[] allSpaceSeparatedNumbers = spaceSeparatedNumbersText.split("\\s+");

            return Arrays.stream(allSpaceSeparatedNumbers)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }

        private static int getDoubledMedianExpenditureForPastDays(
                List<Integer> allExpenditures,
                int medianStartDay,
                int medianEndDay
        ) {
            List<Integer> expendituresForMedian = new ArrayList<>();
            for (int i = medianStartDay - 1; i < medianEndDay; i++) {
                int expenditureToConsider = allExpenditures.get(i);
                expendituresForMedian.add(expenditureToConsider);
            }
            expendituresForMedian.sort(Comparator.comparingInt(e -> e));

            int size = expendituresForMedian.size();
            int middleIndex = (int) Math.ceil((double) size / 2) - 1;
            boolean isNotifyingDaysEven = size % 2 == 0;

            //2 3 4 2 3 6 8 4 5
            if (isNotifyingDaysEven) {
                int expenditureAtMiddle = expendituresForMedian.get(middleIndex);
                int expenditureAtOtherMiddle = expendituresForMedian.get(middleIndex + 1);
                return expenditureAtMiddle + expenditureAtOtherMiddle;
            }
            else {
                return expendituresForMedian.get(middleIndex) * 2;
            }
        }

}
