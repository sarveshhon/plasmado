package in.plasmado.fragement;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.plasmado.R;
import in.plasmado.databinding.FragmentRegisterBinding;

import static in.plasmado.helper.ParamHelper.AGE;
import static in.plasmado.helper.ParamHelper.BLOODGROUP;
import static in.plasmado.helper.ParamHelper.CITY;
import static in.plasmado.helper.ParamHelper.DATETIME;
import static in.plasmado.helper.ParamHelper.DISTRICT;
import static in.plasmado.helper.ParamHelper.EMAIl;
import static in.plasmado.helper.ParamHelper.GENDER;
import static in.plasmado.helper.ParamHelper.LANDMARK;
import static in.plasmado.helper.ParamHelper.NAME;
import static in.plasmado.helper.ParamHelper.PASSWORD;
import static in.plasmado.helper.ParamHelper.PHONE;
import static in.plasmado.helper.ParamHelper.PINCODE;
import static in.plasmado.helper.ParamHelper.STATE;
import static in.plasmado.helper.ParentHelper.encrypt;
import static in.plasmado.helper.UrlHelper.BASE_KEY;
import static in.plasmado.helper.UrlHelper.BASE_URL;
import static in.plasmado.helper.UrlHelper.REGISTRATION;

public class RegisterFragment extends Fragment {


    Context context;

    String bloodGroups[] = {"Select Blood", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
    String gender[] = {"Select Gender", "Male", "Female", "Trans"};
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

        adapterBloodGroup = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, bloodGroups);
        mBinding.spinnerBloodGroup.setAdapter(adapterBloodGroup);

        adapterGender = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, gender);
        mBinding.spinnerGender.setAdapter(adapterGender);

        mBinding.btnCreateAccount.setOnClickListener(v -> {

            if (checkFields()) {
                registerUser();
            } else {

            }

        });


    }

    private void registerUser() {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL + REGISTRATION + BASE_KEY, response -> {

            getActivity().getSupportFragmentManager().popBackStack();

        }, error -> {
            Log.d("Error", error.getMessage());
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>(0);
                map.put(NAME, encrypt(mBinding.etFirstName.getText().toString() + mBinding.etLastName.getText().toString()));
                map.put(PHONE, encrypt(mBinding.etPhone.getText().toString()));
                map.put(EMAIl, encrypt(mBinding.etEmail.getText().toString()));
                map.put(AGE, encrypt(mBinding.etAge.getText().toString()));
                map.put(PINCODE, encrypt(mBinding.etPin.getText().toString()));
                map.put(CITY, encrypt(mBinding.etCity.getText().toString()));
                map.put(DISTRICT, encrypt(mBinding.etDistrict.getText().toString()));
                map.put(LANDMARK, encrypt(mBinding.etLandmark.getText().toString()));
                map.put(STATE, encrypt(mBinding.etState.getText().toString()));
                map.put(GENDER, encrypt(gender[mBinding.spinnerGender.getSelectedItemPosition()]));
                map.put(BLOODGROUP, encrypt(bloodGroups[mBinding.spinnerBloodGroup.getSelectedItemPosition()]));
                map.put(DATETIME, timeStamp);
                map.put(PASSWORD, encrypt(mBinding.etPassword.getText().toString()));


                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    private boolean checkFields() {

        String etFirstName = mBinding.etFirstName.getText().toString();
        String etLastName = mBinding.etLastName.getText().toString();
        String etPhone = mBinding.etPhone.getText().toString();
        String etPassword = mBinding.etPassword.getText().toString();
        String etConfirmPassword = mBinding.etConfirmPassword.getText().toString();
        String etState = mBinding.etState.getText().toString();
        String etDistrict = mBinding.etDistrict.getText().toString();
        String etLandmark = mBinding.etLandmark.getText().toString();
        String etCity = mBinding.etCity.getText().toString();
        String etAge = mBinding.etAge.getText().toString();
        String etPin = mBinding.etPin.getText().toString();

        if (!etFirstName.equals("")) {
            if (!etLastName.equals("")) {
                if (!etPhone.equals("")) {
                    if (!etPassword.equals("")) {
                        if (!etConfirmPassword.equals("")) {
                            if (etPassword.equals(etConfirmPassword)) {
                                if (!etState.equals("")) {
                                    if (!etDistrict.equals("")) {
                                        if (!etLandmark.equals("")) {
                                            if (!etCity.equals("")) {
                                                if (!etPin.equals("")) {
                                                    if (mBinding.spinnerBloodGroup.getSelectedItemPosition() != 0) {
                                                        if (mBinding.spinnerGender.getSelectedItemPosition() != 0) {
                                                            if (!etAge.equals("")) {
                                                                return true;
                                                            } else {
                                                                mBinding.etAge.setError("Enter Age");
                                                            }
                                                        } else {
                                                            Toast.makeText(getActivity(), "Select Gender", Toast.LENGTH_SHORT).show();
                                                        }

                                                    } else {
                                                        Toast.makeText(getActivity(), "Select Blood Group", Toast.LENGTH_SHORT).show();
                                                    }

                                                } else {
                                                    mBinding.etPin.setError("Enter Pin");
                                                }

                                            } else {
                                                mBinding.etCity.setError("Enter City");
                                            }

                                        } else {
                                            mBinding.etLandmark.setError("Enter Landmark Password");
                                        }

                                    } else {
                                        mBinding.etDistrict.setError("Enter District Password");
                                    }

                                } else {
                                    mBinding.etState.setError("Enter District Password");
                                }
                            } else {
                                mBinding.etConfirmPassword.setError("Enter Same Password");
                            }

                        } else {
                            mBinding.etConfirmPassword.setError("Enter Confirm Password");
                        }

                    } else {
                        mBinding.etPassword.setError("Enter Password");
                    }

                } else {
                    mBinding.etPhone.setError("Enter Phone");
                }
            } else {
                mBinding.etLastName.setError("Enter Last Name");
            }
        } else {
            mBinding.etFirstName.setError("Enter First Name");
        }
        return false;
    }


}