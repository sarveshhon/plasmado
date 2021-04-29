package in.plasmado.fragement;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Patterns;
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
import static in.plasmado.helper.ParentHelper.checkInternet;
import static in.plasmado.helper.ParentHelper.getTimestamp;
import static in.plasmado.helper.UrlHelper.BASE_KEY;
import static in.plasmado.helper.UrlHelper.BASE_URL;
import static in.plasmado.helper.UrlHelper.REGISTRATION;

public class RegisterFragment extends Fragment {


    Context context;

    String bloodGroups[] = {"Select Blood", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
    String gender[] = {"Select Gender", "Male", "Female", "Trans"};
    String states[] = {"Select State", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh",
            "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan",
            "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal", "Andaman and Nicobar", "Chandigarh", "Dadra and Nagar Haveli", "Delhi", "Ladakh", "Lakshadweep", "Jammu and Kashmir", "Puducherry"};
    ArrayAdapter adapterBloodGroup, adapterGender, adapterStates;

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

        adapterStates = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, states);
        mBinding.spinnerStates.setAdapter(adapterStates);

        mBinding.tvAgreement.setText(Html.fromHtml("Agree with our " + getResources().getString(R.string.pp) + " & " + getResources().getString(R.string.tandc)));
        mBinding.tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());

        mBinding.btnCreateAccount.setOnClickListener(v -> {

            if (checkFields()) {
                if (checkInternet(getContext())) {
                    if (mBinding.cbAgreement.isChecked()) {
                        registerUser();
                        Toast.makeText(getActivity(), "Creating Account", Toast.LENGTH_SHORT).show();
                        mBinding.btnCreateAccount.setEnabled(false);
                        new Handler().postDelayed(() -> mBinding.btnCreateAccount.setEnabled(true), 5000);
                    } else {
                        Toast.makeText(getContext(), "Click on Checkbox to Agree.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please Turn On Internet Connection.", Toast.LENGTH_SHORT).show();
                }
            } else {

            }

        });


    }

    private void registerUser() {
        getActivity().getSupportFragmentManager().popBackStack();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL + REGISTRATION + BASE_KEY, response -> {


        }, error -> {
            Log.d("Error", error.getMessage());
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> map = new HashMap<>(0);
                map.put(NAME, mBinding.etFirstName.getText().toString() + " " + mBinding.etLastName.getText().toString());
                map.put(PHONE, mBinding.etPhone.getText().toString());
                map.put(EMAIl, mBinding.etEmail.getText().toString());
                map.put(AGE, mBinding.etAge.getText().toString());
                map.put(PINCODE, mBinding.etPin.getText().toString());
                map.put(CITY, mBinding.etCity.getText().toString());
                map.put(DISTRICT, mBinding.etDistrict.getText().toString());
                map.put(LANDMARK, mBinding.etLandmark.getText().toString());
                map.put(STATE, states[mBinding.spinnerStates.getSelectedItemPosition()]);
                map.put(GENDER, gender[mBinding.spinnerGender.getSelectedItemPosition()]);
                map.put(BLOODGROUP, bloodGroups[mBinding.spinnerBloodGroup.getSelectedItemPosition()]);
                map.put(DATETIME, getTimestamp());
                map.put(PASSWORD, mBinding.etPassword.getText().toString());


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
        String etDistrict = mBinding.etDistrict.getText().toString();
        String etLandmark = mBinding.etLandmark.getText().toString();
        String etCity = mBinding.etCity.getText().toString();
        String etAge = mBinding.etAge.getText().toString();
        String etPin = mBinding.etPin.getText().toString();
        String etEmail = mBinding.etEmail.getText().toString();

        if (!etFirstName.equals("")) {
            if (!etLastName.equals("")) {
                if (!etPhone.equals("") && etPhone.length() == 10) {
                    if (!etPassword.equals("")) {
                        if (!etConfirmPassword.equals("")) {
                            if (etPassword.equals(etConfirmPassword)) {
                                if (mBinding.spinnerStates.getSelectedItemPosition() != 0) {
                                    if (!etDistrict.equals("")) {
                                        if (!etLandmark.equals("")) {
                                            if (!etCity.equals("")) {
                                                if (!etPin.equals("") && etPin.length() == 6) {
                                                    if (mBinding.spinnerBloodGroup.getSelectedItemPosition() != 0) {
                                                        if (mBinding.spinnerGender.getSelectedItemPosition() != 0) {
                                                            if (!etAge.equals("")) {
                                                                if (isValidEmail(etEmail) && !etEmail.equals("")) {
                                                                    return true;
                                                                } else {
                                                                    mBinding.etEmail.setError("Enter Valid Email");
                                                                }
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
                                                    mBinding.etPin.setError("Enter Valid Pin");
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
                                    Toast.makeText(getActivity(), "Select State or Union Territory", Toast.LENGTH_SHORT).show();
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
                    mBinding.etPhone.setError("Enter Valid Phone");
                }
            } else {
                mBinding.etLastName.setError("Enter Last Name");
            }
        } else {
            mBinding.etFirstName.setError("Enter First Name");
        }
        return false;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}