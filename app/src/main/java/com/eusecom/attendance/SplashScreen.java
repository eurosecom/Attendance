package com.eusecom.attendance;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import com.eusecom.attendance.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class SplashScreen extends Activity {

    private final static String TAG = SplashScreen.class.getSimpleName();
    private static final String USER_IS_LOGIN = "UserIsLogin";
    private static final String UI_ID_FIREBASE = "UiIdFirebase";
    // Duration of wait
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        mAuth = FirebaseAuth.getInstance();
        //mUser = User.getInstance();

        // New Handler to start the Menu-Activity and close this Splash-Screen after some seconds.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("run at handler", "Start splash screen");
                mAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            // User is signed in
                            //mUser.setLoginState(true);
                            //mUser.setUiIdFirebase(user.getUid());

                            Log.d(TAG, "User state : signed_in:" + user.getUid());
                            //StartMainActivity();
                            //SplashScreen.this.finish();
                        } else {
                            // User is signed out
                            //mUser.setLoginState(false);

                            Log.d(TAG, "User state : signed_out");
                            //StartSignInActivity();
                            //SplashScreen.this.finish();
                        }
                    }
                };
                mAuth.addAuthStateListener(mAuthListener);
            }
        }, SPLASH_DISPLAY_LENGTH);

        //mAuth.addAuthStateListener(mAuthListener);
    }

    public void StartMainActivity() {
        Log.d(TAG, "User is in , Start MainActivity");
        //Intent i = new Intent(SplashScreen.this,MainActivity.class);
        //startActivity(i);
    }

    public void StartSignInActivity() {
        Log.d(TAG, "User need to sign in , Start SignInActivity");
        //Intent i = new Intent(SplashScreen.this,SignInActivity.class);
        //startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}