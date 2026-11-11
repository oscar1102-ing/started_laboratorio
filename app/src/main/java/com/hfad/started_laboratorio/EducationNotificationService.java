package com.hfad.started_laboratorio;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Parte 2 (funcionalidad adicional): notificaciones del contexto educativo.
 *  - TYPE_GRADE:      el estudiante recibe la calificación de un taller o parcial.
 *  - TYPE_SUBMISSION: el profesor recibe aviso de que una tarea fue entregada
 *                     y ya puede calificarla.
 */
@SuppressWarnings("deprecation")
public class EducationNotificationService extends IntentService {

    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_ACTIVITY_NAME = "activity_name";
    public static final String EXTRA_VALUE = "value"; // nota o nombre del estudiante

    public static final String TYPE_GRADE = "GRADE";
    public static final String TYPE_SUBMISSION = "SUBMISSION";

    private static final String TAG = "EducationNotification";

    public EducationNotificationService() {
        super("EducationNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Thread.sleep(5000); // simula el proceso (calificar / subir tarea)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        String type = intent.getStringExtra(EXTRA_TYPE);
        String activity = intent.getStringExtra(EXTRA_ACTIVITY_NAME);
        String value = intent.getStringExtra(EXTRA_VALUE);

        String title;
        String text;
        if (TYPE_GRADE.equals(type)) {
            title = getString(R.string.notif_grade_title);
            text = getString(R.string.notif_grade_text, activity, value);
        } else {
            title = getString(R.string.notif_submission_title);
            text = getString(R.string.notif_submission_text, value, activity);
        }

        Log.v(TAG, title + " - " + text);
        // id distinto por notificación para que no se reemplacen entre sí
        NotificationHelper.show(this, (int) System.currentTimeMillis(), title, text);
    }
}
