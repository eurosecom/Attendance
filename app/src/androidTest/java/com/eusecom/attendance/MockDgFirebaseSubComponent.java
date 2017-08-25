package com.eusecom.attendance;


import com.eusecom.attendance.dagger.components.DgFirebaseSubComponent;
import com.eusecom.attendance.dagger.scopes.DgFirebaseScope;
import dagger.Subcomponent;

@DgFirebaseScope
@Subcomponent(modules={ MockDgFirebaseSubModule.class })

public interface MockDgFirebaseSubComponent extends DgFirebaseSubComponent {

    void inject(DgOver2ModulAllEmpsAbsMvvmActivityEspressoTest activityTest);
}





    

