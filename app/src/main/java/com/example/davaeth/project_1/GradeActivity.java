package com.example.davaeth.project_1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GradeActivity extends AppCompatActivity {

    Intent extras;

    ViewGroup radioGroup;

    ArrayList<RadioGroup> checkedGrades;

    int gradesCount;

    public static final String AVERAGE_GRADE = "averageGradeText";

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        radioGroup = ((ViewGroup) findViewById(R.id.radioGroup));

        extras = getIntent();

        gradesCount = extras.getIntExtra("gradesCount", 2);

        checkedGrades = new ArrayList<RadioGroup>(gradesCount);

        for (int row = 0; row < gradesCount; row++) {
            RadioGroup gradesCount = new RadioGroup(this);
            gradesCount.setOrientation(LinearLayout.HORIZONTAL);

            for (int i = 2; i <= 5; i++) {
                RadioButton gradeButton = new RadioButton(this);

                gradeButton.setId(i);
                gradeButton.setText(String.valueOf(i));

                gradesCount.addView(gradeButton);
            }
            radioGroup.addView(gradesCount);

            checkedGrades.add(gradesCount);
        }
    }

    public void sendGrades(View view) {
        float averageGrade = 0;

        for (int i = 0; i < gradesCount; i++) {

            if (checkedGrades.get(i).getCheckedRadioButtonId() != -1) {
                averageGrade += checkedGrades.get(i).getCheckedRadioButtonId();
            } else {
                Toast.makeText(getApplicationContext(), "Please choose every grade!", Toast.LENGTH_LONG).show();
                return;
            }

        }

        averageGrade /= gradesCount;

        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra(AVERAGE_GRADE, averageGrade);
        startActivity(intent);
    }
}