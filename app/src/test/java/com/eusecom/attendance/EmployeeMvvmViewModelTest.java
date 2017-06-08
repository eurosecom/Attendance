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
import com.eusecom.attendance.mvvmdatamodel.EmployeeIDataModel;
import com.eusecom.attendance.mvvmmodel.Language;
import com.eusecom.attendance.mvvmschedulers.ImmediateSchedulerProvider;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.doReturn;
import static com.eusecom.attendance.mvvmmodel.Language.LanguageCode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EmployeeMvvmViewModelTest {

    @Mock
    private EmployeeIDataModel mDataModel;

    private EmployeeMvvmViewModel mMainViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mMainViewModel = new EmployeeMvvmViewModel(mDataModel, new ImmediateSchedulerProvider());

    }

    @After
    public void tearDown() throws Exception {

    }

    //recyclerviewtests
    @Test
    public void testRecyclerview_getObservableKeyEditedEmployee() {

        Employee editedEmployee =  new Employee( "eurosecom3", "eurosecom3@gmail.com", "0", "12345678", "0");

        String mockKeyf =  "K6u6ay4ghKbXRh7ZJTAEBoKLazm3";
        Observable<String> mockObservable = Observable.just(mockKeyf);
        doReturn(mockObservable).when(mDataModel).getObservableKeyFBeditUser(editedEmployee);

        mMainViewModel.saveEditEmloyee(editedEmployee, "eurosecom3", "3", "12345678", "99", "0");

        TestSubscriber<String> testSubscriber = new TestSubscriber<>();

        mMainViewModel.getObservableKeyEditedEmployee().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        List<String> liststring = testSubscriber.getOnNextEvents();
        String obserdedkeyf = liststring.get(0).toString();
        System.out.println("editedkeyf " + liststring.get(0).toString());
        junit.framework.Assert.assertEquals(obserdedkeyf, mockKeyf);
    }

    @Test
    public void testRecyclerview_getObservableFBusersEmployee() {

        List<Employee> mockEmployees =  Arrays
                .asList(new Employee("andrejd", "1"),
                        new Employee("petere", "2"),
                        new Employee("pavols", "3"));

        Observable<List<Employee>> mockObservable = Observable.just(mockEmployees);
        doReturn(mockObservable).when(mDataModel).getObservableFBusersEmployee();

        TestSubscriber<List<Employee>> testSubscriber = new TestSubscriber<>();

        mMainViewModel.getObservableFBusersEmployee().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertUnsubscribed();
        testSubscriber.assertTerminalEvent();
        String threadname = testSubscriber.getLastSeenThread().getName();
        System.out.println("threadname " + threadname);

        List<List<Employee>> listlistresult = testSubscriber.getOnNextEvents();
        List<Employee> listresult = listlistresult.get(0);
        System.out.println("listresult0 " + listresult.get(0).getUsername());
        System.out.println("listresult1 " + listresult.get(1).getUsername());

        assertEquals(mockEmployees.get(0).getUsername(), listresult.get(0).getUsername());

        assertThat(listresult, hasSize(3));

    }

    //spinner tests
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

