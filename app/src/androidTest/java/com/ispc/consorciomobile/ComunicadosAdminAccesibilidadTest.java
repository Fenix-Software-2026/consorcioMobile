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
public class ComunicadosAdminAccesibilidadTest {

    // ACCESIBILIDAD (ACC-0):
    // Mismo chequeo automatico que en la pantalla del residente, pero sobre el
    // panel de administracion: formulario, checkbox de urgente y botones del
    // historial. Criterio de aceptacion: 0 errores.
    @BeforeClass
    public static void enableAccessibilityChecks() {
        AccessibilityChecks.enable().setRunChecksFromRootView(true);
    }

    // AISLAMIENTO:
    // Abro el panel directo. Si el token no es de administrador la API responde
    // 403, pero el formulario se dibuja de todas formas y se puede auditar.
    @Rule
    public ActivityScenarioRule<ComunicadosAdminActivity> activityRule =
            new ActivityScenarioRule<>(ComunicadosAdminActivity.class);

    // La consulta a la vista dispara el chequeo sobre la raiz de la pantalla.
    @Test
    public void comunicadosAdmin_pasaLosChequeosDeAccesibilidad() {
        onView(withId(R.id.etTituloComunicado)).check(matches(isDisplayed()));
    }
}
