package com.ispc.consorciomobile.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.scrollTo;

import com.ispc.consorciomobile.R;

public class LoginScreen {

    // Identificadores privados encapsulados
    private final int inputUsuarioId = R.id.inputUsuario;
    private final int inputPasswordId = R.id.inputPassword;
    private final int btnLoginId = R.id.buttonLogin;

    public LoginScreen ingresarUsuario(String usuario) {
        onView(withId(inputUsuarioId))
                .perform(typeText(usuario), closeSoftKeyboard());
        return this;
    }

    public LoginScreen ingresarPassword(String password) {
        onView(withId(inputPasswordId))
                .perform(typeText(password), closeSoftKeyboard());
        return this;
    }

    public LoginScreen clickBotonEntrar() {
        onView(withId(btnLoginId))
                .perform(closeSoftKeyboard(), scrollTo(), click());
        return this;
    }



    // Regla: sin Thread.sleep, Espresso valida la sincronización
    public LoginScreen verificarPermaneceEnLogin() {
        onView(withId(btnLoginId)).check(matches(isDisplayed()));
        return this;
    }
}