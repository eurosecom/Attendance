package com.eusecom.attendance;


import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.mvvmdatamodel.IDataModel;
import com.eusecom.attendance.mvvmschedulers.ImmediateSchedulerProvider;
import com.eusecom.attendance.rxbus.RxBus;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.flowables.ConnectableFlowable;
import rx.Observable;
import rx.functions.Action1;
import rx.observers.TestSubscriber;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.robolectric.Shadows.shadowOf;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EmployeeMvvmActivityRoboelectricTest {

    private EmployeeMvvmActivity activity;

    @Mock
    private IDataModel mDataModel;

    private EmployeeMvvmViewModel mMainViewModel;

    private RecyclerView mRecycler;

    EmployeesRxAdapter mAdapter;

    private ActivityController<EmployeeMvvmActivity> activityController;
    private ShadowActivity myActivityShadow;

    private RxBus _rxBus;
    private CompositeDisposable _disposables;


    @Before
    public void setUp() throws Exception {

        //if exist TestAplicationname.java roboelectric work first for firebase init
        MockitoAnnotations.initMocks(this);
        mMainViewModel = new EmployeeMvvmViewModel(mDataModel, new ImmediateSchedulerProvider());

        activity = Robolectric.buildActivity(EmployeeMvvmActivity.class)
                .create()
                .resume()
                .get();

        mRecycler = (RecyclerView) activity.findViewById(R.id.employees_list);


        _rxBus = getRxBusSingleton();
        _disposables = new CompositeDisposable();

        ConnectableFlowable<Object> tapEventEmitter = _rxBus.asFlowable().publish();
        //rewrite _disposables from retrolambda to classic annotation bebause of testcompiler has problem with lambda expression

        _disposables
                .add(tapEventEmitter.subscribe(event -> {

                    System.out.println("rxBus tapEventEmitter");

                }));


        _disposables.add(tapEventEmitter.connect());

        mAdapter =  new EmployeesRxAdapter(Collections.<Employee>emptyList(), _rxBus);

    }

    public RxBus getRxBusSingleton() {
        if (_rxBus == null) {
            _rxBus = new RxBus();
        }

        return _rxBus;
    }

    @After
    public void tearDown() {
        _disposables.dispose();
    }



    //recyclerview test

    @Test
    public void checkActivityNotNull() throws Exception {
        assertNotNull(activity);
    }

    @Test
    public void shouldHaveCorrectAppName() throws Exception {
        String appname = activity.getResources().getString(R.string.app_name);
        assertEquals("Attendance", appname);
    }

    @Test
    public void mRecyclerShouldNotBeNull() throws Exception {
        assertViewIsVisible(mRecycler);
        assertNotNull(mRecycler.getAdapter());

    }

    @Test
    public void testRecyclerview_setDataToAdapter() {

        List<Employee> mockEmployees =  Arrays
                .asList(new Employee("andrejd", "1"),
                        new Employee("petere", "2"),
                        new Employee("pavols", "3"));

        mAdapter.setData(mockEmployees);
        Assert.assertEquals(mAdapter.getItemCount(), 3);

    }

    @Test
    public void testRecyclerview_whatDesign() {


        int leftMargin = ((RelativeLayout.LayoutParams) mRecycler.getLayoutParams()).leftMargin;
        assertEquals(0, leftMargin);
        int righttMargin = ((RelativeLayout.LayoutParams) mRecycler.getLayoutParams()).rightMargin;
        assertEquals(0, righttMargin);


    }

    @Test
    public void testRecyclerview_clickToitems() {

        List<Employee> mockEmployees =  Arrays
                .asList(new Employee("andrejd", "1"),
                        new Employee("petere", "2"),
                        new Employee("pavols", "3"));

        RecyclerView currentRecyclerView = (RecyclerView) activity.findViewById(R.id.employees_list);

        mAdapter = (EmployeesRxAdapter) currentRecyclerView.getAdapter();
        mAdapter.setData(mockEmployees);

        currentRecyclerView.measure(0, 0);
        currentRecyclerView.layout(0, 0, 100, 10000);

        assertNotNull(currentRecyclerView.getChildAt(0).performClick());
        assertNotNull(currentRecyclerView.getChildAt(1).performLongClick());
        assertNotNull(currentRecyclerView.getChildAt(2).performLongClick());

        assertEquals(3, currentRecyclerView.getChildCount());

        assertNull(currentRecyclerView.getChildAt(3));

        _rxBus.send(mockEmployees);
    }



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

    public static void assertViewIsVisible(View view){
        assertNotNull(view);
        Assert.assertEquals(view.getVisibility(), View.VISIBLE);
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




}