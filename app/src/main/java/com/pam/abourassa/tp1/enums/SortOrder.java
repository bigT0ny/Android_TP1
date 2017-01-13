package com.pam.abourassa.tp1.enums;

/**
 * Created by Anthony on 09/12/2016.
 */

/**
 * Enum servant pour le tri ascendant et descendant des pays.
 */
public enum SortOrder {
    ASC(0, "ASC"),
    DESC(1, "DESC");

    private int value;
    private String sqlValue;

    public int getValue () {
        return value;
    }

    public String getSqlValue () {
        return sqlValue;
    }

    SortOrder(int value, String sqlValue) {
        this.value = value;
        this.sqlValue = sqlValue;
    }

    public static SortOrder createFromInt(int value)  {
        SortOrder sortOrder = SortOrder.ASC;
        if (value == 1) {
            sortOrder = SortOrder.DESC;
        }

        return sortOrder;
    }

}
