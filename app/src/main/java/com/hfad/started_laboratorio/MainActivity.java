package com.hfad.started_laboratorio;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private EditText etActivityName;
    private EditText etValue;

    // Android 13+ (API 33) exige pedir permiso para mostrar notificaciones
    private final ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    granted -> {
                        if (!granted) {
                            Toast.makeText(this, R.string.permission_denied,
                                    Toast.LENGTH_LONG).show();
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etActivityName = findViewById(R.id.etActivityName);
        etValue = findViewById(R.id.etValue);

        NotificationHelper.createChannel(this);
        requestNotificationPermission();
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
    }

    /** Botón de la guía: inicia DelayedMessageService con el texto "Timing!". */
    public void onClick(View view) {
        Intent intent = new Intent(this, DelayedMessageService.class);
        intent.putExtra(DelayedMessageService.EXTRA_MESSAGE, getString(R.string.button_response));
        startService(intent);
    }

    /** Estudiante: llega la calificación de un taller o parcial. */
    public void onGradeClick(View view) {
        startEducationService(EducationNotificationService.TYPE_GRADE);
    }

    /** Profesor: un estudiante entregó una tarea y hay que calificarla. */
    public void onSubmissionClick(View view) {
        startEducationService(EducationNotificationService.TYPE_SUBMISSION);
    }

    private void startEducationService(String type) {
        String activityName = etActivityName.getText().toString().trim();
        String value = etValue.getText().toString().trim();
        if (activityName.isEmpty() || value.isEmpty()) {
            Toast.makeText(this, R.string.fill_fields, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, EducationNotificationService.class);
        intent.putExtra(EducationNotificationService.EXTRA_TYPE, type);
        intent.putExtra(EducationNotificationService.EXTRA_ACTIVITY_NAME, activityName);
        intent.putExtra(EducationNotificationService.EXTRA_VALUE, value);
        startService(intent);
        Toast.makeText(this, R.string.notification_scheduled, Toast.LENGTH_SHORT).show();
    }
}
