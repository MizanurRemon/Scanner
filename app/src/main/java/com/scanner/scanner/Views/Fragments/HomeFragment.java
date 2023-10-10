package com.scanner.scanner.Views.Fragments;

import static com.scanner.scanner.Utils.Helpers.convertStringToPDF;
import static com.scanner.scanner.Utils.Helpers.getFileFromBase64AndSaveInSDCard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.scanner.scanner.Adapter.FileListAdapter;
import com.scanner.scanner.Model.FileListResponse;
import com.scanner.scanner.R;
import com.scanner.scanner.Remote.FileUpload.FileViewModel;
import com.scanner.scanner.databinding.FragmentFileUploadBinding;
import com.scanner.scanner.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements FileListAdapter.OnPDFItemClickListener {

    FileViewModel fileViewModel;

    FragmentHomeBinding binding;
    List<FileListResponse> fileList;
    FileListAdapter fileListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        initView(view);

        load_data();

        return view;
    }

    private void load_data() {
        binding.progressBar.setVisibility(View.VISIBLE);
        fileViewModel.getFileList(getActivity()).observe(getViewLifecycleOwner(), new Observer<List<FileListResponse>>() {
            @Override
            public void onChanged(List<FileListResponse> fileListResponses) {
                binding.progressBar.setVisibility(View.GONE);
                fileList = new ArrayList<>();
                fileList = fileListResponses;
                fileListAdapter = new FileListAdapter(fileList);
                binding.itemView.setAdapter(fileListAdapter);

            }
        });
    }

    private void initView(View view) {
        binding.itemView.setHasFixedSize(true);
        binding.itemView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fileViewModel = new ViewModelProvider(getActivity()).get(FileViewModel.class);
    }

    @Override
    public void onPDFItemClick(int position) {

        FileListResponse response = fileList.get(position);
        getFileFromBase64AndSaveInSDCard(response.file,  response.fileName, ".pdf");
    }
}