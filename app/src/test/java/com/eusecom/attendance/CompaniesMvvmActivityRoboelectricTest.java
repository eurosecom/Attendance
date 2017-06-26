package com.eusecom.attendance;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.eusecom.attendance.models.Company;
import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.mvvmdatamodel.CompaniesIDataModel;
import com.eusecom.attendance.mvvmschedulers.ImmediateSchedulerProvider;
import com.eusecom.attendance.rxbus.RxBus;
import junit.framework.Assert;

import org.assertj.android.api.Assertions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static android.R.attr.fragment;
import static org.mockito.Mockito.stubVoid;
import static org.robolectric.util.FragmentTestUtil.startFragment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.flowables.ConnectableFlowable;
import rx.Observable;
import rx.functions.Action1;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static com.eusecom.attendance.EmployeeMvvmActivityRoboelectricTest.assertViewIsVisible;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.robolectric.Shadows.shadowOf;


import org.robolectric.shadows.ShadowIntent;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class CompaniesMvvmActivityRoboelectricTest {

    private CompaniesMvvmActivity activity;

    @Mock
    private CompaniesIDataModel mDataModel;

    private CompaniesMvvmViewModel mMainViewModel;

    CompaniesListFragment fragment;

    private RecyclerView mRecycler;

    CompaniesRxAdapter mAdapter;


    private RxBus _rxBus;
    private CompositeDisposable _disposables;

    private LinearLayoutManager mockLayoutManager;


    @Before
    public void setUp() throws Exception {

        //if exist TestAplicationname.java roboelectric work first for firebase init
        MockitoAnnotations.initMocks(this);
        mMainViewModel = new CompaniesMvvmViewModel(mDataModel, new ImmediateSchedulerProvider());

        activity = Robolectric.buildActivity(CompaniesMvvmActivity.class)
                .create()
                .resume()
                .get();

        fragment = new CompaniesListFragment();
        SupportFragmentTestUtil.startVisibleFragment(fragment);


        _rxBus = getRxBusSingleton();
        _disposables = new CompositeDisposable();

        ConnectableFlowable<Object> tapEventEmitter = _rxBus.asFlowable().publish();
        //rewrite _disposables from retrolambda to classic annotation bebause of testcompiler has problem with lambda expression

        _disposables
                .add(tapEventEmitter.subscribe(event -> {

                    if (event instanceof CompaniesListFragment.ClickFobEvent) {

                        System.out.println("rxBus CompaniesActivity fobClick");

                    }
                    if (event instanceof Company) {

                        System.out.println("rxBus CompaniesActivity onItemClick");

                    }

                }));

        _disposables.add(tapEventEmitter.connect());

        mAdapter =  new CompaniesRxAdapter(Collections.<Company>emptyList(), _rxBus);

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
    public void checkActivityNotNullTest() throws Exception {
        assertNotNull(activity);
    }

    @Test
    public void checkFragmentNotNullTest() throws Exception {

        assertNotNull( fragment );
    }


    @Test
    public void checkRecyclerViewNotNullTest() throws Exception {

        RecyclerView mRecyclerView = (RecyclerView) fragment.getView().findViewById(R.id.companies_list);
        assertNotNull( mRecyclerView );

    }


    @Test
    public void testRecyclerview_setDataToAdapter() {

        List<Company> mockCompanies =  Arrays
                .asList(new Company("12345678", "name1", "", "0", "city1"),
                        new Company("12345670", "name2", "", "0", "city2"),
                        new Company("12345671", "name3", "", "0", "city3"));

        mAdapter.setData(mockCompanies);
        Assert.assertEquals(mAdapter.getItemCount(), 3);

    }

    @Test
    public void testRecyclerview_clickToitems() {

        List<Company> mockCompanies =  Arrays
                .asList(new Company("12345678", "name1", "", "0", "city1"),
                        new Company("12345670", "name2", "", "0", "city2"),
                        new Company("12345671", "name3", "", "0", "city3"));

        RecyclerView currentRecyclerView = (RecyclerView) fragment.getView().findViewById(R.id.companies_list);

        mAdapter = (CompaniesRxAdapter) currentRecyclerView.getAdapter();
        mAdapter.setData(mockCompanies);

        currentRecyclerView.measure(0, 0);
        currentRecyclerView.layout(0, 0, 100, 10000);

        assertNotNull(currentRecyclerView.getChildAt(0).performClick());
        assertNotNull(currentRecyclerView.getChildAt(1).performLongClick());
        assertNotNull(currentRecyclerView.getChildAt(2).performLongClick());

        assertEquals(3, currentRecyclerView.getChildCount());

        assertNull(currentRecyclerView.getChildAt(3));

        _rxBus.send(mockCompanies.get(0));
    }

    @Test
    public void testRecyclerview_whatDesign() {

        RecyclerView mRecyclerView = (RecyclerView) fragment.getView().findViewById(R.id.companies_list);
        int leftMargin = ((FrameLayout.LayoutParams) mRecyclerView.getLayoutParams()).leftMargin;
        assertEquals(0, leftMargin);
        int righttMargin = ((FrameLayout.LayoutParams) mRecyclerView.getLayoutParams()).rightMargin;
        assertEquals(0, righttMargin);

    }

    @Test
    public void testOptinsMenuItem_click_items() {

        ShadowActivity shadowActivity = Shadows.shadowOf(activity);

        // Click menu1
        shadowActivity.clickMenuItem(R.id.action_settings);

        // Get intent
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);

        // Make your assertion
        assertEquals(shadowIntent.getIntentClass().getName(), SettingsActivity.class.getName());


        // Click menu2
        shadowActivity.clickMenuItem(R.id.action_logout);

        // Get intent
        Intent startedIntent2 = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent2 = Shadows.shadowOf(startedIntent2);

        // Make your assertion
        assertEquals(shadowIntent2.getIntentClass().getName(), EmailPasswordActivity.class.getName());

    }


    @Test
    public void testOptinsMenuItem_click_fab() {

        assertNotNull(activity.findViewById(R.id.fab));
        activity.findViewById(R.id.fab).performClick();

        _rxBus.send(new CompaniesListFragment.ClickFobEvent());

    }


    @Test
    public void test_getObservableFBcompanies() {

        List<Company> mockCompanies =  Arrays
                .asList(new Company("12345678", "name1", "", "0", "city1"),
                        new Company("12345670", "name2", "", "0", "city2"),
                        new Company("12345671", "name3", "", "0", "city3"));

        Observable<List<Company>> mockObservable = Observable.just(mockCompanies);
        doReturn(mockObservable).when(mDataModel).getObservableFBXcompanies();

        TestSubscriber<List<Company>> testSubscriber = new TestSubscriber<>();

        mMainViewModel.getObservableFBcompanies().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertUnsubscribed();
        testSubscriber.assertTerminalEvent();
        String threadname = testSubscriber.getLastSeenThread().getName();
        System.out.println("threadname " + threadname);

        List<List<Company>> listlistresult = testSubscriber.getOnNextEvents();
        List<Company> listresult = listlistresult.get(0);
        System.out.println("listresult0 " + listresult.get(0).getCmname());
        System.out.println("listresult1 " + listresult.get(1).getCmname());

        Assert.assertEquals(mockCompanies.get(0).getCmname(), listresult.get(0).getCmname());

        assertThat(listresult, hasSize(3));

    }

    @Test
    public void test_getObservableKeyNewCompany() {

        Company editedCompany =  new Company("12345678", "name1", "", "0", "city1");

        String mockKeyf =  "12345678";
        Observable<String> mockObservable = Observable.just(mockKeyf);
        doReturn(mockObservable).when(mDataModel).getObservableKeyFBnewCompany(editedCompany);

        mMainViewModel.saveNewCompany(editedCompany);

        TestSubscriber<String> testSubscriber = new TestSubscriber<>();

        mMainViewModel.getObservableKeyNewCompany().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        List<String> liststring = testSubscriber.getOnNextEvents();
        String obserdedkeyf = liststring.get(0).toString();
        System.out.println("editedkeyf " + liststring.get(0).toString());
        Assert.assertEquals(obserdedkeyf, mockKeyf);

    }




}