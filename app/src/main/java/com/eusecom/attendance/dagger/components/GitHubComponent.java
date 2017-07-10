package com.eusecom.attendance.dagger.components;

import com.eusecom.attendance.DemoDaggerActivity;
import com.eusecom.attendance.dagger.modules.GitHubModule;
import com.eusecom.attendance.dagger.scopes.UserScope;
import dagger.Component;

@UserScope
@Component(dependencies = NetComponent.class, modules = GitHubModule.class)
public interface GitHubComponent {
    void inject(DemoDaggerActivity activity);
}
