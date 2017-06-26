package com.eusecom.attendance;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import rx.Observable;
import rx.observers.TestSubscriber;
import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.mvvmdatamodel.CompaniesIDataModel;
import com.eusecom.attendance.mvvmdatamodel.EmployeeIDataModel;
import com.eusecom.attendance.mvvmmodel.Language;
import com.eusecom.attendance.mvvmschedulers.ImmediateSchedulerProvider;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.doReturn;
import static com.eusecom.attendance.mvvmmodel.Language.LanguageCode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CompaniesMvvmViewModelTest {

    @Mock
    private CompaniesIDataModel mDataModel;

    private CompaniesMvvmViewModel mMainViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mMainViewModel = new CompaniesMvvmViewModel(mDataModel, new ImmediateSchedulerProvider());

    }

    @After
    public void tearDown() throws Exception {

    }

    //recyclerviewtests

    @Test
    public void test_getObservableFBcompanies() {


    }

    @Test
    public void test_getObservableKeyNewCompany() {


    }

    @Test
    public void test_getObservableKeyEditedEmployee() {


    }



}

