package com.eusecom.attendance;

import android.app.Application;
import android.support.annotation.NonNull;

import com.eusecom.attendance.dagger.components.ApplicationComponent;
import com.eusecom.attendance.dagger.components.DaggerApplicationComponent;
import com.eusecom.attendance.dagger.components.DaggerGitHubComponent;
import com.eusecom.attendance.dagger.components.DaggerNetComponent;
import com.eusecom.attendance.dagger.components.GitHubComponent;
import com.eusecom.attendance.dagger.components.NetComponent;
import com.eusecom.attendance.dagger.modules.ApplicationModule;
import com.eusecom.attendance.dagger.modules.GitHubModule;
import com.eusecom.attendance.dagger.modules.NetModule;
import com.eusecom.attendance.mvvmdatamodel.AllEmpsAbsDataModel;
import com.eusecom.attendance.mvvmdatamodel.AllEmpsAbsIDataModel;
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

    @NonNull
    private final AllEmpsAbsIDataModel mAllEmpsAbsDataModel;

    @Override public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...


        //dagger demo retrofit
        // specify the full namespace of the component
        // Dagger_xxxx (where xxxx = component name)

        mNetComponent = DaggerNetComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .netModule(new NetModule("https://api.github.com", "https://api.myserver.com"))
                .build();

        mGitHubComponent = DaggerGitHubComponent.builder()
                .netComponent(mNetComponent)
                .gitHubModule(new GitHubModule())
                .build();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();


    }


    private static AttendanceApplication instance;

    public AttendanceApplication() {

        mCompaniesDataModel = new CompaniesDataModel();
        mEmployeeDataModel = new EmployeeDataModel();
        mAllEmpsAbsDataModel = new AllEmpsAbsDataModel();
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
    public AllEmpsAbsIDataModel getAllEmpsAbsDataModel() {
        return mAllEmpsAbsDataModel;
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

    @NonNull
    public AllEmpsAbsMvvmViewModel getAllEmpsAbsMvvmViewModel() {
        return new AllEmpsAbsMvvmViewModel(getAllEmpsAbsDataModel(), getSchedulerProvider());
    }


    //dagger2 demo retrofit

    private NetComponent mNetComponent;
    private GitHubComponent mGitHubComponent;
    private ApplicationComponent mApplicationComponent;

    public NetComponent getNetComponent() {
        return mNetComponent;
    }

    public GitHubComponent getGitHubComponent() {
        return mGitHubComponent;
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }


}
