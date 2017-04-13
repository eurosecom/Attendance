package com.eusecom.attendance.mvp;

import android.util.Log;

import java.lang.ref.WeakReference;

public class MainPresenter implements MainMVP.RequiredPresenterOps, MainMVP.PresenterOps {

    // Layer View reference
    private WeakReference<MainMVP.RequiredViewOps> mView;
    // Layer Model reference
    private MainMVP.ModelOps mModel;

    // Configuration change state
    private boolean mIsChangingConfig;

    public MainPresenter(MainMVP.RequiredViewOps mView) {
        this.mView = new WeakReference<>(mView);
        this.mModel = new MainModel(this);
    }

    /**
     * Sent from Activity after a configuration changes
     * @param view  View reference
     */
    @Override
    public void onConfigurationChanged(MainMVP.RequiredViewOps view) {
        mView = new WeakReference<>(view);
    }

    /**
     * Receives {@link //MainActivity#onDestroy()} event
     * @param isChangingConfig  Config change state
     */
    @Override
    public void onDestroy(boolean isChangingConfig) {
        Log.d("Thread MainP onDestroy", Thread.currentThread().getName());
        mView = null;
        mIsChangingConfig = isChangingConfig;
        if ( !isChangingConfig ) {
            mModel.onDestroy();
        }
    }

    /**
     * Called by user interaction from {@link //MainActivity}
     * creates a new Note
     */
    @Override
    public void insertNote(String noteText, String date) {
        Note note = new Note();
        note.setText(noteText);
        note.setDate(date);
        mModel.addNote(note);
    }

    /**
     * Called from {@link //MainActivity},
     * Removes a Note
     */
    @Override
    public void removeNote(Note note) {
        mModel.delNote(note);
    }

    /**
     * Called from {@link MainModel}
     * when a Note is inserted successfully
     */
    @Override
    public void onNoteInsertedNote(Note newNote) {

        mView.get().showToast("New register added " + newNote.getText() + ", " + newNote.getDate());
    }

    @Override
    public void onNoteInserted(Boolean bool) {
        mView.get().showToast("New register added ");
    }

    /**
     * Receives call from {@link MainModel}
     * when Note is removed
     */
    @Override
    public void onNoteRemoved(Note noteRemoved) {
        mView.get().showToast("Note removed");
    }

    /**
     * receive errors
     */
    @Override
    public void onError(String errorMsg) {
        mView.get().showAlert(errorMsg);
    }
}