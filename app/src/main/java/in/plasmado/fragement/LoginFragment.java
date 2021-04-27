package in.plasmado.fragement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import in.plasmado.R;
import in.plasmado.databinding.FragmentLoginBinding;

import static in.plasmado.helper.ParentHelper.addFragment;

public class LoginFragment extends Fragment {

    public FragmentLoginBinding mBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentLoginBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mBinding.btnRegister.setOnClickListener(v -> {
            addFragment(Objects.requireNonNull(getActivity()), R.id.flMainContainer, new RegisterFragment());
        });

        mBinding.btnLogin.setOnClickListener(v -> {
            if(checkFields()){

                checkLogin();

            }else{
                Toast.makeText(getActivity(), "Complete Fields", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean checkFields() {

        mBinding.etPhone.getText().toString();
        if(mBinding.etPhone.getText().toString() != ""){

            mBinding.etPassword.getText().toString();
            if(mBinding.etPassword.getText().toString() != ""){
                return true;
            }else{
                return false;
            }

        }else{
            mBinding.etPhone.setError("Complete Field");
            return false;
        }

    }

    private void checkLogin(){



    }


}