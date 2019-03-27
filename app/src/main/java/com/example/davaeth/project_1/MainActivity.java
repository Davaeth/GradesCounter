package com.example.davaeth.project_1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity implements TextWatcher {

    EditText firstNameText;
    EditText secondNameText;
    EditText countText;

    TextView averageGradeText;

    float average;
    double roundedAverage;

    Button nextBtn;

    Intent intent;

    public static final String GRADES_COUNT = "gradesCount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        averageGradeText = (TextView) findViewById(R.id.averageGrade);

        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setVisibility(View.INVISIBLE);

        intent = getIntent();

        if (intent.hasExtra("averageGradeText")) {
            if (intent.getFloatExtra("averageGradeText", 3.0f) != 0) {
                average = intent.getFloatExtra("averageGradeText", 3.0f);
                roundedAverage = new BigDecimal(average).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                averageGradeText.setText(String.valueOf("Your average grade: " + roundedAverage));

                if (roundedAverage > 3.0f && roundedAverage <= 4.0f) {
                    averageGradeText.setTextColor(getResources().getColor(R.color.colorPrimary));

                    nextBtn.setVisibility(View.VISIBLE);
                    nextBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    nextBtn.setText("COOL");
                } else if (roundedAverage > 4.0f) {
                    averageGradeText.setTextColor(getResources().getColor(R.color.colorPrimary));

                    nextBtn.setVisibility(View.VISIBLE);
                    nextBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    nextBtn.setText("SUPERB");
                } else if (roundedAverage <= 3.0f) {
                    averageGradeText.setTextColor(getResources().getColor(R.color.colorAccent));

                    nextBtn.setVisibility(View.VISIBLE);
                    nextBtn.setText("BAD!!");
                }
            }
        }

        firstNameText = (EditText) findViewById(R.id.First_name);
        firstNameText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        secondNameText = (EditText) findViewById(R.id.Second_name);
        secondNameText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        countText = (EditText) findViewById(R.id.Count);

        firstNameText.addTextChangedListener(this);
        secondNameText.addTextChangedListener(this);
        countText.addTextChangedListener(this);

    }

    public void SendData(View view) {
        if (!intent.hasExtra("averageGradeText")) {
            if (Integer.parseInt(countText.getText().toString()) < 5) {
                Toast.makeText(getApplicationContext(), "At least 5 grades!", Toast.LENGTH_LONG).show();
                return;
            } else if (Integer.parseInt(countText.getText().toString()) > 15) {
                Toast.makeText(getApplicationContext(), "No more than 15 grades!", Toast.LENGTH_LONG).show();
                return;
            }

            int gradesCount = Integer.parseInt(countText.getText().toString());

            Intent intent = new Intent(this, GradeActivity.class);

            intent.putExtra(GRADES_COUNT, gradesCount);
            startActivity(intent);
        } else {
            if(roundedAverage < 3.0f) {
                CreateNotification("Lost! You have to pay!");
            } else {
                CreateNotification("You've passed!");
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!firstNameText.getText().toString().isEmpty()
                && !secondNameText.getText().toString().isEmpty()
                && !countText.getText().toString().isEmpty()) {
            nextBtn.setVisibility(View.VISIBLE);
        } else {
            nextBtn.setVisibility(View.GONE);
        }
    }

    private void CreateNotification(String content) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Your result!";
            String description = "Test";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("Grades result", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Grades result")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Your result!")
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(1, builder.build());
    }
}
