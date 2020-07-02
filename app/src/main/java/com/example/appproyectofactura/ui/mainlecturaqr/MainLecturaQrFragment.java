package com.example.appproyectofactura.ui.mainlecturaqr;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.appproyectofactura.MainLecturaQR;
import com.example.appproyectofactura.R;

public class MainLecturaQrFragment extends Fragment {

    private MainLecturaQrViewModel mViewModel;

    public static MainLecturaQrFragment newInstance() {
        return new MainLecturaQrFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        //startActivity(new Intent(getContext(), MainLecturaQR.class));
        startActivity(new Intent(getActivity(), MainLecturaQR.class));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainLecturaQrViewModel.class);
        // TODO: Use the ViewModel
    }

}
