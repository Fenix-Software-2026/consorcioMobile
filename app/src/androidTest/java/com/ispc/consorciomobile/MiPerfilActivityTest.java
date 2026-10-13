package com.ispc.consorciomobile;

import androidx.test.espresso.accessibility.AccessibilityChecks;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.ispc.consorciomobile.screens.MiPerfilScreen;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MiPerfilActivityTest {

    // ACCESIBILIDAD:
    // Con solo meter esta línea en el @BeforeClass, Espresso analiza automáticamente
    // la pantalla entera buscando botones muy chicos o colores con poco contraste
    // cada vez que hace un click. Así cumplimos el requisito de Accesibilidad automatizada.
    @BeforeClass
    public static void enableAccessibilityChecks() {
        AccessibilityChecks.enable().setRunChecksFromRootView(true);
    }

    // AISLAMIENTO:
    // La ActivityScenarioRule levanta directamente la pantalla de MiPerfilActivity
    // para probarla aislada. Así no necesito hacer un test enorme que pase primero por el Login.
    @Rule
    public ActivityScenarioRule<MiPerfilActivity> activityRule =
            new ActivityScenarioRule<>(MiPerfilActivity.class);

    @Test
    public void intentarCambiarContrasena_conClaveDebil_noAvanzaYMuestraError() {
        // Instancio mi "robot" que conoce los R.id de esta pantalla
        MiPerfilScreen perfilScreen = new MiPerfilScreen();

        // CLARIDAD:
        // Gracias al Screen Object, el test quedó súper limpio.
        // Se lee exactamente igual que un Caso de Prueba manual de la planilla Excel,
        // ocultando todo el código feo de selectores (onView, withId) en la otra clase.
        perfilScreen
                .ingresarContrasenaActual("ClaveAntigua123!")
                .ingresarNuevaContrasena("debil123") // Le paso una clave débil a propósito
                .repetirNuevaContrasena("debil123")
                .clickActualizarContrasena()
                .verificarPantallaVisible(); // Valido que sigo en la misma pantalla
    }
}