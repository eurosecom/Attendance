package com.eusecom.attendance;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;

import com.eusecom.attendance.dagger.components.AllEmpsAbsComponent;
import com.eusecom.attendance.dagger.components.ApplicationComponent;
import com.eusecom.attendance.dagger.components.DaggerAllEmpsAbsComponent;
import com.eusecom.attendance.dagger.components.DaggerApplicationComponent;
import com.eusecom.attendance.dagger.components.DaggerFirebaseDependentComponent;
import com.eusecom.attendance.dagger.components.DaggerGitHubComponent;
import com.eusecom.attendance.dagger.components.DaggerNetComponent;
import com.eusecom.attendance.dagger.components.DgFirebaseSubComponent;
import com.eusecom.attendance.dagger.components.FirebaseDependentComponent;
import com.eusecom.attendance.dagger.components.GitHubComponent;
import com.eusecom.attendance.dagger.components.NetComponent;
import com.eusecom.attendance.dagger.modules.AllEmpsAbsModule;
import com.eusecom.attendance.dagger.modules.ApplicationModule;
import com.eusecom.attendance.dagger.modules.DgFirebaseSubModule;
import com.eusecom.attendance.dagger.modules.FirebaseDependentModule;
import com.eusecom.attendance.dagger.modules.GitHubModule;
import com.eusecom.attendance.dagger.modules.NetModule;
import com.eusecom.attendance.mvvmdatamodel.AllEmpsAbsDataModel;
import com.eusecom.attendance.mvvmdatamodel.AllEmpsAbsIDataModel;
import com.eusecom.attendance.mvvmdatamodel.CompaniesDataModel;
import com.eusecom.attendance.mvvmdatamodel.CompaniesIDataModel;
import com.eusecom.attendance.mvvmdatamodel.DgAllEmpsAbsDataModel;
import com.eusecom.attendance.mvvmdatamodel.DgAllEmpsAbsIDataModel;
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
    private AllEmpsAbsIDataModel mAllEmpsAbsIDataModel;
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

        mFirebaseDependentComponent = DaggerFirebaseDependentComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .firebaseDependentModule(new FirebaseDependentModule("https://api.github.com", "https://api.myserver.com"))
                .build();

        mAllEmpsAbsComponent = DaggerAllEmpsAbsComponent.builder()
                .firebaseDependentComponent(mFirebaseDependentComponent)
                .allEmpsAbsModule(new AllEmpsAbsModule())
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
    public AllEmpsAbsIDataModel getAllEmpsAbsIDataModel() {
        mAllEmpsAbsIDataModel = new AllEmpsAbsDataModel(getDatabaseFirebaseReference(), getRealm());
        return mAllEmpsAbsIDataModel;
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

    //we use only for classic create mvvmviewmodel without dagger2
    @NonNull
    public AllEmpsAbsMvvmViewModel getAllEmpsAbsMvvmViewModel() {
        return new AllEmpsAbsMvvmViewModel(getAllEmpsAbsIDataModel(), getSchedulerProvider());
    }

    //dagger2 get components created in onCreate method

    private NetComponent mNetComponent;
    public NetComponent getNetComponent() {
        return mNetComponent;
    }

    private GitHubComponent mGitHubComponent;
    public GitHubComponent getGitHubComponent() {
        return mGitHubComponent;
    }

    private ApplicationComponent mApplicationComponent;
    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    private FirebaseDependentComponent mFirebaseDependentComponent;
    public FirebaseDependentComponent getFirebaseDependentComponent() {
        return mFirebaseDependentComponent;
    }

    private AllEmpsAbsComponent mAllEmpsAbsComponent;
    public AllEmpsAbsComponent getAllEmpsAbsComponent() {
        return mAllEmpsAbsComponent;
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



}
