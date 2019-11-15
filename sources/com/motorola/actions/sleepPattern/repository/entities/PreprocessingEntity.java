package com.motorola.actions.sleepPattern.repository.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@Entity(tableName = "preprocessing_results")
public class PreprocessingEntity {
    @Ignore
    private static final String SEPARATOR = " ";
    @ColumnInfo(name = "results")
    private String mResults;
    @PrimaryKey
    @ColumnInfo(name = "type")
    private int mType;

    public PreprocessingEntity(int i, String str) {
        this.mType = i;
        this.mResults = str;
    }

    public int getType() {
        return this.mType;
    }

    public void setType(int i) {
        this.mType = i;
    }

    public String getResults() {
        return this.mResults;
    }

    public void setResults(String str) {
        this.mResults = str;
    }

    @Ignore
    public static String setResultsAsList(List<Double> list) {
        StringBuilder sb = new StringBuilder();
        NumberFormat instance = NumberFormat.getInstance(Locale.ENGLISH);
        instance.setMaximumFractionDigits(4);
        for (Double doubleValue : list) {
            sb.append(instance.format(doubleValue.doubleValue()));
            sb.append(SEPARATOR);
        }
        return sb.toString();
    }

    @Ignore
    public List<Double> getResultsAsList() {
        LinkedList linkedList = new LinkedList();
        for (String parseDouble : this.mResults.split(SEPARATOR)) {
            linkedList.add(Double.valueOf(Double.parseDouble(parseDouble)));
        }
        return linkedList;
    }
}
