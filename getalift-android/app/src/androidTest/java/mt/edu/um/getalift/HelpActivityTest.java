package mt.edu.um.getalift;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import javax.security.auth.Destroyable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

/** Created by JEAN-LOUIS Thessalène */

//Executor of test : JUnit4
@RunWith(AndroidJUnit4.class)
public class HelpActivityTest {

    //For data
    private Context context;

    //To launch the Help page before testing tests
    @Rule
    public final ActivityTestRule<HelpActivity> mActivityRule = new ActivityTestRule<>(HelpActivity.class);

    //Retrieve the context before each test
    @Before
    public void setup() {
        this.context = InstrumentationRegistry.getTargetContext();
    }

    /** ------ TESTS --------- */

    private boolean triedMatching;
    private String title;
    private Description description;

    @Test
    public void checkOnCreate() {
        //Check if the Toolbar has the good title
        onView(withId(R.id.tlb_profile)).check(matches(isDisplayed()));
        onView(withText("Get A Lift - Help")).check(matches(withParent(withId(R.id.tlb_profile))));


    }



    @Test
    public void onOptionsItemSelected() {
    }
}