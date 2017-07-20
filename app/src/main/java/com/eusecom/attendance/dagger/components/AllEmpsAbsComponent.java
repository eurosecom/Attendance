package com.eusecom.attendance.dagger.components;

import com.eusecom.attendance.AllEmpsAbsListFragment;
import com.eusecom.attendance.AllEmpsAbsMvvmActivity;
import com.eusecom.attendance.dagger.modules.AllEmpsAbsModule;
import com.eusecom.attendance.dagger.scopes.FirebaseScope;
import dagger.Component;

@FirebaseScope
@Component(dependencies = FirebaseDependentComponent.class, modules = AllEmpsAbsModule.class)
public interface AllEmpsAbsComponent {
    void inject(AllEmpsAbsMvvmActivity activity);
    void inject(AllEmpsAbsListFragment frg);
}
