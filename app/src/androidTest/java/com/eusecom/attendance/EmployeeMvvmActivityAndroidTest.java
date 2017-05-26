package com.eusecom.attendance;

//monkey http://stackoverflow.com/questions/29678088/testing-android-app-using-android-studio
//in terminal window
//cd c:\sdkandroidstudio\platform-tools\

//the packagename is without com.
//adb shell monkey -p upday.droidconmvvm -v 500

import android.test.suitebuilder.annotation.MediumTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;


@MediumTest
@RunWith(AndroidJUnit4.class)
public class EmployeeMvvmActivityAndroidTest {

    public static final int ADAPTER_COUNT = 3;

    @Rule
    public ActivityTestRule<EmployeeMvvmActivity> rule  = new  ActivityTestRule<>(EmployeeMvvmActivity.class);

    @Test
    public void testRecyclerviewer() throws Exception {
        EmployeeMvvmActivity activity = rule.getActivity();

        //recyclerview exist test
        View viewRcvw = activity.findViewById(R.id.employees_list);
        assertThat(viewRcvw,notNullValue());



    }

    @Test
    public void testSpinner() throws Exception {
        EmployeeMvvmActivity activity = rule.getActivity();

        //spinner test
        View viewById = activity.findViewById(R.id.languages);
        assertThat(viewById,notNullValue());

        Spinner languages = (Spinner) viewById;
        SpinnerAdapter adapter = languages.getAdapter();

        assertTrue(languages.getOnItemSelectedListener() != null);
        assertTrue(adapter != null);
        assertEquals(adapter.getCount(),ADAPTER_COUNT);


    }



}

