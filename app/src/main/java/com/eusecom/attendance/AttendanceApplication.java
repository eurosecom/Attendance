package com.eusecom.attendance;

import android.app.Application;
import android.support.annotation.NonNull;

import com.eusecom.attendance.mvvmdatamodel.CompaniesDataModel;
import com.eusecom.attendance.mvvmdatamodel.CompaniesIDataModel;
import com.eusecom.attendance.mvvmdatamodel.EmployeeDataModel;
import com.eusecom.attendance.mvvmdatamodel.EmployeeIDataModel;
import com.eusecom.attendance.mvvmschedulers.ISchedulerProvider;
import com.eusecom.attendance.mvvmschedulers.SchedulerProvider;
import com.eusecom.attendance.rxbus.RxBus;
import com.squareup.leakcanary.LeakCanary;


public class AttendanceApplication extends Application {

    public RxBus _rxBus;
    static AttendanceApplication myAppInstance;


    @NonNull
    private final CompaniesIDataModel mCompaniesDataModel;

    @NonNull
    private final EmployeeIDataModel mEmployeeDataModel;

    @Override public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }


    private static AttendanceApplication instance;

    public AttendanceApplication() {

        mCompaniesDataModel = new CompaniesDataModel();
        mEmployeeDataModel = new EmployeeDataModel();
    }

    public static AttendanceApplication getInstance() {
        if (instance== null) {
            instance = new AttendanceApplication();
            }
        // Return the instance
        return instance;

    }

    public RxBus getRxBusSingleton() {
        if (_rxBus == null) {
            _rxBus = new RxBus();
        }

        return _rxBus;
    }


    @NonNull
    public CompaniesIDataModel getCompaniesDataModel() {
        return mCompaniesDataModel;
    }

    @NonNull
    public EmployeeIDataModel getEmployeeDataModel() {
        return mEmployeeDataModel;
    }

    @NonNull
    public ISchedulerProvider getSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }


    @NonNull
    public EmployeeMvvmViewModel getEmployeeMvvmViewModel() {
        return new EmployeeMvvmViewModel(getEmployeeDataModel(), getSchedulerProvider());
    }

    @NonNull
    public CompaniesMvvmViewModel getCompaniesMvvmViewModel() {
        return new CompaniesMvvmViewModel(getCompaniesDataModel(), getSchedulerProvider());
    }


}
