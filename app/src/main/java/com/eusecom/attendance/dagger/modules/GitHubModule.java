package com.eusecom.attendance.dagger.modules;

import com.eusecom.attendance.dagger.scopes.UserScope;
import com.eusecom.attendance.retrofit.GitHubApiInterface;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;


@Module
public class GitHubModule {

    @Provides
    @UserScope
    public GitHubApiInterface providesGitHubInterface(Retrofit retrofit) {
        return retrofit.create(GitHubApiInterface.class);
    }
}
