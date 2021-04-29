package in.plasmado.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import in.plasmado.R;

public class ParentHelper {

    public static String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date());
    public static final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static void replaceFragment(FragmentActivity activity, int layout, Fragment fragment){
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(layout, fragment)
                .commit();
    }

    public static void addFragment(FragmentActivity activity, int layout, Fragment fragment){
        activity.getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(fragment.getTag())
                .replace(layout, fragment)
                .commit();
    }

    public static String decrypt(String text) {
        try {
            Cipher ciper = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec key = new SecretKeySpec(UtilsDefault.KEY.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec iv = new IvParameterSpec(UtilsDefault.IV.getBytes(StandardCharsets.UTF_8), 0, ciper.getBlockSize());
            ciper.init(Cipher.DECRYPT_MODE, key, iv);
            return new String(ciper.doFinal(Base64.decode(text.getBytes(), Base64.DEFAULT)));
        } catch (Exception e) {
            return text;
        }
    }

    public static String encrypt(String text) {
        try {
            Cipher ciper = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec key = new SecretKeySpec(UtilsDefault.KEY.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec iv = new IvParameterSpec(UtilsDefault.IV.getBytes(StandardCharsets.UTF_8), 0, ciper.getBlockSize());
            ciper.init(Cipher.ENCRYPT_MODE, key, iv);
            return new String(Base64.encode(ciper.doFinal(text.getBytes()), Base64.DEFAULT));
        } catch (Exception e) {
            return text;
        }
    }

    public static void startAct(Context context, Class activity){
        Intent i = new Intent(context, activity);
        context.startActivity(i);
    }


    // Show Custom Dialog
    public static void showCustomDialog(Dialog dialog, String txt, int layout){
        dialog.setContentView(layout);
        dialog.setCancelable(false);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        tvTitle.setText(txt);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }


    // Method to Convert MongoDB Object to String
    public static String convertMongodbObjToString(String id){
        return id.substring(id.indexOf('"')+8,id.lastIndexOf('"'));
    }

    public static boolean checkInternet(Context context){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return connected = true;
        }
        else {
            return connected = false;
        }
    }

}
