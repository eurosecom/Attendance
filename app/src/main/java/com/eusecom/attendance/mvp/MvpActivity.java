package com.eusecom.attendance.mvp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.eusecom.attendance.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//by https://github.com/tinmegali/Tutorial-Android-Model-View-Presenter-MVP/blob/master/MainModel.java

public class MvpActivity extends AppCompatActivity
        implements MainMVP.RequiredViewOps {

    protected final String TAG = getClass().getSimpleName();

    // Responsible to maintain the Objects state
    // during changing configuration
    private final StateMaintainer mStateMaintainer =
            new StateMaintainer( this.getFragmentManager(), TAG );

    // Presenter operations
    private MainMVP.PresenterOps mPresenter;
    //private EditText mText, mDate;
    @Bind(R.id.text) EditText mText;
    @Bind(R.id.date) EditText mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startMVPOps();
        setContentView(R.layout.activity_mvp);
        ButterKnife.bind(this);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener((View v) -> {

                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                mPresenter.insertNote(mText.getText().toString(), mDate.getText().toString());

        });

    }


    /**
     * Initialize and restart the Presenter.
     * This method should be called after {@link //Activity#onCreate(Bundle)}
     */
    public void startMVPOps() {
        try {
            if ( mStateMaintainer.firstTimeIn() ) {
                Log.d(TAG, "onCreate() called for the first time");
                initialize(this);
            } else {
                Log.d(TAG, "onCreate() called more than once");
                reinitialize(this);
            }
        } catch ( InstantiationException | IllegalAccessException e ) {
            Log.d(TAG, "onCreate() " + e );
            throw new RuntimeException( e );
        }
    }


    /**
     * Initialize relevant MVP Objects.
     * Creates a Presenter instance, saves the presenter in {@link StateMaintainer}
     */
    private void initialize( MainMVP.RequiredViewOps view )
            throws InstantiationException, IllegalAccessException{
        mPresenter = new MainPresenter(view);
        mStateMaintainer.put(MainMVP.PresenterOps.class.getSimpleName(), mPresenter);
    }

    /**
     * Recovers Presenter and informs Presenter that occurred a config change.
     * If Presenter has been lost, recreates a instance
     */
    private void reinitialize( MainMVP.RequiredViewOps view)
            throws InstantiationException, IllegalAccessException {
        mPresenter = mStateMaintainer.get( MainMVP.PresenterOps.class.getSimpleName() );

        if ( mPresenter == null ) {
            Log.w(TAG, "recreating Presenter");
            initialize( view );
        } else {
            mPresenter.onConfigurationChanged( view );
        }
    }


    // Show AlertDialog
    @Override
    public void showAlert(String msg) {
        // show alert Box
    }

    // Show Toast
    @Override
    public void showToast(String msg) {
        Toast.makeText(MvpActivity.this, msg,Toast.LENGTH_SHORT).show();
    }
}