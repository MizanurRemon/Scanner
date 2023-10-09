package com.scanner.scanner.Adapter;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scanner.scanner.Model.FileResponse;
import com.scanner.scanner.R;
import com.scanner.scanner.Utils.Constants;

import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {
    List<FileResponse> fileList;

    public FileAdapter(List<FileResponse> fileList) {
        this.fileList = fileList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_card, parent, false));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FileResponse response = fileList.get(position);
        holder.fileNameText.setText(response.getFileName());

        if (response.getFileExtension().equals(Constants.PDF)) {
            holder.imageView.setImageDrawable(holder.itemView.getContext().getDrawable(R.drawable.ic_pdf));
        } else {
            holder.imageView.setImageDrawable(holder.itemView.getContext().getDrawable(R.drawable.ic_photos));
        }
    }



    @Override
    public int getItemCount() {
        return fileList.size();
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
        TextView fileNameText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            fileNameText = itemView.findViewById(R.id.fileNameText);

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
