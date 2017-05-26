package com.eusecom.attendance;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EmployeeMvvmActivityRoboelectricTest {

    private EmployeeMvvmActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(EmployeeMvvmActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void shouldHaveDefaultMargin() throws Exception {
        TextView textView = (TextView) activity.findViewById(R.id.greeting);
        int bottomMargin = ((RelativeLayout.LayoutParams) textView.getLayoutParams()).bottomMargin;
        assertEquals(0, bottomMargin);
        int topMargin = ((RelativeLayout.LayoutParams) textView.getLayoutParams()).topMargin;
        assertEquals(0, topMargin);
        int rightMargin = ((RelativeLayout.LayoutParams) textView.getLayoutParams()).rightMargin;
        assertEquals(0, rightMargin);
        int leftMargin = ((RelativeLayout.LayoutParams) textView.getLayoutParams()).leftMargin;
        assertEquals(0, leftMargin);
    }

    @Test
    public void checkActivityNotNull() throws Exception {
        assertNotNull(activity);
    }

    @Test
    public void shouldHaveCorrectAppName() throws Exception {
        String appname = activity.getResources().getString(R.string.app_name);
        assertEquals("Attendance", appname);

    }


}