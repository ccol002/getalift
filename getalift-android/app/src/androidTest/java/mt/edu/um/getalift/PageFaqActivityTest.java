package mt.edu.um.getalift;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static mt.edu.um.getalift.HelpActivityTest.navigationIconMatcher;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

public class PageFaqActivityTest {

    //For data
    private Context context;

    //To launch the about page before testing tests
    @Rule
    public final ActivityTestRule<PageFaqActivity> mActivityRule = new ActivityTestRule<>(PageFaqActivity.class);

    private PageFaqActivity faqActivity = null;
    //Retrieve the context before each test
    @Before
    public void setup() {
        this.context = InstrumentationRegistry.getTargetContext();
        faqActivity = mActivityRule.getActivity();
    }

    @Test
    public void onCreateFAQ() {
        //Check if the Toolbar has the good title
        onView(withId(R.id.tlb_profile)).check(matches(isDisplayed()));
        onView(withText("Get A Lift - F.A.Q")).check(matches(withParent(withId(R.id.tlb_profile))));

        //Check that all the text views are not null
        View txt_view_answer1 = faqActivity.findViewById(R.id.text_answer1);
        View txt_view_answer2 = faqActivity.findViewById(R.id.text_answer2);
        View txt_view_answer3 = faqActivity.findViewById(R.id.text_answer3);
        View txt_view_answer4 = faqActivity.findViewById(R.id.text_answer4);
        View txt_view_answer5 = faqActivity.findViewById(R.id.text_answer5);
        View txt_view_answer6= faqActivity.findViewById(R.id.text_answer6);
        View txt_view_question1 = faqActivity.findViewById(R.id.text_question1);
        View txt_view_question2 = faqActivity.findViewById(R.id.text_question2);
        View txt_view_question3 = faqActivity.findViewById(R.id.text_question3);
        View txt_view_question4 = faqActivity.findViewById(R.id.text_question4);
        View txt_view_question5 = faqActivity.findViewById(R.id.text_question5);
        View txt_view_question6 = faqActivity.findViewById(R.id.text_question6);


        assertNotNull(txt_view_answer1);
        assertNotNull(txt_view_answer2);
        assertNotNull(txt_view_answer3);
        assertNotNull(txt_view_answer4);
        assertNotNull(txt_view_answer5);
        assertNotNull(txt_view_answer6);
        assertNotNull(txt_view_question1);
        assertNotNull(txt_view_question2);
        assertNotNull(txt_view_question3);
        assertNotNull(txt_view_question4);
        assertNotNull(txt_view_question5);
        assertNotNull(txt_view_question6);
    }

    public static Matcher<View> navigationIconMatcher() {
        return allOf(
                isAssignableFrom(ImageButton.class),
                withParent(isAssignableFrom(Toolbar.class)));
    }

    @Test
    public void onOptionsItemSelected() {
        onView(navigationIconMatcher()).perform(click());
    }

}