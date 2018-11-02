/*
 *  @author
 *  Nidhi Chourasia created on 2018
 *
 */
package com.utilities;

import org.joda.time.DateTime;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class TimeStampUtility {

    private static final DateTimeFormatter TEMPORAL_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss/SSS").withZone(ZoneOffset.UTC);
    private static final DateTimeFormatter SYSTEM_TIME_FORMAT = new DateTimeFormatterBuilder().appendInstant(3).toFormatter();
    private static final DateTimeFormatter UTC_TIME_WITH_SPECIALCHAR = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssSSS'Z'").withZone(ZoneOffset.UTC);

    public static void main(String[] args) {
        /*Instant is an offset since the origin (called an epoch)i.e Jan. 1st 1970 - 00:00 - Greenwhich mean time (GMT).
          Time measured using 86.400 seconds per day, moving forward from the origin*/

        Instant now = Instant.now();
        System.out.println("Instant--->" + now);

        createTemporalTimeStamp(now);

        //Instant is a point of time, it doesn't store any TimeZone information and as such it supports only UTC formatted strings
        createInstantFromTimestamp("2018-10-23T10:12:35Z");

        createUTCFromInstant(now);

        createSystemTimestampInUTC_ISO(now);

        createUTCWithSpecialChar(now);
    }


    public static void createTemporalTimeStamp(Instant instant) {
        String temporalFormat = TEMPORAL_TIME_FORMAT.format(instant);
        System.out.println("Temporal format--->" + temporalFormat);
    }

    public static void createInstantFromTimestamp(String temporal) {
        Instant instant = Instant.ofEpochMilli((DateTime.parse(temporal)).getMillis());
        System.out.println(instant);
    }

    public static void createSystemTimestampInUTC_ISO(Instant instant) {
        String inUTC_ISO = SYSTEM_TIME_FORMAT.format(instant);
        System.out.println(inUTC_ISO);
    }

    public static void createUTCFromInstant(Instant instant) {
        String systemTimeInISO = SYSTEM_TIME_FORMAT.format(instant);
        System.out.println(systemTimeInISO);
    }

    public static void createUTCWithSpecialChar(Instant instant){
        String format = UTC_TIME_WITH_SPECIALCHAR.format(instant);
        System.out.println(format);
    }


}
