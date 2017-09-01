package com.eusecom.attendance;

import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import com.eusecom.attendance.dagger.components.ApplicationComponent;
import com.eusecom.attendance.dagger.components.DaggerApplicationComponent;
import com.eusecom.attendance.dagger.components.DaggerDemoComponent;
import com.eusecom.attendance.dagger.components.DemoComponent;
import com.eusecom.attendance.dagger.components.DgFirebaseSubComponent;
import com.eusecom.attendance.dagger.modules.ApplicationModule;
import com.eusecom.attendance.dagger.modules.DgFirebaseSubModule;
import com.eusecom.attendance.mvvmdatamodel.CompaniesDataModel;
import com.eusecom.attendance.mvvmdatamodel.CompaniesIDataModel;
import com.eusecom.attendance.mvvmdatamodel.EmployeeDataModel;
import com.eusecom.attendance.mvvmdatamodel.EmployeeIDataModel;
import com.eusecom.attendance.mvvmschedulers.ISchedulerProvider;
import com.eusecom.attendance.mvvmschedulers.SchedulerProvider;
import com.eusecom.attendance.rxbus.RxBus;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.leakcanary.LeakCanary;
import io.realm.RealmConfiguration;
import io.realm.Realm;

public class AttendanceApplication extends MultiDexApplication {

    public RxBus _rxBus;

    @NonNull
    private final CompaniesIDataModel mCompaniesDataModel;

    @NonNull
    private final EmployeeIDataModel mEmployeeDataModel;

    @NonNull
    private DatabaseReference mDatabaseReference;
    private Realm mRealm;

    @Override public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        //dagger demo retrofit
        // specify the full namespace of the component
        // Dagger_xxxx (where xxxx = component name)


        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();



        mDgFirebaseSubComponent = createDgFirebaseSubComponent();


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
    public Realm getRealm() {
        return Realm.getDefaultInstance();
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
    public DatabaseReference getDatabaseFirebaseReference() {
        return mDatabaseReference;
    }


    @NonNull
    public EmployeeMvvmViewModel getEmployeeMvvmViewModel() {
        return new EmployeeMvvmViewModel(getEmployeeDataModel(), getSchedulerProvider());
    }

    @NonNull
    public CompaniesMvvmViewModel getCompaniesMvvmViewModel() {
        return new CompaniesMvvmViewModel(getCompaniesDataModel(), getSchedulerProvider());
    }


    //dagger2 get components created in onCreate method
    private ApplicationComponent mApplicationComponent;
    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public DgFirebaseSubComponent mDgFirebaseSubComponent;
    public DgFirebaseSubComponent getDgFirebaseSubComponent() {
        return mDgFirebaseSubComponent;
    }
    public DgFirebaseSubComponent createDgFirebaseSubComponent(){

        DgFirebaseSubComponent.Builder builder = (DgFirebaseSubComponent.Builder)
            this.getApplicationComponent()
                    .subcomponentBuilders()
                    .get(DgFirebaseSubComponent.Builder.class)
                    .get();
        mDgFirebaseSubComponent = builder.activityModule(new DgFirebaseSubModule()).build();
        return mDgFirebaseSubComponent;

    }

    private final DemoComponent dgaeacomponent = createDgAeaComponent();

    protected DemoComponent createDgAeaComponent() {
        return DaggerDemoComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public DemoComponent dgaeacomponent() {
        return dgaeacomponent;
    }



}
