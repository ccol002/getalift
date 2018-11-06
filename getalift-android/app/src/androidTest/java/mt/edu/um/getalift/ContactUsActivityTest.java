package mt.edu.um.getalift;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static mt.edu.um.getalift.HelpActivityTest.navigationIconMatcher;
import static org.junit.Assert.*;
/** Created by Thessalène JEAN-LOUIS**/
public class ContactUsActivityTest {
    //For data
    private Context context;

    //To launch the Help page before testing tests
    @Rule
    public final ActivityTestRule<ContactUsActivity> mActivityRule = new ActivityTestRule<>(ContactUsActivity.class);

    private ContactUsActivity contactUsActivity = null;
    //Retrieve the context before each test
    @Before
    public void setup() {
        this.context = InstrumentationRegistry.getTargetContext();
        contactUsActivity = mActivityRule.getActivity();
    }

    /** --------------------------------- TESTS --------------------------------------------*/

    //Make sure the button exist and make sure second activity is launched after click

    //add a monitor for each activity the app has to display after click on the specific image button
   /* Instrumentation.ActivityMonitor about_monitor = getInstrumentation()
            .addMonitor(PageAboutActivity.class.getName(), null, false);*/

    @Test
    public void onCreateContactUs() {
        //Check if the Toolbar has the good title
        onView(withId(R.id.tlb_profile)).check(matches(isDisplayed()));
        onView(withText("Get A Lift - Contact Us")).check(matches(withParent(withId(R.id.tlb_profile))));
    }

    @Test
    public void onOptionsItemSelectedontactUs() {
        onView(navigationIconMatcher()).perform(click());
    }

    @Test
    public void fillOutTheFormontactUs() {
    }

    @Test
    public void profilontactUs() {
    }

    @Test
    public void clearAllontactUs() {
    }

    @Test
    public void sendMailontactUs() {
    }
}