package in.plasmado;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import in.plasmado.fragement.HomeFragment;

import static in.plasmado.MainActivity.sharedpreferences;
import static in.plasmado.helper.ParentHelper.replaceFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        replaceFragment(HomeActivity.this, R.id.flHomeContainer, new HomeFragment());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m1:
                try {
                    String applink = "✅ *Download App* ✅\nhttps://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                    String applink_h = "✅ *ऐप डाउनलोड करें* ✅\nhttps://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                    String shareMessage = getResources().getString(R.string.sharetext);
                    String shareMessage_h = getResources().getString(R.string.sharetext_h);
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                    shareMessage = shareMessage + applink+"\n\n"+shareMessage_h + applink_h;
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
                break;
            case R.id.m2:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.m3:
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                String[] recipients1 = {"plasmadoapp@gmail.com"};
                intent1.putExtra(Intent.EXTRA_EMAIL, recipients1);
                intent1.putExtra(Intent.EXTRA_SUBJECT, "PlasmaDo Feedback");
                intent1.putExtra(Intent.EXTRA_TEXT, " ");
                intent1.putExtra(Intent.EXTRA_CC, "mailcc@gmail.com");
                intent1.setType("text/html");
                intent1.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent1, "Send mail"));
                break;
            case R.id.m4:
                Intent intent2 = new Intent(Intent.ACTION_SEND);
                String[] recipients2 = {"plasmadoapp@gmail.com"};
                intent2.putExtra(Intent.EXTRA_EMAIL, recipients2);
                intent2.putExtra(Intent.EXTRA_SUBJECT, "PlasmaDo Email Support");
                intent2.putExtra(Intent.EXTRA_TEXT, " ");
                intent2.putExtra(Intent.EXTRA_CC, "mailcc@gmail.com");
                intent2.setType("text/html");
                intent2.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent2, "Send mail"));
                break;
            case R.id.m5:
                Intent browserIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/plasmadoapp/home/privacy-policy?authuser=0"));
                startActivity(browserIntent1);
                break;
            case R.id.m6:
                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/plasmadoapp/home/terms_and_conditions?authuser=0"));
                startActivity(browserIntent2);
                break;
            case R.id.m7:
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("LOGIN", false);
                editor.apply();
                Intent i3 = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(i3);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}