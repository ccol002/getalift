package mt.edu.um.getalift;

import android.app.Activity;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

//All the tests pass
public class SignInActivityTest {
    @Rule
    public final ActivityTestRule<SignInActivity> mActivityRule = new ActivityTestRule<>(SignInActivity.class);

    private String username_already_registered="dada";
    private String correct_username="test11";
    //A second one because the first one has been added to the database os, the message is : it's already exists
    private String correct_username2 = "test22";
    private String correct_username3 = "test33";
    private String short_username = "test";
    private String long_username = "dada123dada123dada123dada123dada123dada123dada123dada123dada123dada123";

    private String short_password ="test";
    private String correct_password ="test11";
    private String long_password = "dada123dada123dada123dada123dada123dada123dada123dada123dada123dada123";

    private String correct_name = "test11";
    private String correct_surname = "test111";

    private String correct_email = "test11@hotmail.fr";
    private String invalid_email = "test11hotmail.fr";


    private String correct_phone = "0622111111";
    private String invalid_phone="512 025.";
    private String invalid_phone2="512254";


    @Test
    public void  signin_success() {
        Log.e("@Test", "Performing login success test");
        //Fill out all the fields correctly
        onView(withId(R.id.edt_signin_username)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_username)).perform(typeText(correct_username));

        onView(withId(R.id.edt_signin_password)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_password)).perform(typeText(correct_password));

        onView(withId(R.id.edt_signin_email)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_email)).perform(typeText(correct_email));

        onView(withId(R.id.edt_signin_firstname)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_firstname)).perform(typeText(correct_name))
        //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_signin_lastname)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_lastname)).perform(typeText(correct_surname))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_signin_phonenumber)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_phonenumber)).perform(typeText(correct_phone))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        //Click on "connection" button
        onView(withId(R.id.button))
                .perform(ViewActions.click());

        //onView(withText("Auth failed. Wrong credentials.")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        //Not Redirection to the homeMapActivity : ok
    }

    @Test
    public void  signin_failed_short_username() {
        Log.e("@Test", "Performing login success test");
        //Fill out all the fields correctly
        onView(withId(R.id.edt_signin_username)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_username)).perform(typeText(short_username));

        onView(withId(R.id.edt_signin_password)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_password)).perform(typeText(correct_password));

        onView(withId(R.id.edt_signin_email)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_email)).perform(typeText(correct_email));

        onView(withId(R.id.edt_signin_firstname)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_firstname)).perform(typeText(correct_name))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_signin_lastname)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_lastname)).perform(typeText(correct_surname))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_signin_phonenumber)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_phonenumber)).perform(typeText(correct_phone))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        //Click on "connection" button
        onView(withId(R.id.button))
                .perform(ViewActions.click());

        onView(withText("The username must be more than 6 character long.")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        //Not Redirection to the homeMapActivity : ok
    }

    @Test
    public void  signin_failed_long_username() {
        Log.e("@Test", "Performing login success test");
        //Fill out all the fields correctly
        onView(withId(R.id.edt_signin_username)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_username)).perform(typeText(long_username));

        onView(withId(R.id.edt_signin_password)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_password)).perform(typeText(correct_password));

        onView(withId(R.id.edt_signin_email)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_email)).perform(typeText(correct_email));

        onView(withId(R.id.edt_signin_firstname)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_firstname)).perform(typeText(correct_name))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_signin_lastname)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_lastname)).perform(typeText(correct_surname))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_signin_phonenumber)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_phonenumber)).perform(typeText(correct_phone))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        //Click on "connection" button
        onView(withId(R.id.button))
                .perform(ViewActions.click());

        onView(withText("The username must be less than 63 character long.")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        //Not Redirection to the homeMapActivity : ok
    }

    @Test
    public void  signin_failed_short_pwd() {
        Log.e("@Test", "Performing login success test");
        //Fill out all the fields correctly
        onView(withId(R.id.edt_signin_username)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_username)).perform(typeText(correct_username));

        onView(withId(R.id.edt_signin_password)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_password)).perform(typeText(short_password));

        onView(withId(R.id.edt_signin_email)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_email)).perform(typeText(correct_email));

        onView(withId(R.id.edt_signin_firstname)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_firstname)).perform(typeText(correct_name))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_signin_lastname)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_lastname)).perform(typeText(correct_surname))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_signin_phonenumber)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_phonenumber)).perform(typeText(correct_phone))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        //Click on "connection" button
        onView(withId(R.id.button))
                .perform(ViewActions.click());

        onView(withText("The password must be more than 6 character long.")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        //Not Redirection to the homeMapActivity : ok
    }

    @Test
    public void  signin_failed_long_pwd() {
        Log.e("@Test", "Performing login success test");
        //Fill out all the fields correctly
        onView(withId(R.id.edt_signin_username)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_username)).perform(typeText(correct_username));

        onView(withId(R.id.edt_signin_password)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_password)).perform(typeText(long_password));

        onView(withId(R.id.edt_signin_email)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_email)).perform(typeText(correct_email));

        onView(withId(R.id.edt_signin_firstname)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_firstname)).perform(typeText(correct_name))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_signin_lastname)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_lastname)).perform(typeText(correct_surname))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_signin_phonenumber)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_phonenumber)).perform(typeText(correct_phone))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        //Click on "connection" button
        onView(withId(R.id.button))
                .perform(ViewActions.click());

        onView(withText("The password must be less than 63 character long.")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        //Not Redirection to the homeMapActivity : ok
    }

    @Test
    public void  signin_failed_missing_firstname() {
        Log.e("@Test", "Performing login failure test");
        //Fill out all the fields correctly
        onView(withId(R.id.edt_signin_username)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_username)).perform(typeText(correct_username2));

        onView(withId(R.id.edt_signin_password)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_password)).perform(typeText(correct_password));

        onView(withId(R.id.edt_signin_email)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_email)).perform(typeText(correct_email))
        //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_signin_lastname)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_lastname)).perform(typeText(correct_surname))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_signin_phonenumber)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_phonenumber)).perform(typeText(correct_phone))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        //Click on "connection" button
        onView(withId(R.id.button))
                .perform(ViewActions.click());

        onView(withText("You must tell your firstname.")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        //Not Redirection to the homeMapActivity : ok
    }

    @Test
    public void  signin_failed_missing_lastname() {
        Log.e("@Test", "Performing login failure test");
        //Fill out all the fields correctly
        onView(withId(R.id.edt_signin_username)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_username)).perform(typeText(correct_username2));

        onView(withId(R.id.edt_signin_password)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_password)).perform(typeText(correct_password));

        onView(withId(R.id.edt_signin_email)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_email)).perform(typeText(correct_email))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_signin_firstname)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_firstname)).perform(typeText(correct_surname))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_signin_phonenumber)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_phonenumber)).perform(typeText(correct_phone))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        //Click on "connection" button
        onView(withId(R.id.button))
                .perform(ViewActions.click());

        onView(withText("You must tell your last name.")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        //Not Redirection to the homeMapActivity : ok
    }

    @Test
    public void  signin_failed_invalid_address() {
        Log.e("@Test", "Performing login success test");
        //Fill out all the fields correctly
        onView(withId(R.id.edt_signin_username)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_username)).perform(typeText(correct_username));

        onView(withId(R.id.edt_signin_password)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_password)).perform(typeText(correct_password));

        onView(withId(R.id.edt_signin_email)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_email)).perform(typeText(invalid_email));

        onView(withId(R.id.edt_signin_firstname)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_firstname)).perform(typeText(correct_name))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_signin_lastname)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_lastname)).perform(typeText(correct_surname))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_signin_phonenumber)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_phonenumber)).perform(typeText(correct_phone))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        //Click on "connection" button
        onView(withId(R.id.button))
                .perform(ViewActions.click());

        onView(withText("You must enter a valid email adress.")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        //Not Redirection to the homeMapActivity : ok
    }

    @Test
    //To check Pattern match
    public void  signin_failed_invalid_phone_number1() {
        Log.e("@Test", "Performing login success test");
        //Fill out all the fields correctly
        onView(withId(R.id.edt_signin_username)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_username)).perform(typeText(correct_username3));

        onView(withId(R.id.edt_signin_password)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_password)).perform(typeText(correct_password));

        onView(withId(R.id.edt_signin_email)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_email)).perform(typeText(correct_email));

        onView(withId(R.id.edt_signin_firstname)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_firstname)).perform(typeText(correct_name))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_signin_lastname)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_lastname)).perform(typeText(correct_surname))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_signin_phonenumber)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_phonenumber)).perform(typeText(invalid_phone))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        //Click on "connection" button
        onView(withId(R.id.button))
                .perform(ViewActions.click());

        onView(withText("You must enter a valid phone number.")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        //Not Redirection to the homeMapActivity : ok
    }

    @Test
    //To check length one time : you can change the format to check the validity of the phoneNumber in more ways
    public void  signin_failed_invalid_phone_number2() {
        Log.e("@Test", "Performing login success test");
        //Fill out all the fields correctly
        onView(withId(R.id.edt_signin_username)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_username)).perform(typeText(correct_username3));

        onView(withId(R.id.edt_signin_password)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_password)).perform(typeText(correct_password));

        onView(withId(R.id.edt_signin_email)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_email)).perform(typeText(correct_email));

        onView(withId(R.id.edt_signin_firstname)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_firstname)).perform(typeText(correct_name))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_signin_lastname)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_lastname)).perform(typeText(correct_surname))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        onView(withId(R.id.edt_signin_phonenumber)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_signin_phonenumber)).perform(typeText(invalid_phone2))
                //To close the keyboard
                .perform(closeSoftKeyboard());

        //Click on "connection" button
        onView(withId(R.id.button))
                .perform(ViewActions.click());

        onView(withText("You must enter a valid phone number.")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        //Not Redirection to the homeMapActivity : ok
    }

}