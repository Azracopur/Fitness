package com.example.fitness.ui.addtomenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitness.FoodModel;
import com.example.fitness.MenuModel;
import com.example.fitness.R;
import com.example.fitness.ui.createmenu.MenuAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AddToMenuFragment extends Fragment {

    RecyclerView menusRecyclerView;
    TextView selectedMenuTextView;

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;

    ArrayList<MenuModel> menuModelArrayList;
    MenuAdapter menuAdapter;

    MenuModel selectedMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_to_menu, container, false);

        menusRecyclerView = view.findViewById(R.id.addtomenu_menusRecyclerView);
        selectedMenuTextView = view.findViewById(R.id.addtomenu_selectedMenuTextView);

        menuModelArrayList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        fetchMenus();
        setupFoodButtons(view);

        return view;
    }

    private void fetchMenus() {
        String userId = mAuth.getCurrentUser().getUid();

        mStore.collection("/userdata/" + userId + "/" + "groups").get().addOnSuccessListener(queryDocumentSnapshots -> {
            menuModelArrayList.clear();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                String menuId = documentSnapshot.getId();
                String menuName = documentSnapshot.getString("name");
                String menuDescription = documentSnapshot.getString("description");
                String menuImage = documentSnapshot.getString("image");

                MenuModel menuModel = new MenuModel(menuName, menuDescription, menuImage, new ArrayList<>(), menuId);

                mStore.collection("/userdata/" + userId + "/groups/" + menuId + "/foods")
                        .get()
                        .addOnSuccessListener(foodQueryDocumentSnapshots -> {
                            List<FoodModel> foodModelList = new ArrayList<>();
                            for (DocumentSnapshot foodDocumentSnapshot : foodQueryDocumentSnapshots.getDocuments()) {
                                FoodModel foodModel = foodDocumentSnapshot.toObject(FoodModel.class);
                                foodModelList.add(foodModel);
                            }

                            menuModel.setFoods(foodModelList);
                            menuModelArrayList.add(menuModel);

                            if (getContext() != null) {
                                menuAdapter.notifyDataSetChanged();
                            }
                        });
            }

            if (getContext() != null) {
                menuAdapter = new MenuAdapter(menuModelArrayList, item -> {
                    selectedMenu = item;
                    selectedMenuTextView.setText("Seçili Menü: " + item.getName());
                });
                menusRecyclerView.setAdapter(menuAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                menusRecyclerView.setLayoutManager(linearLayoutManager);
            }
        });
    }

    private void setupFoodButtons(View view) {
        Button foodButton1 = view.findViewById(R.id.foodButton1);
        Button foodButton2 = view.findViewById(R.id.foodButton2);
        Button foodButton3 = view.findViewById(R.id.foodButton3);
        Button foodButton4 = view.findViewById(R.id.foodButton4);

        foodButton1.setOnClickListener(v -> onFoodItemClick(foodButton1));
        foodButton2.setOnClickListener(v -> onFoodItemClick(foodButton2));
        foodButton3.setOnClickListener(v -> onFoodItemClick(foodButton3));
        foodButton4.setOnClickListener(v -> onFoodItemClick(foodButton4));
    }

    public void onFoodItemClick(Button foodButton) {
        if (selectedMenu != null) {
            String foodName = foodButton.getTag().toString();
            int calories = calculateCaloriesForFood(foodName);
            selectedMenu.addFood(foodName, calories);

            mStore.collection("/userdata/" + mAuth.getCurrentUser().getUid() + "/" + "groups" + "/" + selectedMenu.getId() + "/" + "foods")
                    .add(new FoodModel(foodName, calories))
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getContext(), "Yiyecek " + foodName + " menüye eklendi", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Yiyecek eklenirken hata oluştu", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getContext(), "Lütfen bir menü seçin", Toast.LENGTH_SHORT).show();
        }
    }

    private int calculateCaloriesForFood(String foodName) {

        switch (foodName) {
            case "Adana Kebap":
                return 450;
            case "İskender":
                return 500;
            case "Köfte Ve Patates Kızartması":
                return 550;
            case "Beyti Kebap":
                return 600;
            default:
                return 0;
        }
    }
}
