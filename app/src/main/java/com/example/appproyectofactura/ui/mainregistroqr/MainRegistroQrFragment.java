package com.example.appproyectofactura.ui.mainregistroqr;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.appproyectofactura.MainRegistroQR;
import com.example.appproyectofactura.R;

public class MainRegistroQrFragment extends Fragment {

    private MainRegistroQrViewModel mViewModel;

    public static MainRegistroQrFragment newInstance() {
        return new MainRegistroQrFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        startActivity(new Intent(getActivity(), MainRegistroQR.class));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainRegistroQrViewModel.class);
        // TODO: Use the ViewModel
    }

}
