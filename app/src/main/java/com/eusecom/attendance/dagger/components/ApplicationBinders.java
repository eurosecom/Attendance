package com.eusecom.attendance.dagger.components;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * For new Dagger2 Subcomponent Builder
 */

@Module(subcomponents={ MyActivitySubComponent.class })
public abstract class ApplicationBinders {
    // Provide the builder to be included in a mapping used for creating the builders.
    @Binds
    @IntoMap
    @SubcomponentKey(MyActivitySubComponent.Builder.class)
    public abstract SubcomponentBuilder myActivity(MyActivitySubComponent.Builder impl);
}