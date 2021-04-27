package in.plasmado.fragement;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import in.plasmado.HomeActivity;
import in.plasmado.R;
import in.plasmado.databinding.FragmentLoginBinding;

import static in.plasmado.helper.ParamHelper.PASSWORD;
import static in.plasmado.helper.ParamHelper.PHONE;
import static in.plasmado.helper.ParentHelper.addFragment;
import static in.plasmado.helper.ParentHelper.encrypt;
import static in.plasmado.helper.ParentHelper.startAct;
import static in.plasmado.helper.UrlHelper.BASE_KEY;
import static in.plasmado.helper.UrlHelper.BASE_URL;
import static in.plasmado.helper.UrlHelper.LOGIN;
import static in.plasmado.helper.UrlHelper.REGISTRATION;

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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL+ LOGIN + BASE_KEY, response -> {

            if(!response.equals("null")){
                startAct(getActivity(), HomeActivity.class);
            }else{
                Toast.makeText(getActivity(), "Invalid Login", Toast.LENGTH_SHORT).show();
            }

        }, error -> {

        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>(0);
                map.put(PHONE,encrypt(mBinding.etPhone.getText().toString()));
                map.put(PASSWORD,encrypt(mBinding.etPassword.getText().toString()));

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }


}