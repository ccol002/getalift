package mt.edu.um.getalift;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static mt.edu.um.getalift.HelpActivityTest.navigationIconMatcher;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;
/** Created by Thessalène JEAN-LOUIS**/
public class ContactUsActivityTest {
    //For data
    private Context context;

    private String correct_name = "test11";
    private String short_name = "test";
    private String long_name = "test11test11test11test11test11test11test11test11test11test11test11";

    private String correct_email = "test11@hotmail.fr";
    private String invalid_email = "test11hotmail.fr";

    private String correct_phone = "0622111111";
    private String invalid_phone="512 025.";
    private String invalid_phone2="512254";

    private String correct_subject = "Subject test : Request of information";
    private String incorrect_subject = "Subject test : Request of information & Subject test : Request of information & Subject test : Request of information";

    private String correct_message = "More than 20 characters : test test test test";
    private String incorrect_message = "More that 500 characters : it's too long";


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
    public void fillOutTheFormContactUs_success() {
        Log.e("@Test", "Performing fill out form success test");
        //Fill out all the fields correctly
        onView(withId(R.id.edt_contact_name)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_contact_name)).perform(typeText(correct_name));

        onView(withId(R.id.edt_contact_phoneNumber)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_contact_phoneNumber)).perform(typeText(correct_phone))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_contact_email)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_contact_email)).perform(typeText(correct_email))
        //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_contact_subject)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_contact_subject)).perform(typeText(correct_subject))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_contact_message)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_contact_message)).perform(typeText(correct_message))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        //Click on "connection" button
        onView(withId(R.id.contact_validate_button))
                .perform(ViewActions.click());

        //onView(withText("Sending the email")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        //Not Redirection to the homeMapActivity : ok
    }

    @Test
    public void fillout_fail_short_name(){
        Log.e("@Test", "Performing fill out form fail test");
        //Fill out all the fields correctly
        onView(withId(R.id.edt_contact_name)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_contact_name)).perform(typeText(short_name));

        onView(withId(R.id.edt_contact_phoneNumber)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_contact_phoneNumber)).perform(typeText(correct_phone))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_contact_email)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_contact_email)).perform(typeText(correct_email))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_contact_subject)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_contact_subject)).perform(typeText(correct_subject))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_contact_message)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_contact_message)).perform(typeText(correct_message))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        //Click on "send message" button
        onView(withId(R.id.contact_validate_button))
                .perform(ViewActions.click());

        onView(withText("The name must be more than 5 character long.")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        //Not Redirection to the homeMapActivity : ok
    }

    @Test
    public void fillout_fail_long_name(){
        Log.e("@Test", "Performing fill out form fail test");
        //Fill out all the fields correctly
        onView(withId(R.id.edt_contact_name)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_contact_name)).perform(typeText(long_name));

        onView(withId(R.id.edt_contact_phoneNumber)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_contact_phoneNumber)).perform(typeText(correct_phone))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_contact_email)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_contact_email)).perform(typeText(correct_email))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_contact_subject)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_contact_subject)).perform(typeText(correct_subject))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_contact_message)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_contact_message)).perform(typeText(correct_message))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        //Click on "send message" button
        onView(withId(R.id.contact_validate_button))
                .perform(ViewActions.click());

        onView(withText("The name must be less than 63 character long.")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        //Not Redirection to the homeMapActivity : ok
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