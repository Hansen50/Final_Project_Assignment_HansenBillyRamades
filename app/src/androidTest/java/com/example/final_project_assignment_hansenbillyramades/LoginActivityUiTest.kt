//import android.content.Intent
//import android.widget.Toast
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.core.view.isVisible
//import androidx.lifecycle.LifecycleOwner
//import androidx.lifecycle.Observer
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.example.final_project_assignment_hansenbillyramades.data.source.local.PreferenceDataStore
//import com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity.LoginActivity
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.nhaarman.mockitokotlin2.*
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.TestCoroutineDispatcher
//import kotlinx.coroutines.test.runBlockingTest
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.Mock
//import org.mockito.Mockito
//import org.mockito.MockitoAnnotations
//import org.robolectric.Robolectric
//import org.robolectric.annotation.Config
//
//@RunWith(AndroidJUnit4::class)
//@ExperimentalCoroutinesApi
//class LoginActivityUiTest {
//
//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    @Mock
//    lateinit var firebaseAuth: FirebaseAuth
//
//    @Mock
//    lateinit var preferenceDataStore: PreferenceDataStore
//
//    @Mock
//    lateinit var firebaseUser: FirebaseUser
//
//    private lateinit var activity: LoginActivity
//
//    private val testDispatcher = TestCoroutineDispatcher()
//
//    @Before
//    fun setUp() {
//        MockitoAnnotations.openMocks(this)
//        // Mock FirebaseAuth and PreferenceDataStore interactions
//        Mockito.`when`(firebaseAuth.currentUser).thenReturn(firebaseUser)
//        Mockito.`when`(preferenceDataStore.getUserOnboarded()).thenReturn(true)
//
//        // Initialize the activity
//        activity = Robolectric.buildActivity(LoginActivity::class.java)
//            .create()
//            .start()
//            .resume()
//            .get()
//
//        // Inject mocks (you may need Dagger or Hilt if your project uses DI)
//        activity.auth = firebaseAuth
//        activity.preferenceDataStore = preferenceDataStore
//    }
//
//    @Test
//    fun `test signIn button click triggers signIn logic`() {
//        // Simulate button click
//        activity.binding.btnSignInLayout.performClick()
//
//        // Assert that the loading state is visible, and other elements are not
//        assert(activity.binding.loadingLayout.isVisible)
//        assert(!activity.binding.googleIcon.isVisible)
//        assert(!activity.binding.btnText.isVisible)
//    }
//
//    @Test
//    fun `test successful login redirects to MainActivity`() = runBlockingTest {
//        // Mock a successful login response
//        val mockIdToken = "mockIdToken"
//        val mockCredential = mock<FirebaseUser>()
//        val mockCredentialResponse = mock<FirebaseAuth.AuthResult>()
//        whenever(firebaseAuth.signInWithCredential(any())).thenReturn(mockCredentialResponse)
//
//        // Trigger the login success scenario
//        activity.firebaseAuthWithGoogle(mockIdToken)
//
//        // Assert: Verify the preference data store is updated and the correct activity is started
//        verify(preferenceDataStore).setUserLoggedIn(true)
//
//        // Check that the correct intent is being triggered (MainActivity or OnBoardActivity)
//        val expectedIntent = Intent(activity, MainActivity::class.java)
//        verify(activity).startActivity(expectedIntent)
//
//        // Check that the Toast message is shown
//        val toastCaptor = argumentCaptor<CharSequence>()
//        verify(activity).showToast(toastCaptor.capture())
//        assert(toastCaptor.value == "Login Success")
//    }
//
//    @Test
//    fun `test failed login shows error toast`() {
//        // Mock a failed login scenario
//        whenever(firebaseAuth.signInWithCredential(any())).thenThrow(RuntimeException("Login failed"))
//
//        // Trigger the login failure scenario
//        activity.firebaseAuthWithGoogle(null)
//
//        // Assert: Verify that a Toast message is shown indicating login failure
//        val toastCaptor = argumentCaptor<CharSequence>()
//        verify(activity).showToast(toastCaptor.capture())
//        assert(toastCaptor.value == "Login Failed, Please check your internet connection")
//    }
//
//    @Test
//    fun `test resetButtonState resets button visibility`() {
//        // Trigger the reset button state method
//        activity.resetButtonState()
//
//        // Assert that the buttons and loading layout are visible or invisible as expected
//        assert(activity.binding.loadingLayout.isVisible == false)
//        assert(activity.binding.googleIcon.isVisible == true)
//        assert(activity.binding.btnText.isVisible == true)
//    }
//}
