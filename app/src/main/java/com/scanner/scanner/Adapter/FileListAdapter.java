package com.scanner.scanner.Adapter;

import static com.scanner.scanner.Utils.Helpers.getImageFromString;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scanner.scanner.Model.FileListResponse;
import com.scanner.scanner.R;
import com.scanner.scanner.Utils.Constants;

import java.util.List;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {

    private List<FileListResponse> fileList;

    public FileListAdapter(List<FileListResponse> fileList) {
        this.fileList = fileList;
    }

    @NonNull
    @Override
    public FileListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FileListAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FileListAdapter.ViewHolder holder, int position) {

        FileListResponse response = fileList.get(position);
        holder.fileNameText.setText(response.fileName);

        if(response.fileType.equals(Constants.PDF)){

        }else {

            holder.imageView.setImageBitmap(getImageFromString(response.file));
        }

    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView  imageView;

        Button downloadButton;
        TextView fileNameText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            downloadButton = itemView.findViewById(R.id.downloadButton);
            fileNameText = itemView.findViewById(R.id.fileNameText);
        }
    }
}
