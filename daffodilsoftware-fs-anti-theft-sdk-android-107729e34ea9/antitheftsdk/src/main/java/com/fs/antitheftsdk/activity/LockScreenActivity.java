package com.fs.antitheftsdk.activity;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fs.antitheftsdk.R;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/*This activity is for locked screen dialog when triggered remote unlock */
public class LockScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showWhenLockedAndTurnScreenOn();
        makeAlertDialog("", getString(R.string.lock_message), getString(R.string.okay), getString(R.string.cancel));
    }

    private void showWhenLockedAndTurnScreenOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            this.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            );
        }
    }


    public void makeAlertDialog(String title, String message, String textPositive, String textNegative) {

        new AlertDialog.Builder(this).setTitle(title)
                .setMessage(message)
                .setPositiveButton(textPositive, (di,i)-> di.dismiss())
                .setNegativeButton(textNegative,(di,i)-> di.dismiss())
                .setOnDismissListener(di-> finish())
                .create()
                .show();
    }
}
