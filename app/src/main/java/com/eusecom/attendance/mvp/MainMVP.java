/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * by http://www.tinmegali.com/en/model-view-presenter-mvp-in-android-part-2/
 *
 */

package com.eusecom.attendance.mvp;

import java.lang.ref.WeakReference;

/*
 * Aggregates all communication operations between MVP pattern layer: 
 * Model, View and Presenter
 */
public interface MainMVP {

    /**
     * View mandatory methods. Available to Presenter
     *      Presenter -> View
     */
    interface RequiredViewOps {
        void showToast(String msg);
        void showAlert(String msg);
        // any other ops
    }

    /**
     * Operations offered from Presenter to View
     *      View -> Presenter
     */
    interface PresenterOps{
        void onConfigurationChanged(RequiredViewOps view);
        void onDestroy(boolean isChangingConfig);
        void insertNote(String noteText, String date);
        void removeNote(Note note);
        // any other ops to be called from View
    }

    /**
     * Operations offered from Presenter to Model
     *      Model -> Presenter
     */
    interface RequiredPresenterOps {
        void onNoteInsertedNote(Note novaNota);
        void onNoteInserted(Boolean bool);
        void onNoteRemoved(Note notaRemovida);
        void onError(String errorMsg);
        // Any other returning operation Model -> Presenter
    }

    /**
     * Model operations offered to Presenter
     *      Presenter -> Model
     */
    interface ModelOps {
        void addNote(Note note);
        void delNote(Note note);
        void onDestroy();
        // Any other data operation
    }
}