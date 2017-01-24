package com.optimalotaku.overguide;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mBattleTagView;
    private View mProgressView;
    private View mLoginFormView;
    private EditText mRegionView;
    private EditText mPlatformView;
    private TextView mResponseView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mBattleTagView = (EditText) findViewById(R.id.battletag);
        mRegionView = (EditText) findViewById(R.id.region);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mPlatformView = (EditText) findViewById(R.id.platform);
        mResponseView = (TextView) findViewById(R.id.response);
    }

    public void onClick(View view) throws InterruptedException, ExecutionException, IOException {
        //use apihelper to verify battletag
        final String enteredTag = mBattleTagView.getText().toString();
        String newTag = cleanTag(enteredTag);
        APIchecker checkTag = new APIchecker();
        checkTag.execute(newTag, mPlatformView.getText().toString(), mRegionView.getText().toString());
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if(checkTag.get()!= null){
            mResponseView.setText("Success! Parameters"  + newTag + mPlatformView.getText().toString() + mRegionView.getText().toString() + "Saved!");
            String FILENAME1 = "BATTLETAG";
            String FILENAME2 = "REGION";
            String FILENAME3 = "PLATFORM";

            FileOutputStream fos = openFileOutput(FILENAME1, Context.MODE_PRIVATE);
            fos.write(newTag.getBytes());
            fos.close();
            FileOutputStream fos2 = openFileOutput(FILENAME2, Context.MODE_PRIVATE);
            fos2.write(mRegionView.getText().toString().getBytes());
            fos2.close();
            FileOutputStream fos3 = openFileOutput(FILENAME3, Context.MODE_PRIVATE);
            fos3.write(mPlatformView.getText().toString().getBytes());
            fos3.close();
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        }
        else{
            mResponseView.setText("Failure! Account not found! Please try again.");
        }

    }

    public String cleanTag(String battletag){
        String cleanTag;
        cleanTag = battletag.replaceAll("-", "#");

        return cleanTag;
    }



}

