package in.plasmado;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import in.plasmado.databinding.ActivityMainBinding;
import in.plasmado.fragement.LoginFragment;

import static in.plasmado.helper.ParentHelper.replaceFragment;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        replaceFragment(this, R.id.flMainContainer, new LoginFragment());


    }

}