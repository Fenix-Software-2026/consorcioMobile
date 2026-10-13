package com.ispc.consorciomobile.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import com.ispc.consorciomobile.R;

public class MiPerfilScreen {

    // Dejo los R.id privados acá arriba para que, si el día de mañana le cambian el nombre
    // a la vista en el XML, solo toco este archivo y no rompo todo.
    private final int etContrasenaActualId = R.id.etContrasenaActual;
    private final int etNuevaContrasenaId = R.id.etNuevaContrasena;
    private final int etRepetirContrasenaId = R.id.etRepetirContrasena;
    private final int btnActualizarContrasenaId = R.id.btnActualizarContrasena;

    // Hago que los métodos devuelvan "MiPerfilScreen" (return this;) en lugar de void.
    // Esto se llama "Fluent Interface" y me permite encadenar acciones en el test
    public MiPerfilScreen ingresarContrasenaActual(String password) {
        onView(withId(etContrasenaActualId))
                .perform(typeText(password), closeSoftKeyboard());// Cierro el teclado para que no me tape el botón de abajo
        return this;
    }

    public MiPerfilScreen ingresarNuevaContrasena(String password) {
        onView(withId(etNuevaContrasenaId))
                .perform(typeText(password), closeSoftKeyboard());
        return this;
    }

    public MiPerfilScreen repetirNuevaContrasena(String password) {
        onView(withId(etRepetirContrasenaId))
                .perform(typeText(password), closeSoftKeyboard());
        return this;
    }

    public MiPerfilScreen clickActualizarContrasena() {
        onView(withId(btnActualizarContrasenaId)).perform(click());
        return this;
    }
    // Nunca hay que usar Thread.sleep() porque rompe el determinismo del test.
    // Espresso se sincroniza solo. Acá le digo que simplemente mire si el botón
    // sigue ahí en pantalla para confirmar que la app no crasheó al fallar la validación.
    public MiPerfilScreen verificarPantallaVisible() {
        onView(withId(btnActualizarContrasenaId)).check(matches(isDisplayed()));
        return this;
    }
}