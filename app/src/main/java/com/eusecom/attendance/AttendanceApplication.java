package com.eusecom.attendance;

import android.app.Application;
import android.support.annotation.NonNull;

import com.eusecom.attendance.mvvmdatamodel.DataModel;
import com.eusecom.attendance.mvvmdatamodel.IDataModel;
import com.eusecom.attendance.mvvmschedulers.ISchedulerProvider;
import com.eusecom.attendance.mvvmschedulers.SchedulerProvider;


public class AttendanceApplication extends Application {

    @NonNull
    private final IDataModel mDataModel;

    public AttendanceApplication() {
        mDataModel = new DataModel();
    }

    @NonNull
    public IDataModel getDataModel() {
        return mDataModel;
    }


    @NonNull
    public ISchedulerProvider getSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }

}
