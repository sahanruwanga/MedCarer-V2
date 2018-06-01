package com.sahanruwanga.medcarer.app;


import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Sahan Ruwanga on 5/30/2018.
 */
public class UserTest {

    @Test
    public void calculateAge() throws Exception {
        User user = new User();
        String result = user.calculateAge("1994-09-09");
        String expected = "24 years";
        assertEquals(expected, result);
    }
}