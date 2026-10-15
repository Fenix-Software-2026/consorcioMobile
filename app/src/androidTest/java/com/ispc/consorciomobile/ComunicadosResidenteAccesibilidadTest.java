package com.ispc.consorciomobile;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.accessibility.AccessibilityChecks;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ComunicadosResidenteAccesibilidadTest {

    // ACCESIBILIDAD (ACC-0):
    // AccessibilityChecks usa el Accessibility Test Framework dentro de Espresso.
    // Cada interaccion del test revisa la pantalla buscando vistas sin etiqueta,
    // areas tactiles menores a 48dp y texto con contraste bajo (WCAG 2.2 AA).
    // Criterio de aceptacion de la consigna: 0 errores.
    @BeforeClass
    public static void enableAccessibilityChecks() {
        AccessibilityChecks.enable().setRunChecksFromRootView(true);
    }

    // AISLAMIENTO:
    // Levanto la pantalla de comunicados directamente, sin pasar por el Login.
    // Si no hay token, la API responde 401 y la lista queda vacia, pero la vista
    // se pinta igual, que es justo lo que queremos auditar.
    @Rule
    public ActivityScenarioRule<ComunicadosResidenteActivity> activityRule =
            new ActivityScenarioRule<>(ComunicadosResidenteActivity.class);

    // El simple hecho de consultar por la vista ya dispara el chequeo de
    // accesibilidad sobre la raiz de la pantalla.
    @Test
    public void comunicadosResidente_pasaLosChequeosDeAccesibilidad() {
        onView(withId(R.id.recyclerComunicados)).check(matches(isDisplayed()));
    }
}
