package mt.edu.um.getalift;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

/** Created by Thessalène JEAN-LOUIS**/
public class LoginActivityTest {

    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    private String username_typed="dada";
    private String correct_password ="dada";
    private String wrong_password = "dada123";

    @Test
    public void  login_success(){
        Log.e("@Test","Performing login success test");
        //Enter good username and password
        onView(withId(R.id.edt_username)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_username)).perform(ViewActions.clearText());
        onView(withId(R.id.edt_username)).perform(ViewActions.typeText(username_typed));

        onView(withId(R.id.edt_password)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_password)).perform(ViewActions.clearText());
        onView(withId(R.id.edt_password)).perform(ViewActions.typeText(correct_password));

        //Click on "connection" button
        onView(withId(R.id.btn_connect))
                .perform(ViewActions.click());
        //Redirection to the homeMapActivity : ok
    }

    @Test
    public void login_failure(){
        Log.e("@Test","Performing login failure test");
        //Enter good username and password
        onView(withId(R.id.edt_username)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_username)).perform(ViewActions.clearText());
        onView(withId(R.id.edt_username)).perform(ViewActions.typeText(username_typed));

        onView(withId(R.id.edt_password)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_password)).perform(ViewActions.clearText());
        onView(withId(R.id.edt_password)).perform(ViewActions.typeText(wrong_password));

        //Click on "connection" button
        onView(withId(R.id.btn_connect))
                .perform(ViewActions.click());

        onView(withText("Auth failed. Wrong credentials.")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        //Not Redirection to the homeMapActivity : ok
    }

    Instrumentation.ActivityMonitor signin_monitor = getInstrumentation()
            .addMonitor(SignInActivity.class.getName(), null, false);

    //To click on the back button
    public static Matcher<View> navigationIconMatcher() {
        return allOf(
                isAssignableFrom(ImageButton.class),
                withParent(isAssignableFrom(Toolbar.class)));
    }

    @Test //ok
    public void signin(){
        //Test button to go to Contact us page
        onView(withId(R.id.btn_create_account)).perform(click());
        //wait for 3 seconds
        Activity signinActivity = getInstrumentation()
                .waitForMonitorWithTimeout(signin_monitor, 3000);
        assertNotNull(signinActivity);
        onView(navigationIconMatcher()).perform(click());
    }
}