package com.example.studentattendanceregister;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText studentNameInput;
    private LinearLayout studentsContainer;
    private TextView emptyMessage;
    private TextView attendanceSummary;

    private final ArrayList<String> studentNames = new ArrayList<>();
    private final ArrayList<String> attendance = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        studentNameInput = findViewById(R.id.studentNameInput);
        studentsContainer = findViewById(R.id.studentsContainer);
        emptyMessage = findViewById(R.id.emptyMessage);
        attendanceSummary = findViewById(R.id.attendanceSummary);

        Button addStudentButton =
                findViewById(R.id.addStudentButton);

        addStudentButton.setOnClickListener(v -> addStudent());

        updateDisplay();
    }

    private void addStudent() {
        String name = studentNameInput.getText()
                .toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(
                    this,
                    "Please enter a student's name",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        studentNames.add(name);
        attendance.add("Absent");

        studentNameInput.setText("");

        Toast.makeText(
                this,
                name + " added successfully",
                Toast.LENGTH_SHORT
        ).show();

        updateDisplay();
    }

    private void updateDisplay() {
        studentsContainer.removeAllViews();

        int presentCount = 0;
        int absentCount = 0;

        emptyMessage.setVisibility(
                studentNames.isEmpty() ? View.VISIBLE : View.GONE
        );

        for (int i = 0; i < studentNames.size(); i++) {

            final int position = i;

            if (attendance.get(i).equals("Present")) {
                presentCount++;
            } else {
                absentCount++;
            }

            LinearLayout studentRow = new LinearLayout(this);
            studentRow.setOrientation(LinearLayout.VERTICAL);
            studentRow.setPadding(12, 12, 12, 12);
            studentRow.setBackgroundColor(Color.WHITE);

            LinearLayout.LayoutParams rowParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            rowParams.setMargins(0, 6, 0, 6);
            studentRow.setLayoutParams(rowParams);

            TextView nameView = new TextView(this);
            nameView.setText(studentNames.get(i));
            nameView.setTextSize(18);
            nameView.setTypeface(null, Typeface.BOLD);
            nameView.setTextColor(Color.rgb(23, 61, 112));

            studentRow.addView(nameView);

            TextView statusView = new TextView(this);
            statusView.setText(
                    "Status: " + attendance.get(i)
            );
            statusView.setTextSize(15);

            statusView.setTextColor(
                    attendance.get(i).equals("Present")
                            ? Color.rgb(22, 130, 70)
                            : Color.rgb(190, 50, 50)
            );

            studentRow.addView(statusView);

            Spinner statusSpinner = new Spinner(this);

            String[] options = {"Absent", "Present"};

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_item,
                            options
                    );

            adapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item
            );

            statusSpinner.setAdapter(adapter);

            statusSpinner.setSelection(
                    attendance.get(i).equals("Present") ? 1 : 0
            );

            statusSpinner.setOnItemSelectedListener(
                    new android.widget.AdapterView
                            .OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(
                                android.widget.AdapterView<?> parent,
                                View view,
                                int selectedPosition,
                                long id) {

                            String selectedStatus =
                                    options[selectedPosition];

                            if (!attendance.get(position)
                                    .equals(selectedStatus)) {

                                attendance.set(position, selectedStatus);
                                updateDisplay();
                            }
                        }

                        @Override
                        public void onNothingSelected(
                                android.widget.AdapterView<?> parent) {
                        }
                    });

            studentRow.addView(statusSpinner);

            studentsContainer.addView(studentRow);
        }

        attendanceSummary.setText(
                "Total: " + studentNames.size()
                        + " | Present: " + presentCount
                        + " | Absent: " + absentCount
        );
    }
}