package com.sahanruwanga.medcarer.activity;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * Created by Sahan Ruwanga on 3/25/2018.
 */
public class LoginActivityTest {
    @Test
    public void checkLogin() throws Exception {
        LoginActivity loginActivity = Mockito.mock(LoginActivity.class);
        Mockito.verify(loginActivity).checkLogin("Sahan@123.com","1234ttt");
    }

    @Test
    public void onCreate() throws Exception {
    }

    @Test
    public void doLogin() throws Exception {

    }

    @Test
    public void openRegisterActivity() throws Exception {
    }

}