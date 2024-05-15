package com.example.fitness.ui.calculatecalori;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class CalculateCaloriFragment extends Fragment {

    private RecyclerView menusRecyclerView;
    private TextView selectedGroupTextView;
    private TextView selectedMessageTextView;
    private Button sendButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;

    private List<MenuModel> menuModelList;
    private MenuAdapter menuAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculate_calori, container, false);

        menusRecyclerView = view.findViewById(R.id.menusRecyclerView);
        selectedGroupTextView = view.findViewById(R.id.selectedGroupTextView);
        selectedMessageTextView = view.findViewById(R.id.selectedMessageTextView);
        sendButton = view.findViewById(R.id.sendmessage_sendButton);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        menuModelList = new ArrayList<>();

        fetchMenus();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSelectedFood();
            }
        });

        return view;
    }

    private void fetchMenus() {
        String userId = mAuth.getCurrentUser().getUid();

        mStore.collection("/userdata/" + userId + "/" + "groups").get().addOnSuccessListener(queryDocumentSnapshots -> {
            menuModelList.clear();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                MenuModel menuModel = new MenuModel(documentSnapshot.getString("name"), documentSnapshot.getString("description"), documentSnapshot.getString("image"), (List<String>) documentSnapshot.get("numbers"), documentSnapshot.getId());
                menuModelList.add(menuModel);
            }

            if (getContext() != null) {
                menuAdapter = new MenuAdapter(menuModelList, item -> {
                    selectedGroupTextView.setText("Seçili Menü: " + item.getName());
                    showFoodsOfMenu(item);
                });
                menusRecyclerView.setAdapter(menuAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                menusRecyclerView.setLayoutManager(linearLayoutManager);
            }

        });
    }


    private void showFoodsOfMenu(MenuModel menu) {
        String userId = mAuth.getCurrentUser().getUid();
        String groupId = menu.getId();

        mStore.collection("/userdata/" + userId + "/" + "groups" + "/" + groupId + "/" + "foods")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<FoodModel> foods = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        FoodModel food = documentSnapshot.toObject(FoodModel.class);
                        foods.add(food);
                    }

                    if (!foods.isEmpty()) {

                        selectedMessageTextView.setText("Seçili Mesaj: " + foods.get(0).getName() + " - Kalori: " + foods.get(0).getCalories());
                    } else {
                        selectedMessageTextView.setText("Seçili Menüye Ait Yiyecek Bulunamadı.");
                    }
                })
                .addOnFailureListener(e -> {

                    selectedMessageTextView.setText("Seçili Menüye Ait Yiyecekleri Getirirken Hata Oluştu.");
                });
    }



    private void showSelectedFood() {

        if (!menuModelList.isEmpty()) {
            MenuModel selectedMenu = menuModelList.get(0);
            if (!selectedMenu.getFoods().isEmpty()) {

                selectedMessageTextView.setText("Seçili Menu: " + selectedMenu.getFoods().get(0).getName() + " - Kalori: " + selectedMenu.getFoods().get(0).getCalories());
            } else {
                selectedMessageTextView.setText("Seçili Menüye Ait Yiyecek Bulunamadı.");
            }
        } else {
            selectedMessageTextView.setText("Henüz bir menü seçilmedi.");
        }
    }
}
