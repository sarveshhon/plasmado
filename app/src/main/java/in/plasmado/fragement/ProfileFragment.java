package in.plasmado.fragement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.plasmado.MainActivity;
import in.plasmado.R;
import in.plasmado.databinding.FragmentProfileBinding;

import static in.plasmado.MainActivity.sharedpreferences;
import static in.plasmado.helper.ParamHelper.AGE;
import static in.plasmado.helper.ParamHelper.BLOODGROUP;
import static in.plasmado.helper.ParamHelper.CITY;
import static in.plasmado.helper.ParamHelper.DISTRICT;
import static in.plasmado.helper.ParamHelper.EMAIl;
import static in.plasmado.helper.ParamHelper.GENDER;
import static in.plasmado.helper.ParamHelper.LANDMARK;
import static in.plasmado.helper.ParamHelper.NAME;
import static in.plasmado.helper.ParamHelper.PHONE;
import static in.plasmado.helper.ParamHelper.PINCODE;
import static in.plasmado.helper.ParamHelper.STATE;
import static in.plasmado.helper.ParentHelper.decrypt;
import static in.plasmado.helper.ParentHelper.startAct;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding mBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentProfileBinding.inflate(getLayoutInflater());

        mBinding.tvName.setText("Name: "+sharedpreferences.getString(NAME,"unknown"));
        mBinding.tvPhone.setText("Phone: "+sharedpreferences.getString(PHONE,"unknown"));
        mBinding.tvEmail.setText("Email: "+decrypt(sharedpreferences.getString(EMAIl,"unknown")));
        mBinding.tvAge.setText("Age: "+decrypt(sharedpreferences.getString(AGE,"unknown")));
        mBinding.tvPin.setText("PinCode: "+decrypt(sharedpreferences.getString(PINCODE,"unknown")));
        mBinding.tvCity.setText("City: "+decrypt(sharedpreferences.getString(CITY,"unknown")));
        mBinding.tvDistrict.setText("District: "+decrypt(sharedpreferences.getString(DISTRICT,"unknown")));
        mBinding.tvLandmark.setText("Landmark: "+decrypt(sharedpreferences.getString(LANDMARK,"unknown")));
        mBinding.tvState.setText("State: "+decrypt(sharedpreferences.getString(STATE,"unknown")));
        mBinding.tvGender.setText("Gender: "+decrypt(sharedpreferences.getString(GENDER,"unknown")));
        mBinding.tvBloodGroup.setText("Blood Group: "+sharedpreferences.getString(BLOODGROUP,"unknown"));

        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {



    }
}