package com.sahanruwanga.medcarer.helper;

import org.junit.Test;

import static org.junit.Assert.*;

public class DateTimeFormattingTest {
    @Test
    public void getDateTimeToShowInUI() throws Exception {
        DateTimeFormatting dateTimeFormatting = new DateTimeFormatting();
        String result = dateTimeFormatting.getDateTimeToShowInUI("2018-04-28 10:25:00");
        String expected = "Apr 28, 2018 10:25 AM";
        assertEquals(expected, result);
    }

    @Test
    public void getDateTimeToSaveInDB() throws Exception {
        DateTimeFormatting dateTimeFormatting = new DateTimeFormatting();
        String result = dateTimeFormatting.getDateTimeToSaveInDB("Apr 28, 2018 10:25 AM");
        String expected = "2018-04-28 10:25:00";
        assertEquals(expected, result);
    }

    @Test
    public void getDateToSaveInDB() throws Exception {
        DateTimeFormatting dateTimeFormatting = new DateTimeFormatting();
        String result = dateTimeFormatting.getDateToSaveInDB("Apr 28, 2018");
        String expected = "2018-04-28";
        assertEquals(expected, result);
    }

    @Test
    public void getDateToShowInUI() throws Exception {
        DateTimeFormatting dateTimeFormatting = new DateTimeFormatting();
        String result = dateTimeFormatting.getDateToShowInUI("2018-04-28");
        String expected = "Apr 28, 2018";
        assertEquals(expected, result);
    }

    @Test
    public void getTimeToSaveInDB() throws Exception {
        DateTimeFormatting dateTimeFormatting = new DateTimeFormatting();
        String result = dateTimeFormatting.getTimeToSaveInDB("10:25 AM");
        String expected = "10:25:00";
        assertEquals(expected, result);
    }

    @Test
    public void getTimeToShowInUI() throws Exception {
        DateTimeFormatting dateTimeFormatting = new DateTimeFormatting();
        String result = dateTimeFormatting.getTimeToShowInUI("10:25:00");
        String expected = "10:25 AM";
        assertEquals(expected, result);
    }

    @Test
    public void getPeriodFormatFromDB() throws Exception {
        DateTimeFormatting dateTimeFormatting = new DateTimeFormatting();
        String result = dateTimeFormatting.getPeriodFormatFromDB("10:25:00");
        String expected = "10 day 25 hour 00 min";
        assertEquals(expected, result);
    }

    @Test
    public void getPeriodFormatFromUI() throws Exception {
        DateTimeFormatting dateTimeFormatting = new DateTimeFormatting();
        String result = dateTimeFormatting.getPeriodFormatFromUI("102500");
        String expected = "10 day 25 hour 00 min";
        assertEquals(expected, result);
    }

    @Test
    public void getNotifyTimeInSHowinFormat() throws Exception {
        DateTimeFormatting dateTimeFormatting = new DateTimeFormatting();
        String result = dateTimeFormatting.getNotifyTimeInSHowinFormat("10");
        String expected = "10 mins";
        assertEquals(expected, result);
    }

    @Test
    public void getNextTimeToTakeMedicine() throws Exception {
        DateTimeFormatting dateTimeFormatting = new DateTimeFormatting();
        String result = dateTimeFormatting.getNextTimeToTakeMedicine("2018-04-28 10:25:00", "50");
        String expected = "Apr 28, 2018 - 11:15 AM";
        assertEquals(expected, result);
    }

}