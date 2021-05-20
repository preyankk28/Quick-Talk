package io.quicktalk.agilean.AppIntroSlides;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.quicktalk.agilean.Login.LoginActivity;
import io.quicktalk.agilean.MainScreen.MainScreen;

public class AppIntro extends com.github.appintro.AppIntro2 {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent i = new Intent(getApplicationContext(), MainScreen.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }



        addSlide(new IntroSlide1Fragment());
        addSlide(new IntroSlide2Fragment());
        addSlide(new IntroSlide3Fragment());
        //setTransformer(AppIntroPageTransformerType.SlideOver);


    }




    @Override
    public void onDonePressed (Fragment currentFragment){
        super.onDonePressed(currentFragment);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent i = new Intent(getApplicationContext(), MainScreen.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);


        }
        Intent i = new Intent(AppIntro.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);


    }

    @Override
    public void onSkipPressed (Fragment currentFragment){
        super.onSkipPressed(currentFragment);

        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        Intent i = new Intent(AppIntro.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);


    }
}
