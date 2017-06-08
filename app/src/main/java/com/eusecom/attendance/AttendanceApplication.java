package com.eusecom.attendance;

import android.app.Application;
import android.support.annotation.NonNull;

import com.eusecom.attendance.mvvmdatamodel.CompaniesDataModel;
import com.eusecom.attendance.mvvmdatamodel.CompaniesIDataModel;
import com.eusecom.attendance.mvvmdatamodel.DataModel;
import com.eusecom.attendance.mvvmdatamodel.IDataModel;
import com.eusecom.attendance.mvvmschedulers.ISchedulerProvider;
import com.eusecom.attendance.mvvmschedulers.SchedulerProvider;
import com.eusecom.attendance.rxbus.RxBus;


public class AttendanceApplication extends Application {

    public RxBus _rxBus;
    static AttendanceApplication myAppInstance;

    @NonNull
    private final IDataModel mDataModel;

    @NonNull
    private final CompaniesIDataModel mCompaniesDataModel;

    public AttendanceApplication() {

        myAppInstance = this;
        mDataModel = new DataModel();
        mCompaniesDataModel = new CompaniesDataModel();
    }

    public static AttendanceApplication getInstance() {
        return myAppInstance;
    }

    public RxBus getRxBusSingleton() {
        if (_rxBus == null) {
            _rxBus = new RxBus();
        }

        return _rxBus;
    }

    @NonNull
    public IDataModel getDataModel() {
        return mDataModel;
    }

    @NonNull
    public CompaniesIDataModel getCompaniesDataModel() {
        return mCompaniesDataModel;
    }

    @NonNull
    public ISchedulerProvider getSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }

    @NonNull
    public MainViewModel getViewModel() {
        return new MainViewModel(getDataModel(), getSchedulerProvider());
    }

    @NonNull
    public EmployeeMvvmViewModel getEmployeeMvvmViewModel() {
        return new EmployeeMvvmViewModel(getDataModel(), getSchedulerProvider());
    }

    @NonNull
    public CompaniesMvvmViewModel getCompaniesMvvmViewModel() {
        return new CompaniesMvvmViewModel(getCompaniesDataModel(), getSchedulerProvider());
    }

}
