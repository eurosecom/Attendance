package com.eusecom.attendance;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.mvvmdatamodel.IDataModel;
import com.eusecom.attendance.mvvmschedulers.ImmediateSchedulerProvider;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static java.security.AccessController.getContext;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EmployeeMvvmActivityRoboelectricTest {

    private EmployeeMvvmActivity activity;

    @Mock
    private IDataModel mDataModel;

    private EmployeeMvvmViewModel mMainViewModel;

    Context context;


    @Before
    public void setUp() throws Exception {

        //if exist TestAplicationname.java roboelectric work first for firebase init
        MockitoAnnotations.initMocks(this);
        mMainViewModel = new EmployeeMvvmViewModel(mDataModel, new ImmediateSchedulerProvider());

        activity = Robolectric.buildActivity(EmployeeMvvmActivity.class)
                .create()
                .resume()
                .get();


    }

    //recyclerview test

    @Test
    public void testRecyclerview_getObservableKeyEditedEmployee() {

        Employee editedEmployee =  new Employee( "eurosecom3", "eurosecom3@gmail.com", "0", "12345678", "0");

        String mockKeyf =  "K6u6ay4ghKbXRh7ZJTAEBoKLazm3";
        Observable<String> mockObservable = Observable.just(mockKeyf);
        doReturn(mockObservable).when(mDataModel).getObservableKeyFBeditUser(editedEmployee);

        mMainViewModel.saveEditEmloyee(editedEmployee, "eurosecom3", "3", "12345678", "99", "0");

        TestSubscriber<String> testSubscriber = new TestSubscriber<>();

        mMainViewModel.getObservableKeyEditedEmployee().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        List<String> liststring = testSubscriber.getOnNextEvents();
        String obserdedkeyf = liststring.get(0).toString();
        System.out.println("editedkeyf " + liststring.get(0).toString());
        Assert.assertEquals(obserdedkeyf, mockKeyf);
    }

    @Test
    public void testRecyclerview_getObservableFBusersEmployee() {

        List<Employee> mockEmployees =  Arrays
                .asList(new Employee("andrejd", "1"),
                        new Employee("petere", "2"),
                        new Employee("pavols", "3"));

        Observable<List<Employee>> mockObservable = Observable.just(mockEmployees);
        doReturn(mockObservable).when(mDataModel).getObservableFBusersEmployee();

        TestSubscriber<List<Employee>> testSubscriber = new TestSubscriber<>();

        mMainViewModel.getObservableFBusersEmployee().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertUnsubscribed();
        testSubscriber.assertTerminalEvent();
        String threadname = testSubscriber.getLastSeenThread().getName();
        System.out.println("threadname " + threadname);

        List<List<Employee>> listlistresult = testSubscriber.getOnNextEvents();
        List<Employee> listresult = listlistresult.get(0);
        System.out.println("listresult0 " + listresult.get(0).getUsername());
        System.out.println("listresult1 " + listresult.get(1).getUsername());

        Assert.assertEquals(mockEmployees.get(0).getUsername(), listresult.get(0).getUsername());

        assertThat(listresult, hasSize(3));

    }

    //spinner test

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