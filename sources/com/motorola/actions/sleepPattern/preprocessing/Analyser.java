package com.motorola.actions.sleepPattern.preprocessing;

import com.motorola.actions.utils.MALogger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;

class Analyser {
    private static final MALogger LOGGER = new MALogger(Analyser.class);
    private List<Double> mZScoreList = new LinkedList();
    private double mZScoreTolerance;

    Analyser() {
    }

    private void calculate(Collection<Integer> collection) {
        double calculateMean = Calculator.calculateMean(collection);
        double calculateStdDeviation = Calculator.calculateStdDeviation(collection, calculateMean);
        this.mZScoreList = Calculator.generateZScoreList(collection, calculateMean, calculateStdDeviation);
        double elementZScore = Calculator.getElementZScore(0, calculateMean, calculateStdDeviation);
        this.mZScoreTolerance = elementZScore + (-1.0d * elementZScore * 0.15d);
    }

    /* access modifiers changed from: 0000 */
    public List<Calendar> identifySleepPeriod(SortedMap<Calendar, Integer> sortedMap) {
        calculate(sortedMap.values());
        List lowActivityRanges = getLowActivityRanges(this.mZScoreList);
        if (lowActivityRanges.size() > 0) {
            return getMaximumRange(lowActivityRanges, new ArrayList(sortedMap.keySet()));
        }
        return new LinkedList();
    }

    private List<List<Integer>> getLowActivityRanges(List<Double> list) {
        LinkedList linkedList = new LinkedList();
        boolean z = false;
        for (int i = 0; i < list.size(); i++) {
            if (((Double) list.get(i)).doubleValue() <= this.mZScoreTolerance) {
                if (!z) {
                    LinkedList linkedList2 = new LinkedList();
                    linkedList2.add(Integer.valueOf(i));
                    linkedList.add(linkedList2);
                }
                z = true;
            } else if (((Double) list.get(i)).doubleValue() > this.mZScoreTolerance) {
                if (z) {
                    ((List) linkedList.get(linkedList.size() - 1)).add(Integer.valueOf(i));
                }
                z = false;
            }
        }
        if (z) {
            ((List) linkedList.get(linkedList.size() - 1)).add(Integer.valueOf(list.size() - 1));
        }
        return linkedList;
    }

    private List<Calendar> getMaximumRange(List<List<Integer>> list, ArrayList<Calendar> arrayList) {
        Calendar calendar = (Calendar) arrayList.get(0);
        Calendar calendar2 = (Calendar) arrayList.get(0);
        LinkedList linkedList = new LinkedList();
        float f = 0.0f;
        for (List list2 : list) {
            if (list2.size() == 2) {
                float timeInMillis = (float) (((Calendar) arrayList.get(((Integer) list2.get(1)).intValue())).getTimeInMillis() - ((Calendar) arrayList.get(((Integer) list2.get(0)).intValue())).getTimeInMillis());
                if (timeInMillis > f) {
                    calendar = (Calendar) arrayList.get(((Integer) list2.get(0)).intValue());
                    calendar2 = (Calendar) arrayList.get(((Integer) list2.get(1)).intValue());
                    f = timeInMillis;
                }
            } else {
                LOGGER.mo11959e("getMaximumRange - Not a valid range...");
            }
        }
        linkedList.add(calendar);
        linkedList.add(calendar2);
        return linkedList;
    }
}
