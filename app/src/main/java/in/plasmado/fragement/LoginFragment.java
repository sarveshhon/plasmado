package in.plasmado.fragement;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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
import static in.plasmado.helper.ParamHelper.PASSWORD;
import static in.plasmado.helper.ParamHelper.PHONE;
import static in.plasmado.helper.ParentHelper.addFragment;
import static in.plasmado.helper.ParentHelper.encrypt;
import static in.plasmado.helper.ParentHelper.startAct;
import static in.plasmado.helper.UrlHelper.BASE_KEY;
import static in.plasmado.helper.UrlHelper.BASE_URL;
import static in.plasmado.helper.UrlHelper.LOGIN;
import static in.plasmado.helper.UrlHelper.databaseReference;

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

        mBinding.btnLogin.setOnClickListener(v -> {
            if (checkFields()) {

                checkLogin();

            } else {
                Toast.makeText(getActivity(), "Complete Fields", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean checkFields() {

        mBinding.etPhone.getText().toString();
        if (mBinding.etPhone.getText().toString() != "") {

            mBinding.etPassword.getText().toString();
            if (mBinding.etPassword.getText().toString() != "") {
                return true;
            } else {
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
                String  uNumber = mBinding.etPhone.getText().toString(),
                        uPassword = mBinding.etPassword.getText().toString();

                if (jsonObject.getString("phone").equals(uNumber) && jsonObject.getString("password").equals(uPassword)) {
                    startAct(getActivity(), HomeActivity.class);
                    getActivity().finish();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(PHONE, mBinding.etPhone.getText().toString());
                    editor.putString(PASSWORD, mBinding.etPassword.getText().toString());
                    editor.putBoolean("LOGIN", true);
                    editor.apply();

                } else {
                    Toast.makeText(getActivity(), "Invalid Login", Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException error){

            }

        }, error -> {

        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>();
                map.put(PHONE, mBinding.etPhone.getText().toString());
                map.put(PASSWORD, mBinding.etPassword.getText().toString());

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }


}