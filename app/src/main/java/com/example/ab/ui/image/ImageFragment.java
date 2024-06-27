package com.example.ab.ui.image;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ab.R; // Ensure this import matches your package name
import com.example.ab.databinding.FragmentImageBinding;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

public class ImageFragment extends Fragment {

    private FragmentImageBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout using ViewBinding
        binding = FragmentImageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set up SubsamplingScaleImageView with an image resource
        SubsamplingScaleImageView imageView = binding.imageView;
        imageView.setImage(ImageSource.resource(R.drawable.your_image)); // Replace with your image resource

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}