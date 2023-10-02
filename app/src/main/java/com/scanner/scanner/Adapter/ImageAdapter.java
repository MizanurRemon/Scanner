package com.scanner.scanner.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scanner.scanner.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    List<Uri> imageList;

    public ImageAdapter(List<Uri> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri uri = imageList.get(position);
        holder.imageView.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public OnImageItemClickListener onImageItemClickListener;
    public OnImageDeleteClickListener onImageDeleteClickListener;

    public interface OnImageDeleteClickListener {
        void onDeleteImageClick(int position);
    }

    public interface OnImageItemClickListener {
        void onImageItemClick(int position);
    }

    public void setOnItemClickListener(OnImageItemClickListener onImageItemClickListener, OnImageDeleteClickListener onImageDeleteClickListener) {
        this.onImageItemClickListener = onImageItemClickListener;
        this.onImageDeleteClickListener = onImageDeleteClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onImageItemClickListener.onImageItemClick(position);
                        }
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageDeleteClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onImageDeleteClickListener.onDeleteImageClick(position);
                        }
                    }
                }
            });
        }
    }
}
