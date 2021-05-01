package in.plasmado;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import in.plasmado.fragement.HomeFragment;

import static in.plasmado.MainActivity.sharedpreferences;
import static in.plasmado.helper.ParamHelper.ID;
import static in.plasmado.helper.ParentHelper.replaceFragment;
import static in.plasmado.helper.ParentHelper.showCustomDialog;

public class HomeActivity extends AppCompatActivity {

    Dialog dialog, dialogBlock;
    DatabaseReference db;
    private static String videoLink = "https://sites.google.com/view/plasmadoapp/home";
    String TEMP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        replaceFragment(HomeActivity.this, R.id.flHomeContainer, new HomeFragment());

        dialog = new Dialog(HomeActivity.this);
        dialogBlock = new Dialog(HomeActivity.this);

        // Configure Dialog Propertied
        showCustomDialog(dialog, getResources().getString(R.string.app_update), R.layout.dialog_update);
        dialog.findViewById(R.id.btnYes).setOnClickListener(v -> {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        });

        // Configure Dialog Propertied
        showCustomDialog(dialogBlock, getResources().getString(R.string.app_block), R.layout.dialog_block);
        dialogBlock.findViewById(R.id.btnYes).setOnClickListener(v -> {
            Intent intent2 = new Intent(Intent.ACTION_SEND);
            String[] recipients2 = {"plasmadoapp@gmail.com"};
            intent2.putExtra(Intent.EXTRA_EMAIL, recipients2);
            intent2.putExtra(Intent.EXTRA_SUBJECT, "PlasmaDo Block Email Support | " + sharedpreferences.getString(ID, "Unknown"));
            intent2.putExtra(Intent.EXTRA_TEXT, " ");
            intent2.putExtra(Intent.EXTRA_CC, "mailcc@gmail.com");
            intent2.setType("text/html");
            intent2.setPackage("com.google.android.gm");
            startActivity(Intent.createChooser(intent2, "Send mail"));
        });

        try {
            db = FirebaseDatabase.getInstance().getReference();
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("appversion").exists()) {
                        if (!(snapshot.child("appversion").getValue().toString().equals(String.valueOf(BuildConfig.VERSION_CODE)))) {
                            dialog.show();
                        } else {
                            dialog.dismiss();
                        }
                    }
                    if (snapshot.child("howtouse").exists()) {
                        try {
                            videoLink = snapshot.child("howtouse").getValue().toString();
                        } catch (Exception e) {

                        }
                    }

                    if (snapshot.child("users").child(sharedpreferences.getString(ID, "unknown")).exists()) {
                        if (snapshot.child("users").child(sharedpreferences.getString(ID, "unknown")).getValue().toString().equals("true")) {
                            dialogBlock.show();
                        } else {
                            dialogBlock.dismiss();
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } catch (Exception e) {

        }


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
                    String applink = "✅ *Download App* ✅\nhttps://play.google.com/store/apps/details?id=" + getPackageName();
                    String applink_h = "✅ *ऐप डाउनलोड करें* ✅\nhttps://play.google.com/store/apps/details?id=" + getPackageName();
                    String shareMessage = getResources().getString(R.string.sharetext);
                    String shareMessage_h = getResources().getString(R.string.sharetext_h);
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                    shareMessage = shareMessage + applink + "\n\n" + shareMessage_h + applink_h;
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
                break;
            case R.id.mVideo:
                Intent browserIntent0 = new Intent(Intent.ACTION_VIEW, Uri.parse(videoLink));
                startActivity(browserIntent0);
                break;
            case R.id.m2:
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                break;
            case R.id.m3:
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                String[] recipients1 = {"plasmadoapp@gmail.com"};
                intent1.putExtra(Intent.EXTRA_EMAIL, recipients1);
                intent1.putExtra(Intent.EXTRA_SUBJECT, "PlasmaDo Feedback | " + sharedpreferences.getString(ID, "Unknown"));
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
                intent2.putExtra(Intent.EXTRA_SUBJECT, "PlasmaDo Email Support | " + sharedpreferences.getString(ID, "Unknown"));
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
                Intent intent3 = new Intent(HomeActivity.this, AboutActivity.class);
                startActivity(intent3);
                break;
            case R.id.m8:
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