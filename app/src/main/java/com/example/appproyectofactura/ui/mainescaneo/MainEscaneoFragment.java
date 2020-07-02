package com.example.appproyectofactura.ui.mainescaneo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.appproyectofactura.MainEscaneo;
import com.example.appproyectofactura.R;

public class MainEscaneoFragment extends Fragment {

    private MainEscaneoViewModel mViewModel;

    public static MainEscaneoFragment newInstance() {
        return new MainEscaneoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        startActivity(new Intent(getActivity(), MainEscaneo.class));
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainEscaneoViewModel.class);
        // TODO: Use the ViewModel
    }

}
