package com.eusecom.attendance.dagger.modules;

import com.eusecom.attendance.dagger.scopes.FirebaseScope;
import com.eusecom.attendance.retrofit.GitHubApiInterface;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;


@Module
public class DgAllEmpsAbsModule {


    @Provides
    @FirebaseScope
    public GitHubApiInterface providesGitHubInterface(Retrofit retrofit) {
        return retrofit.create(GitHubApiInterface.class);
    }


}
