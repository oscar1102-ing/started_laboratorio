package com.hfad.started_laboratorio;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Parte 1 de la guía: started service que espera 10 s y luego muestra
 * el texto en el logcat y en una notificación.
 * (IntentService está deprecado desde API 30, pero es lo que pide el laboratorio.)
 */
@SuppressWarnings("deprecation")
public class DelayedMessageService extends IntentService {

    public static final String EXTRA_MESSAGE = "message";
    public static final int NOTIFICATION_ID = 5453;
    private static final String TAG = "DelayedMessageService";

    public DelayedMessageService() {
        super("DelayedMessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Thread.sleep(10000); // 10 segundos, en un hilo aparte
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        String text = intent.getStringExtra(EXTRA_MESSAGE);
        showText(text);
    }

    private void showText(final String text) {
        Log.v(TAG, "The message is: " + text);
        NotificationHelper.show(this, NOTIFICATION_ID,
                getString(R.string.question), text);
    }
}
