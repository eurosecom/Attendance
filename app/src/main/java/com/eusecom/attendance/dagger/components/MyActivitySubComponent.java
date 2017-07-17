package com.eusecom.attendance.dagger.components;

import android.content.SharedPreferences;
import android.widget.ArrayAdapter;

import com.eusecom.attendance.AllEmpsAbsMvvmActivity;
import com.eusecom.attendance.DemoDaggerSubActivity;
import com.eusecom.attendance.dagger.modules.MyActivityModule;
import com.eusecom.attendance.dagger.scopes.MyActivityScope;
import javax.inject.Named;
import dagger.Subcomponent;

@MyActivityScope
@Subcomponent(modules={ MyActivityModule.class })
public interface MyActivitySubComponent {

    void inject(DemoDaggerSubActivity activity);

    @Named("my_list") ArrayAdapter myListAdapter();
    SharedPreferences sharedPreferences();

    @Subcomponent.Builder
    interface Builder extends SubcomponentBuilder<MyActivitySubComponent> {
        Builder activityModule(MyActivityModule module);
    }

}
