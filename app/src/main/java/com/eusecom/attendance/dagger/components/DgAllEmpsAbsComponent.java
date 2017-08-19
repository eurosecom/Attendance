package com.eusecom.attendance.dagger.components;

import com.eusecom.attendance.DgAllEmpsAbsListFragment;
import com.eusecom.attendance.DgAllEmpsAbsMvvmActivity;
import com.eusecom.attendance.DgAllEmpsAbsMvvmViewModel;
import com.eusecom.attendance.dagger.modules.DgAllEmpsAbsModule;
import com.eusecom.attendance.dagger.scopes.FirebaseScope;

import dagger.Component;

@FirebaseScope
@Component(dependencies = FirebaseDependentComponent.class, modules = DgAllEmpsAbsModule.class)
public interface DgAllEmpsAbsComponent {

    DgAllEmpsAbsMvvmViewModel dgAllEmpsAbsMvvmViewModel();

    void inject(DgAllEmpsAbsMvvmActivity activity);
    void inject(DgAllEmpsAbsListFragment frg);

}
