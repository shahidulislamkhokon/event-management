package com.example.eventmanager;

import android.widget.Button;
import android.widget.TextView;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class signUpTest {

    private SignUp sign;

    @Rule
    public final ActivityTestRule<SignUp> act = new ActivityTestRule<SignUp>(SignUp.class);


    @Before
    public void setUp() throws Exception {
        sign = act.getActivity();

    }

    @After
    public void tearDown() throws Exception {
        sign.finish();

    }

    @Test
    public void testOnCreate() throws Exception {
        Assert.assertEquals("sign up activity", (sign.getTitle().toString().toLowerCase()));
        Assert.assertEquals("sign up", ((Button) sign.findViewById(R.id.signUpButtonId)).getText().toString().toLowerCase());
        Assert.assertEquals("are you alredy registered? sign in...", ((TextView) sign.findViewById(R.id.signInTextId)).getText().toString().toLowerCase());
    }
}
