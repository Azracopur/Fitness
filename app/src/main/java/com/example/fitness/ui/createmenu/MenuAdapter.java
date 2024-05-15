package com.example.fitness.ui.createmenu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitness.MenuModel;
import com.example.fitness.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.GroupViewHolder> {

    List<MenuModel> groupModelList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(MenuModel item);
    }

    public MenuAdapter(List<MenuModel> groupModelList, OnItemClickListener listener) {
        this.groupModelList = groupModelList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_createmenu_menu, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        MenuModel groupModel = groupModelList.get(position);
        holder.bind(groupModel, mListener);
    }

    @Override
    public int getItemCount() {
        return groupModelList.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        ImageView groupImageView;
        TextView groupNameTextView, groupDescriptionTextView;

        public GroupViewHolder(View itemView) {
            super(itemView);

            groupImageView = itemView.findViewById(R.id.item_createmenu_menu_image);
            groupNameTextView = itemView.findViewById(R.id.item_createmenu_menu_name);
            groupDescriptionTextView = itemView.findViewById(R.id.item_createmenu_menu_description);
        }

        public void bind(final MenuModel groupModel, final OnItemClickListener listener) {
            groupNameTextView.setText(groupModel.getName());
            groupDescriptionTextView.setText(groupModel.getDescription());

            String imageUrl = groupModel.getImage();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.get().load(imageUrl).into(groupImageView);
            } else {

                groupImageView.setImageResource(R.drawable.menu);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(groupModel);
                }
            });
        }



        }
    }



