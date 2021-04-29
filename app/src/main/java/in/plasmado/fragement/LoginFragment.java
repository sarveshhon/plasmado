package in.plasmado.fragement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import in.plasmado.HomeActivity;
import in.plasmado.R;
import in.plasmado.databinding.FragmentLoginBinding;

import static in.plasmado.MainActivity.sharedpreferences;
import static in.plasmado.helper.ParamHelper.AGE;
import static in.plasmado.helper.ParamHelper.BLOODGROUP;
import static in.plasmado.helper.ParamHelper.CITY;
import static in.plasmado.helper.ParamHelper.DISTRICT;
import static in.plasmado.helper.ParamHelper.EMAIl;
import static in.plasmado.helper.ParamHelper.GENDER;
import static in.plasmado.helper.ParamHelper.ID;
import static in.plasmado.helper.ParamHelper.LANDMARK;
import static in.plasmado.helper.ParamHelper.NAME;
import static in.plasmado.helper.ParamHelper.PASSWORD;
import static in.plasmado.helper.ParamHelper.PHONE;
import static in.plasmado.helper.ParamHelper.PINCODE;
import static in.plasmado.helper.ParamHelper.STATE;
import static in.plasmado.helper.ParentHelper.addFragment;
import static in.plasmado.helper.ParentHelper.checkInternet;
import static in.plasmado.helper.ParentHelper.convertMongodbObjToString;
import static in.plasmado.helper.ParentHelper.encrypt;
import static in.plasmado.helper.ParentHelper.startAct;
import static in.plasmado.helper.UrlHelper.BASE_KEY;
import static in.plasmado.helper.UrlHelper.BASE_URL;
import static in.plasmado.helper.UrlHelper.LOGIN;

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

        if (sharedpreferences.getBoolean("LOGIN", false)) {
            startAct(getActivity(), HomeActivity.class);
            getActivity().finish();
        }

        mBinding.btnRegister.setOnClickListener(v -> {
            addFragment(Objects.requireNonNull(getActivity()), R.id.flMainContainer, new RegisterFragment());
        });

        mBinding.btnEmailSupport.setOnClickListener(v -> {

            Intent intent=new Intent(Intent.ACTION_SEND);
            String[] recipients={"plasmadoapp@gmail.com"};
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.putExtra(Intent.EXTRA_SUBJECT,"PlasmaDo Email Support");
            intent.putExtra(Intent.EXTRA_TEXT," ");
            intent.putExtra(Intent.EXTRA_CC,"mailcc@gmail.com");
            intent.setType("text/html");
            intent.setPackage("com.google.android.gm");
            startActivity(Intent.createChooser(intent, "Send mail"));
        });

        mBinding.btnLogin.setOnClickListener(v -> {
            if(checkInternet(getContext())){
                if (checkFields()) {
                    checkLogin();
                    Toast.makeText(getActivity(), "Checking Credentials", Toast.LENGTH_SHORT).show();
                    mBinding.btnLogin.setEnabled(false);
                    new Handler().postDelayed(() -> mBinding.btnLogin.setEnabled(true), 5000);


                } else {
                    Toast.makeText(getActivity(), "Complete Fields", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getContext(), "Please Turn On Internet Connection.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean checkFields() {
        if (!mBinding.etPhone.getText().toString().equals("")) {
            if (!mBinding.etPassword.getText().toString().equals("")) {
                return true;
            } else {
                mBinding.etPassword.setError("Complete Field");
                return false;
            }

        } else {
            mBinding.etPhone.setError("Complete Field");
            return false;
        }

    }

    private void checkLogin() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL + LOGIN + BASE_KEY, response -> {

            try {
                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String uNumber = mBinding.etPhone.getText().toString(),
                        uPassword = mBinding.etPassword.getText().toString();

                if (jsonObject.getString(PHONE).equals(uNumber) && jsonObject.getString(PASSWORD).equals(encrypt(uPassword))) {
                    Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                    startAct(getActivity(), HomeActivity.class);
                    getActivity().finish();

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(PHONE, jsonObject.getString(PHONE));
                    editor.putString(PASSWORD, jsonObject.getString(PASSWORD));
                    editor.putString(NAME, jsonObject.getString(NAME));
                    editor.putString(ID, convertMongodbObjToString(jsonObject.getString(ID)));
                    editor.putString(EMAIl, jsonObject.getString(EMAIl));
                    editor.putString(AGE, jsonObject.getString(AGE));
                    editor.putString(PINCODE, jsonObject.getString(PINCODE));
                    editor.putString(CITY, jsonObject.getString(CITY));
                    editor.putString(DISTRICT, jsonObject.getString(DISTRICT));
                    editor.putString(LANDMARK, jsonObject.getString(LANDMARK));
                    editor.putString(STATE, jsonObject.getString(STATE));
                    editor.putString(GENDER, jsonObject.getString(GENDER));
                    editor.putString(BLOODGROUP, jsonObject.getString(BLOODGROUP));
                    editor.putBoolean("LOGIN", true);
                    editor.apply();

                } else {
                }

            } catch (JSONException error) {
                Toast.makeText(getContext(), "Invalid Login", Toast.LENGTH_SHORT).show();
            }

        }, error -> {
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>();
                map.put(PHONE, mBinding.etPhone.getText().toString());
                map.put(PASSWORD, encrypt(mBinding.etPassword.getText().toString()));

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }



}