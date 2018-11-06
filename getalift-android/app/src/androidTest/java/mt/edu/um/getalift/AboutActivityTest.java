package mt.edu.um.getalift;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static mt.edu.um.getalift.HelpActivityTest.navigationIconMatcher;
import static org.junit.Assert.*;

public class AboutActivityTest {

    //For data
    private Context context;

    //To launch the about page before testing tests
    @Rule
    public final ActivityTestRule<PageAboutActivity> mActivityRule = new ActivityTestRule<>(PageAboutActivity.class);

    private PageAboutActivity aboutActivity = null;
    //Retrieve the context before each test
    @Before
    public void setup() {
        this.context = InstrumentationRegistry.getTargetContext();
        aboutActivity = mActivityRule.getActivity();
    }

    @Test
    public void onCreate() {
        //Check if the Toolbar has the good title
        onView(withId(R.id.tlb_profile)).check(matches(isDisplayed()));
        onView(withText("Get A Lift - About")).check(matches(withParent(withId(R.id.tlb_profile))));

        //Check that all the text views are not null
        View txt_view_about = aboutActivity.findViewById(R.id.text_about);
        View txt_view_answer1 = aboutActivity.findViewById(R.id.text_answer1);
        View txt_view_answer2 = aboutActivity.findViewById(R.id.text_answer2);
        View txt_view_answer3 = aboutActivity.findViewById(R.id.text_answer3);
        View txt_view_answer4 = aboutActivity.findViewById(R.id.text_answer4);
        View txt_view_question2 = aboutActivity.findViewById(R.id.text_question2);

        assertNotNull(txt_view_about);
        assertNotNull(txt_view_answer1);
        assertNotNull(txt_view_answer2);
        assertNotNull(txt_view_answer3);
        assertNotNull(txt_view_answer4);
        assertNotNull(txt_view_question2);
    }

    @Test
    public void onOptionsItemSelected() {
        onView(navigationIconMatcher()).perform(click());
    }
}