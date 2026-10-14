package com.ispc.consorciomobile;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.ispc.consorciomobile.screens.LoginScreen;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    // Aísla LoginActivity para levantarlo directamente sin pasar por Splash
    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    // -------------------------------------------------------------------------
    // TC-L-03: Intento con campos vacíos
    // -------------------------------------------------------------------------
    @Test
    public void TC_L_03_login_camposVacios_noAvanzaYPermaneceEnLogin() {
        LoginScreen loginScreen = new LoginScreen();

        loginScreen
                .clickBotonEntrar()
                .verificarPermaneceEnLogin();
    }

    // -------------------------------------------------------------------------
    // TC-L-02: Intento con credenciales inválidas
    // -------------------------------------------------------------------------
    @Test
    public void TC_L_02_login_credencialesInvalidas_noAvanzaYPermaneceEnLogin() {
        LoginScreen loginScreen = new LoginScreen();

        loginScreen
                .ingresarUsuario("juan")
                .ingresarPassword("abdcefghijk")
                .clickBotonEntrar()
                .verificarPermaneceEnLogin();
    }
}