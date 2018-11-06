package mt.edu.um.getalift;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import javax.security.auth.Destroyable;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
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

    private HelpActivity helpActivity = null;
    //Retrieve the context before each test
    @Before
    public void setup() {
        this.context = InstrumentationRegistry.getTargetContext();
        helpActivity = mActivityRule.getActivity();
    }

    /** ------ TESTS --------- */

    private boolean triedMatching;
    private String title;
    private Description description;

    //Test the title of the page
    @Test
    public void checkOnCreate() {
        //Check if the Toolbar has the good title
        onView(withId(R.id.tlb_profile)).check(matches(isDisplayed()));
        onView(withText("Get A Lift - Help")).check(matches(withParent(withId(R.id.tlb_profile))));

        //Check that the textViews on the page are not null
        View txt_view_faq = helpActivity.findViewById(R.id.text_faq);
        View txt_view_contact_us = helpActivity.findViewById(R.id.text_contact_us);
        View txt_view_about = helpActivity.findViewById(R.id.text_about);

        assertNotNull(txt_view_about);
        assertNotNull(txt_view_contact_us);
        assertNotNull(txt_view_faq);
    }

    //Make sure the button exist and make sure second activity is launched after click

    //add a monitor for each activity the app has to display after click on the specific image button
    Instrumentation.ActivityMonitor about_monitor = getInstrumentation()
            .addMonitor(PageAboutActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor faq_monitor = getInstrumentation()
            .addMonitor(PageFaqActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor contact_monitor = getInstrumentation()
            .addMonitor(ContactUsActivity.class.getName(), null, false);

    //To click on the back button
    public static Matcher<View> navigationIconMatcher() {
        return allOf(
                isAssignableFrom(ImageButton.class),
                withParent(isAssignableFrom(Toolbar.class)));
    }

    @Test
    public void testButtonToGoodPage(){
        //Test button to go to Contact us page
        assertNotNull( helpActivity.findViewById(R.id.image_contact_us) );
        onView(withId(R.id.image_contact_us)).perform(click());
        //wait for 3 seconds
        Activity contactActivity = getInstrumentation()
                .waitForMonitorWithTimeout(contact_monitor, 3000);
        assertNotNull(contactActivity);
        onView(navigationIconMatcher()).perform(click());

        //Test button to go to About page
        assertNotNull( helpActivity.findViewById(R.id.image_about) );
        onView(withId(R.id.image_about)).perform(click());
        Activity aboutActivity = getInstrumentation()
                .waitForMonitorWithTimeout(about_monitor, 3000);
        assertNotNull(contactActivity);
        onView(navigationIconMatcher()).perform(click());

        //Test button to go to FAQ page
        assertNotNull( helpActivity.findViewById(R.id.image_faq_button) );
        onView(withId(R.id.image_faq_button)).perform(click());
        Activity faqActivity = getInstrumentation()
                .waitForMonitorWithTimeout(faq_monitor, 3000);
        assertNotNull(contactActivity);
        onView(navigationIconMatcher()).perform(click());
    }

    //To test the come back button
    @Test
    public void onOptionsItemSelected() {
        onView(navigationIconMatcher()).perform(click());
    }

    @After
    public void tearDown() throws Exception {
        helpActivity = null;
    }
}