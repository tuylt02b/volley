package com.example.tuy.volley;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by TienLQ on 3/10/15.
 */
public class Helpers {
    public static final String TAG = "Helpers";

    public static String md5(String text) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(text.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (int i = 0; i < len; i++) {
                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 0x0f, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.d("---", e.getMessage());
        }
        return null;
    }

    /**
     * Disable actionbar show/hide animation
     *
     * @param actionBar
     */

    public static void disableActionBarAnimation(ActionBar actionBar) {
        try {
            actionBar.getClass().getDeclaredMethod("setShowHideAnimationEnabled", boolean.class).invoke(actionBar, false);
        } catch (Exception exception) {
            try {
                Field mActionBarField = actionBar.getClass().getSuperclass().getDeclaredField("mActionBar");
                mActionBarField.setAccessible(true);
                Object icsActionBar = mActionBarField.get(actionBar);
                Field mShowHideAnimationEnabledField = icsActionBar.getClass().getDeclaredField("mShowHideAnimationEnabled");
                mShowHideAnimationEnabledField.setAccessible(true);
                mShowHideAnimationEnabledField.set(icsActionBar, false);
                Field mCurrentShowAnimField = icsActionBar.getClass().getDeclaredField("mCurrentShowAnim");
                mCurrentShowAnimField.setAccessible(true);
                mCurrentShowAnimField.set(icsActionBar, null);
                //icsActionBar.getClass().getDeclaredMethod("setShowHideAnimationEnabled", boolean.class).invoke(icsActionBar, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    
}
