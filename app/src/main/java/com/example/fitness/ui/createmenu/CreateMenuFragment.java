package com.example.fitness.ui.createmenu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fitness.MenuModel;
import com.example.fitness.R;
import com.example.fitness.ui.createmenu.MenuAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CreateMenuFragment extends Fragment {

    EditText groupNameEditText, groupDescriptionEditText;
    ImageView groupImageView;
    Button createGroupButton;
    RecyclerView groupsRecyclerView;

    Uri filePath;

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    FirebaseStorage mStorage;

    ArrayList<MenuModel> groupModelArrayList;
    MenuAdapter menuAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_menu, container, false);

        groupNameEditText = view.findViewById(R.id.createmenu_menuNameEditText);
        groupDescriptionEditText = view.findViewById(R.id.createmenu_menuDescriptionEditText);
        groupImageView = view.findViewById(R.id.createmenu_menuImageImageView);
        createGroupButton = view.findViewById(R.id.createmenu_createMenuButton);
        groupsRecyclerView = view.findViewById(R.id.createmenu_menusRecyclerView);

        groupModelArrayList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        Intent data = result.getData();
                        filePath = data.getData();
                        groupImageView.setImageURI(filePath);
                    }
                }
        );

        groupImageView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncher.launch(intent);
        });

        createGroupButton.setOnClickListener(v -> {
            String groupName = groupNameEditText.getText().toString();
            String groupDescription = groupDescriptionEditText.getText().toString();

            if (groupName.isEmpty()) {
                Toast.makeText(getContext(), "Menu ismi gerekli", Toast.LENGTH_SHORT).show();
                return;
            }
            if (groupDescription.isEmpty()) {
                Toast.makeText(getContext(), "Menu açıklaması gerekli", Toast.LENGTH_SHORT).show();
                return;
            }
            if (filePath != null) {

                StorageReference storageReference = mStorage.getReference().child("resmiler/" + UUID.randomUUID().toString());

                storageReference.putFile(filePath).addOnSuccessListener(taskSnapshot -> {
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        Toast.makeText(getContext(), "Resim yüklendi", Toast.LENGTH_SHORT).show();

                        mStore.collection("/userdata/" + mAuth.getCurrentUser().getUid() + "/" + "groups")
                                .add(new HashMap<String, Object>(){{
                                    put("name", groupName);
                                    put("description", groupDescription);
                                    put("image", downloadUrl);
                                    put("numbers", null);
                                }}).addOnSuccessListener(documentReference -> {
                                    Toast.makeText(getContext(), "Menu başarıyla oluşturuldu", Toast.LENGTH_SHORT).show();

                                    documentReference.get().addOnSuccessListener(documentSnapshot -> {
                                        MenuModel groupModel = new MenuModel(groupName, groupDescription, downloadUrl, null, documentSnapshot.getId());
                                        groupModelArrayList.add(groupModel);
                                        if (menuAdapter != null) {
                                            menuAdapter.notifyItemInserted(groupModelArrayList.size() - 1);
                                        }
                                    });
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Menu oluşturulamadı", Toast.LENGTH_SHORT).show();
                                });
                    });
                });
                return;
            } else {
                mStore.collection("/userdata/" + mAuth.getCurrentUser().getUid() + "/" + "groups")
                        .add(new HashMap<String, Object>(){{
                            put("name", groupName);
                            put("description", groupDescription);
                            put("image", null);
                            put("numbers", null);
                        }}).addOnSuccessListener(documentReference -> {
                            Toast.makeText(getContext(), "Menu başarıyla oluşturuldu", Toast.LENGTH_SHORT).show();

                            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                                MenuModel groupModel = new MenuModel(groupName, groupDescription, null, null, documentSnapshot.getId());
                                groupModelArrayList.add(groupModel);
                                if (menuAdapter != null) {
                                    menuAdapter.notifyItemInserted(groupModelArrayList.size() - 1);
                                }
                            });
                        }).addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Menu oluşturulamadı", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        FetchGroups();
        return view;
    }

    private void FetchGroups(){
        String userId = mAuth.getCurrentUser().getUid();

        mStore.collection("/userdata/" + userId + "/" + "groups").get().addOnSuccessListener(queryDocumentSnapshots -> {
            groupModelArrayList.clear();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                MenuModel groupModel = new MenuModel(documentSnapshot.getString("name"), documentSnapshot.getString("description"), documentSnapshot.getString("image"), (List<String>)documentSnapshot.get("numbers"),documentSnapshot.getId());
                groupModelArrayList.add(groupModel);
            }

            if (getActivity() != null) {
                menuAdapter = new MenuAdapter(groupModelArrayList, new MenuAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(MenuModel item) {

                    }
                });
                groupsRecyclerView.setAdapter(menuAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                groupsRecyclerView.setLayoutManager(linearLayoutManager);
            }

        });
    }
}
