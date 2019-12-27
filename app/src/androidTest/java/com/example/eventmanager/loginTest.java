package com.example.eventmanager;

import android.widget.Button;
import android.widget.TextView;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class loginTest {
    private MainActivity loginActivity;

    @Rule
    public final ActivityTestRule<MainActivity> act = new ActivityTestRule<MainActivity>(MainActivity.class);


    @Before
    public void setUp() throws Exception {
        loginActivity = act.getActivity();

    }

    @After
    public void tearDown() throws Exception {
        loginActivity.finish();

    }

    @Test
    public void testOnCreate() throws Exception {
        Assert.assertEquals("sign in activity", (loginActivity.getTitle().toString().toLowerCase()));
        Assert.assertEquals("sign in", ((Button) loginActivity.findViewById(R.id.signInButtonId)).getText().toString().toLowerCase());
        Assert.assertEquals("are you not register? sign up...", ((TextView) loginActivity.findViewById(R.id.signUpTextId)).getText().toString().toLowerCase());
    }


}
