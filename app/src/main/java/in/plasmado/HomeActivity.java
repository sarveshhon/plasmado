package in.plasmado;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import in.plasmado.fragement.HomeFragment;

import static in.plasmado.helper.ParentHelper.replaceFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        replaceFragment(HomeActivity.this, R.id.flHomeContainer, new HomeFragment());


    }
}