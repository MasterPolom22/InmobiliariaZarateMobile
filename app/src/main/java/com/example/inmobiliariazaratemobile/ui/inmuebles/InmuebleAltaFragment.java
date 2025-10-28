package com.example.inmobiliariazaratemobile.ui.inmuebles;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.*;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.bumptech.glide.Glide;
import com.example.inmobiliariazaratemobile.databinding.FragmentInmuebleAltaBinding;

public class InmuebleAltaFragment extends Fragment {

    private InmuebleAltaViewModel mViewModel;
    private FragmentInmuebleAltaBinding binding;
    private ActivityResultLauncher<Intent> arl;
    private Intent intent;

    public static InmuebleAltaFragment newInstance() {
        return new InmuebleAltaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(InmuebleAltaViewModel.class);
        binding = FragmentInmuebleAltaBinding.inflate(getLayoutInflater());
        abrirGaleria();

        binding.btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arl.launch(intent);

            }
        });

        mViewModel.getMuri().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                binding.imgView.setImageURI(uri);
            }
        });

        binding.btnCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//String direccion, String valor, String tipo, String uso, String ambientes, boolean disponible
                mViewModel.cargarInmueble(binding.etDireccion.getText().toString(),
                        binding.etValor.getText().toString(),
                        binding.etTipo.getText().toString(),
                        binding.etUso.getText().toString(),
                        binding.etAmbientes.getText().toString(),
                        binding.cbDisp.isChecked());
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(InmuebleAltaViewModel.class);
        // TODO: Use the ViewModel
    }

    private void abrirGaleria() {
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//Es para abrir la galeria
        arl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Log.d("AgregarInmuebleFragment", "Result: " + result);
                mViewModel.recibirFoto(result);

            }
        });
    }


}
