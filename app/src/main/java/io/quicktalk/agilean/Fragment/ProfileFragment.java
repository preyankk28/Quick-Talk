package io.quicktalk.agilean.Fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.InputStream;
import java.net.URL;

import io.quicktalk.agilean.Login.LoginActivity;
import io.quicktalk.agilean.R;

public class ProfileFragment extends Fragment {
    private View view;
    private Context context;
    TextView Name, Email;
    Button logoutbutton;
    FirebaseUser user;
    String Uid;
    FirebaseAuth mAuth;
    LoginActivity loginActivity;
    private GoogleSignInClient mGoogleSignInClient;
   ImageView photo;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_profile,container,false);
        init();
        return view;
    }

    private void init() {
        initElements();
        initListeners();
        initActions();

    }

    private void initActions() {


    }

    private void initListeners() {


    }

    private void initElements() {
     Name =view.findViewById(R.id.name);
     Email = view.findViewById(R.id.email);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Name.setText(user.getDisplayName());
        Email.setText(user.getEmail());
       Uri photourl = user.getPhotoUrl();
        Log.e("TAG","Photo Url"+ photourl );

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
//        logoutbutton = view.findViewById(R.id.logoutprofile);
//        logoutbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAuth.signOut();
//
//                // Google sign out
//                mGoogleSignInClient.signOut();
//
//
//            }
//        });
        photo = view.findViewById(R.id.photo);
        Glide.with(photo.getContext())
                .load(photourl)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(photo);
    }
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

}
