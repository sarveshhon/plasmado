package in.plasmado.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ParentHelper {

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

}
