package com.motorola.actions.sleepPattern.preprocessing;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Calculator {
    static double getElementZScore(int i, double d, double d2) {
        return (((double) i) - d) / d2;
    }

    static double calculateMean(Collection<Integer> collection) {
        double d = 0.0d;
        if (collection.isEmpty()) {
            return 0.0d;
        }
        for (Integer intValue : collection) {
            d += (double) intValue.intValue();
        }
        return d / ((double) collection.size());
    }

    static double calculateStdDeviation(Collection<Integer> collection, double d) {
        double d2 = 0.0d;
        if (collection.isEmpty()) {
            return 0.0d;
        }
        for (Integer intValue : collection) {
            d2 += Math.pow(((double) intValue.intValue()) - d, 2.0d);
        }
        return Math.sqrt(d2 / ((double) collection.size()));
    }

    static List<Double> generateZScoreList(Collection<Integer> collection, double d, double d2) {
        LinkedList linkedList = new LinkedList();
        for (Integer intValue : collection) {
            linkedList.add(Double.valueOf(getElementZScore(intValue.intValue(), d, d2)));
        }
        return linkedList;
    }
}
