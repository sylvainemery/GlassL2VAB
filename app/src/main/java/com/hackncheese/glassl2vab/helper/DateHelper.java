// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.hackncheese.glassl2vab.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {

    public static final String FORMAT_DD_MM_YYYY_DOT = "dd.MM.yyyy";
    public static final String FORMAT_DD_MM_YYYY_HH_MM_DOT = "dd.MM.yyyy HH:mm";
    public static final String FORMAT_DD_MM_YYYY_HH_MM_SLASH = "dd/MM/yyyy-HH'h'mm";
    public static final String FORMAT_DD_MM_YYYY_HHhMM_HUMAN = "dd MMMM yyyy HH'h'mm";
    public static final String FORMAT_DD_MM_YYYY_HUMAN = "dd MMMM yyyy";
    public static final String FORMAT_DD_MM_YYYY_SLASH = "dd/MM/yyyy";
    public static final String FORMAT_DD_MM_YY_SLASH = "dd/MM/yy";
    public static final String FORMAT_HH_MM_SLASH = "HH:mm";
    public static final String FORMAT_HH_MM_SSS_CONCAT = "HHmmsss";
    public static final String FORMAT_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String FORMAT_MM_YYYY_HUMAN = "MMMM yyyy";
    public static final String FORMAT_YYYY_MM_DD_CONCAT = "yyyyMMdd";
    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS_SYSTEM = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYY_MM_DD_SLASH = "yyyy/MM/dd";
    public static final String FORMAT_YYYY_MM_DD_T_HH_MM_SS_SYSTEM = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String FORMAT_YYYY_MM_DD_UNION = "yyyy-MM-dd";
    private static final long HOURS_TO_MILLIS = 0x36ee80L;

    public DateHelper() {
    }

    public static Date getDateTime(long l) {
        return new Date(l);
    }

    public static String getFormattedNow(String s) {
        return getFormattedTime(System.currentTimeMillis(), s);
    }

    public static String getFormattedTime(long l, String s) {
        return (new SimpleDateFormat(s, Locale.FRANCE)).format(getDateTime(l));
    }

}