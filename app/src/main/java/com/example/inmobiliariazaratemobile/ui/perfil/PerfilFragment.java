package com.example.inmobiliariazaratemobile.ui.perfil;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliariazaratemobile.databinding.FragmentPerfilBinding;
import com.example.inmobiliariazaratemobile.model.PropietarioModel;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding b;
    private PerfilViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = FragmentPerfilBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(PerfilViewModel.class);

        b.btnEditarGuardar.setOnClickListener(v -> {
            PropietarioModel editado = leerUI();
            vm.toggleEditarGuardar(editado);
        });

        b.btnCancelar.setOnClickListener(v -> vm.cancelarEdicion());

        b.btnCambiarPass.setOnClickListener(v -> mostrarDialogoCambioPass());

        vm.propietario.observe(getViewLifecycleOwner(), p -> { if (p != null) setUI(p); });

        vm.editMode.observe(getViewLifecycleOwner(), edit -> {
            boolean e = edit != null && edit;
            setEditable(e);
            b.btnEditarGuardar.setText(e ? "Guardar" : "Editar");
            b.btnCancelar.setVisibility(e ? View.VISIBLE : View.GONE);
        });

        vm.ui.observe(getViewLifecycleOwner(), s -> {
            if (s == null) return;
            switch (s.status){
                case LOADING:
                    b.progress.setVisibility(View.VISIBLE);
                    b.lblMsg.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    b.progress.setVisibility(View.GONE);
                    b.lblMsg.setVisibility(View.GONE);
                    if (s.message != null) Toast.makeText(requireContext(), s.message, Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    b.progress.setVisibility(View.GONE);
                    b.lblMsg.setVisibility(View.VISIBLE);
                    b.lblMsg.setText(s.message);
                    break;
                default:
                    b.progress.setVisibility(View.GONE);
                    b.lblMsg.setVisibility(View.GONE);
            }
        });

        vm.cargarPerfil();
    }

    // ---- Solo tareas de vista ----
    private void setEditable(boolean e){
        b.txtNombre.setEnabled(e);
        b.txtApellido.setEnabled(e);
        b.txtEmail.setEnabled(e);
        b.txtTelefono.setEnabled(e);
    }

    private void setUI(PropietarioModel p){
        b.txtId.setText(String.valueOf(p.getIdPropietario()));
        b.txtNombre.setText(n(p.getNombre()));
        b.txtApellido.setText(n(p.getApellido()));
        b.txtEmail.setText(n(p.getEmail()));
        b.txtTelefono.setText(n(p.getTelefono()));
    }

    private PropietarioModel leerUI(){
        PropietarioModel base = vm.propietario.getValue();
        PropietarioModel p = base != null ? base : new PropietarioModel();
        // No tocar Id
        p.setNombre(b.txtNombre.getText().toString().trim());
        p.setApellido(b.txtApellido.getText().toString().trim());
        p.setEmail(b.txtEmail.getText().toString().trim());
        p.setTelefono(b.txtTelefono.getText().toString().trim());
        return p;
    }

    private String n(String s){ return s==null? "": s; }

    private void mostrarDialogoCambioPass(){
        EditText etActual = new EditText(requireContext());
        etActual.setHint("Actual");
        etActual.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        EditText etNueva = new EditText(requireContext());
        etNueva.setHint("Nueva");
        etNueva.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        LinearLayout ll = new LinearLayout(requireContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) (16 * getResources().getDisplayMetrics().density);
        ll.setPadding(pad, pad, pad, 0);
        ll.addView(etActual);
        ll.addView(etNueva);

        new AlertDialog.Builder(requireContext())
                .setTitle("Cambiar contraseña")
                .setView(ll)
                .setPositiveButton("Guardar", (d, w) ->
                        vm.cambiarPassword(
                                etActual.getText().toString().trim(),
                                etNueva.getText().toString().trim()
                        )
                )
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
