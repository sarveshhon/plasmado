package in.plasmado;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import in.plasmado.databinding.ActivityMainBinding;
import in.plasmado.fragement.LoginFragment;

import static in.plasmado.helper.ParentHelper.replaceFragment;

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences sharedpreferences;
    public ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        replaceFragment(this, R.id.flMainContainer, new LoginFragment());
        sharedpreferences = getSharedPreferences("plasmado", Context.MODE_PRIVATE);


    }
}