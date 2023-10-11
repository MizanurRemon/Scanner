package com.scanner.scanner.Views.Fragments;

import static com.scanner.scanner.Utils.Helpers.convertImageStringToPDF;
import static com.scanner.scanner.Utils.Helpers.convertPdfStringToPDF;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scanner.scanner.Adapter.FileListAdapter;
import com.scanner.scanner.Model.FileListResponse;
import com.scanner.scanner.Remote.FileUpload.FileViewModel;
import com.scanner.scanner.Utils.Constants;
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
                fileListAdapter.setOnItemClickListener(HomeFragment.this::onPDFItemClick);
                binding.itemView.setAdapter(fileListAdapter);

            }
        });
    }

    private void initView(View view) {
        binding.itemView.setHasFixedSize(true);
        binding.itemView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.itemView.setItemViewCacheSize(100);
        fileViewModel = new ViewModelProvider(getActivity()).get(FileViewModel.class);
    }

    @Override
    public void onPDFItemClick(int position) {

        FileListResponse response = fileList.get(position);
      /*  GetFilePathAndStatus getFilePathAndStatus = getFileFromBase64AndSaveInSDCard(response.file,  response.fileName, "pdf");
        Toast.makeText(getActivity(), getFilePathAndStatus.filePath, Toast.LENGTH_SHORT).show();*/

        if (response.fileType.equals(Constants.PDF)) {
            convertPdfStringToPDF(response.file, getActivity(), response.fileName);
        } else {
            //Toast.makeText(getActivity(), "developing", Toast.LENGTH_SHORT).show();
            convertImageStringToPDF(response.file, getActivity(), response.fileName);
        }


    }
}