package com.eusecom.attendance;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.rxbus.RxBus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.flowables.ConnectableFlowable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

//by https://chelseatroy.com/2015/09/27/android-examples-a-test-driven-recyclerview/

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EmployeesRxAdapterRoboelectricTest {

    private RxBus _rxBus;
    private CompositeDisposable _disposables;

    private EmployeesRxAdapter mAdapter;
    private EmployeesRxViewHolder holder;

    private View mockView;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        _rxBus = getRxBusSingleton();
        _disposables = new CompositeDisposable();

        ConnectableFlowable<Object> tapEventEmitter = _rxBus.asFlowable().publish();
        //rewrite _disposables from retrolambda to classic annotation bebause of testcompiler has problem with lambda expression

        _disposables
                .add(tapEventEmitter.subscribe(event -> {

                    if (event instanceof EmployeeMvvmActivity.TapEvent) {
                        System.out.println("rxBus longClickOnItemOfAdapter");
                    }
                    if (event instanceof Employee) {
                        String keys = ((Employee) event).getUsatw();
                        //Log.d("In FRGM longClick", keys);

                        Employee model= (Employee) event;

                        System.out.println("username " + model.username);
                        assertThat(model.username).isEqualTo("andrejd");

                    }

                }));


        _disposables.add(tapEventEmitter.connect());

        mAdapter = new EmployeesRxAdapter(Collections.<Employee>emptyList(), _rxBus);

        mockView = mock(View.class);

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

    @Test
    public void adapter_itemCount() {
        List<Employee> mockEmployees =  Arrays
                .asList(new Employee("andrejd", "1"),
                        new Employee("petere", "2"),
                        new Employee("pavols", "3"));

        mAdapter.setData(mockEmployees);

        assertThat(mAdapter.getItemCount()).isEqualTo(3);
    }

    @Test
    public void adapter_getItemAtPosition() {
        List<Employee> mockEmployees =  Arrays
                .asList(new Employee("andrejd", "1"),
                        new Employee("petere", "2"),
                        new Employee("pavols", "3"));

        mAdapter.setData(mockEmployees);
        assertThat(mAdapter.getItemAtPosition(0)).isEqualTo(mockEmployees.get(0));
        assertThat(mAdapter.getItemAtPosition(1)).isEqualTo(mockEmployees.get(1));
    }

    @Test
    public void onBindViewHolder_setsTextAndClickEventForEmployeeView() {

        List<Employee> mockEmployees =  Arrays
                .asList(new Employee("andrejd", "1"),
                        new Employee("petere", "2"),
                        new Employee("pavols", "3"));

        mAdapter.setData(mockEmployees);
        //adapter.setContext(mockFragment);
        LayoutInflater inflater = (LayoutInflater) RuntimeEnvironment.application.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //We have a layout especially for the items in our recycler view. We will see it in a moment.
        View listItemView = inflater.inflate(R.layout.employee_item, null, false);
        holder = new EmployeesRxViewHolder(listItemView);
        //click on item 0
        mAdapter.onBindViewHolder(holder, 0);
        assertThat(holder.employee_name.getText().toString()).isEqualTo("andrejd");

        //send to rxbus
        holder.itemView.performLongClick();

    }



}