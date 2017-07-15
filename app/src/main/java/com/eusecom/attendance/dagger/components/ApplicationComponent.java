package com.eusecom.attendance.dagger.components;


import com.eusecom.attendance.dagger.modules.ApplicationModule;
import java.util.Map;
import javax.inject.Provider;
import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by Valentine on 4/20/2016.
 */
@Singleton
@Component( modules = { ApplicationModule.class, ApplicationBinders.class })
public interface ApplicationComponent {

    //do not inject activity for subcomponent
    //void inject(DemoDaggerOtherActivity activity);


    // Returns a map with all the builders mapped by their class.
    Map<Class<?>, Provider<SubcomponentBuilder>> subcomponentBuilders();



}
