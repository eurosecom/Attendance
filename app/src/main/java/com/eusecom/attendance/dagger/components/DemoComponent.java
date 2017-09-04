package com.eusecom.attendance.dagger.components;

import com.eusecom.attendance.DgAeaActivity;
import com.eusecom.attendance.DgAeaCompListFragment;
import com.eusecom.attendance.DgAeaListFragment;
import com.eusecom.attendance.MapActivity;
import com.eusecom.attendance.dagger.modules.ApplicationModule;
import com.eusecom.attendance.dagger.modules.ClockModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules={ApplicationModule.class, ClockModule.class})
public interface DemoComponent {
  void inject(DgAeaActivity dgaeaActivity);
  void inject(DgAeaListFragment dgaeafragment);
  void inject(DgAeaCompListFragment dgaeacompfragment);
  void inject(MapActivity mapActivity);
}