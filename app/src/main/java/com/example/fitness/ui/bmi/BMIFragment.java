package com.example.fitness.ui.bmi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.fitness.R;

public class BMIFragment extends Fragment {

    private EditText editTextAge, editTextWeight, editTextHeight;
    private RadioGroup radioGroupGender;
    private Button buttonCalculate;
    private TextView textViewResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_b_m_i, container, false);


        editTextAge = view.findViewById(R.id.editTextAge);
        editTextWeight = view.findViewById(R.id.editTextWeight);
        editTextHeight = view.findViewById(R.id.editTextHeight);
        radioGroupGender = view.findViewById(R.id.radioGroupGender);
        buttonCalculate = view.findViewById(R.id.buttonCalculate);
        textViewResult = view.findViewById(R.id.textViewResult);


        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI();
            }
        });

        return view;
    }

    private void calculateBMI() {
        textViewResult.setVisibility(View.VISIBLE);


        int age = Integer.parseInt(editTextAge.getText().toString());
        double weight = Double.parseDouble(editTextWeight.getText().toString());
        double height = Double.parseDouble(editTextHeight.getText().toString());
        int genderId = radioGroupGender.getCheckedRadioButtonId();


        double genderFactor = 1.0;
        if (genderId == R.id.radioButtonFemale) {
            genderFactor = 0.9;
        }


        double heightInMeter = height / 100;
        double bmi = weight / (heightInMeter * heightInMeter);


        String category;
        if (age < 18) {
            category = "18 yaş altı için BMI kategorizasyonu uygun değildir.";
        } else if (bmi < 18.5) {
            category = "Zayıf";
        } else if (bmi < 24.9) {
            category = "Normal";
        } else if (bmi < 29.9) {
            category = "Fazla Kilolu";
        } else if (bmi < 34.9) {
            category = "Şişman (Tip 1)";
        } else if (bmi < 39.9) {
            category = "Şişman (Tip 2)";
        } else {
            category = "Aşırı Şişman (Morbid)";
        }


        textViewResult.setText(getString(R.string.bmi_result, bmi) + "\n" + category);
    }

}
