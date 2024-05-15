package com.example.fitness.ui.createegzersize;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.fitness.R;

public class CreateEgzersizeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_egzersize, container, false);


        LinearLayout imageLayout1 = view.findViewById(R.id.image1Layout);
        TextView textBox1 = view.findViewById(R.id.description1);
        LinearLayout imageLayout2 = view.findViewById(R.id.image2Layout);
        TextView textBox2 = view.findViewById(R.id.description2);
        LinearLayout imageLayout3 = view.findViewById(R.id.image3Layout);
        TextView textBox3 = view.findViewById(R.id.description3);
        LinearLayout imageLayout4 = view.findViewById(R.id.image4Layout);
        TextView textBox4 = view.findViewById(R.id.description4);
        LinearLayout imageLayout5 = view.findViewById(R.id.image5Layout);
        TextView textBox5 = view.findViewById(R.id.description8);
        LinearLayout imageLayout6 = view.findViewById(R.id.image6Layout);
        TextView textBox6 = view.findViewById(R.id.description9);


        setClickListener(imageLayout1, textBox1);
        setClickListener(imageLayout2, textBox2);
        setClickListener(imageLayout3, textBox3);
        setClickListener(imageLayout4, textBox4);
        setClickListener(imageLayout5, textBox5);
        setClickListener(imageLayout6, textBox6);

        return view;
    }

    private void setClickListener(LinearLayout layout, TextView textBox) {
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = (ImageView) layout.getChildAt(0);
                String description = imageView.getContentDescription().toString();
                textBox.setText(description);
                textBox.setVisibility(View.VISIBLE);
            }
        });
    }

}
