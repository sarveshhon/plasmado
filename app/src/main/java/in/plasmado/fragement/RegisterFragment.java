package in.plasmado.fragement;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import in.plasmado.R;
import in.plasmado.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    Context context;

    String bloodGroups[] = {"A+","A-","B+","B-","AB+","AB-","O+","O-"};
    String gender[] = {"Male","Female","Trans"};
    ArrayAdapter adapterBloodGroup, adapterGender;

    private FragmentRegisterBinding mBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentRegisterBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        adapterBloodGroup = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item,bloodGroups);
        mBinding.spinnerBloodGroup.setAdapter(adapterBloodGroup);

        adapterGender = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item,gender);
        mBinding.spinnerGender.setAdapter(adapterGender);

        mBinding.btnCreateAccount.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Hi", Toast.LENGTH_SHORT).show();
        });




    }

    private void checkFields(){

    }


}