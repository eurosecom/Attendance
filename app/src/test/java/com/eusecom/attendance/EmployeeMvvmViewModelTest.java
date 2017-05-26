package com.eusecom.attendance;

import android.util.Log;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;
import com.eusecom.attendance.mvvmdatamodel.IDataModel;
import com.eusecom.attendance.mvvmmodel.Language;
import com.eusecom.attendance.mvvmschedulers.ImmediateSchedulerProvider;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.doReturn;
import static com.eusecom.attendance.mvvmmodel.Language.LanguageCode;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EmployeeMvvmViewModelTest {

    @Mock
    private IDataModel mDataModel;

    private EmployeeMvvmViewModel mMainViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mMainViewModel = new EmployeeMvvmViewModel(mDataModel, new ImmediateSchedulerProvider());

    }

    @After
    public void tearDown() throws Exception {

    }

    //test works ok
    @Test
    public void testGetSupportedLanguages_emitsCorrectLanguages() {
        Language de = new Language("German", LanguageCode.DE);
        Language en = new Language("English", LanguageCode.EN);
        List<Language> languages = Arrays.asList(de, en);

        Observable<List<Language>> mockObservable = Observable.just(languages);
        //Observable<Language> mockObservable = Observable.from(languages);

        doReturn(mockObservable).when(mDataModel).getObservableSupportedLanguages();

        Language hr = new Language("Slovakian", LanguageCode.HR);
        List<Language> expectedLanguages = Arrays.asList(de, en, hr);

        //mMainViewModel = new MainViewModel(mDataModel, new ImmediateSchedulerProvider());
        TestSubscriber<List<Language>> testSubscriber = new TestSubscriber<>();

        mMainViewModel.getObservableSupportedLanguages().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertUnsubscribed();
        testSubscriber.assertTerminalEvent();
        String threadname = testSubscriber.getLastSeenThread().getName();
        System.out.println("threadname " + threadname);

        //testSubscriber.getOnNextEvents( give List<T>
        List<List<Language>> listlistresult = testSubscriber.getOnNextEvents();
        System.out.println("listlistresult " + listlistresult.get(0).toString());

        List<Language> listresult = listlistresult.get(0);
        System.out.println("listresult0 " + listresult.get(0).getName());
        System.out.println("listresult1 " + listresult.get(1).getName());

        //testSubscriber.assertReceivedOnNext(expectedLanguages); do not works result of assertReceivedOnNext is List<List<Language>>

        assertEquals(expectedLanguages.get(0).getName(), listresult.get(0).getName());

        //hamcrest matchers
        //http://www.vogella.com/tutorials/Hamcrest/article.html
        assertThat(languages, hasSize(2));
    }


    //test works ok
    @Test
    public void testGetGreeting_doesNotEmit_whenNoLanguageSet() {
        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        mMainViewModel.getObservableGreeting().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertNoValues();
    }

    //test works ok
    @Test
    public void testGetGreeting_emitsCorrectGreeting_whenLanguageSet() {

        String mockEnGreeting = "Hello!";
        String expectedEnGreeting = "Hello!";
        Language en = new Language("English", LanguageCode.EN);
        Language de = new Language("German", LanguageCode.DE);


        Observable<String> mockObservable = Observable.just(mockEnGreeting);
        doReturn(mockObservable).when(mDataModel).getObservableGreetingByLanguageCode(LanguageCode.EN);

        //mMainViewModel.emitlanguageSelected(de); wrong
        mMainViewModel.emitlanguageSelected(en);

        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        mMainViewModel.getObservableGreeting().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(expectedEnGreeting);

        //testSubscriber.getOnNextEvents( give List<T>
        List<String> liststring = testSubscriber.getOnNextEvents();
        System.out.println("liststring " + liststring.get(0).toString());

        String result = liststring.get(0);
        System.out.println("liststring0 " + result);

        //hamcrest matchers
        //http://www.vogella.com/tutorials/Hamcrest/article.html
        assertEquals(expectedEnGreeting, result);

    }

    //test works ok
    @Test
    public void test_getValue() {

        int xxx=mMainViewModel.getValue(77);

        assertEquals(23, xxx);
    }

}

