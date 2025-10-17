// InicioFragment.java
package com.example.inmobiliariazaratemobile.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliariazaratemobile.databinding.FragmentInicioBinding;

public class InicioFragment extends Fragment {

    private FragmentInicioBinding b;
    private InicioViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = FragmentInicioBinding.inflate(inflater, container, false);
        b.webMap.setWebViewClient(new WebViewClient());
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vm = new ViewModelProvider(this).get(InicioViewModel.class);

        vm.getUiModel().observe(getViewLifecycleOwner(), this::render);
        // Si existieran listeners, solo llamarían a métodos del ViewModel sin decidir nada.
    }

    private void render(InicioViewModel.UiModel m) {
        b.webMap.setVisibility(m.webVisibility);
        WebSettings ws = b.webMap.getSettings();
        ws.setJavaScriptEnabled(m.javascriptEnabled);
        b.webMap.loadDataWithBaseURL(
                m.baseUrl,
                m.html,
                "text/html",
                "UTF-8",
                null
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }
}
