package io.quicktalk.agilean.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.quicktalk.agilean.Adapter.DiscussionRecyclerAdapter;
import io.quicktalk.agilean.Model.BroadcastModel;
import io.quicktalk.agilean.R;

public class HomeFragment extends Fragment {
    private View view;
    private Context context;
    RecyclerView userfetchfirebasetecyc;
    DiscussionRecyclerAdapter discussionRecyclerAdapter;
    FirebaseUser user;
    String Uid;
    DatabaseReference Discussions;
    FirebaseAuth mAuth;
    private SearchView searchView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_home,container,false);
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
     //   Discussions = FirebaseDatabase.getInstance().getReference("Users").child("Uid")
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Uid = user.getUid();

        userfetchfirebasetecyc = view.findViewById(R.id.meetingfetchuser);
      userfetchfirebasetecyc.setLayoutManager(new LinearLayoutManager(context));
//        userfetchfirebasetecyc.setLayoutManager(new CardSliderLayoutManager(context));
//        new CardSnapHelper().attachToRecyclerView(userfetchfirebasetecyc);

        FirebaseRecyclerOptions<BroadcastModel> options = new FirebaseRecyclerOptions.Builder<BroadcastModel>()
            //    .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").child(Uid).child("Discussions"), BroadcastModel.class)
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Discussions").child(Uid), BroadcastModel.class)
                .build();


        discussionRecyclerAdapter= new DiscussionRecyclerAdapter(options,context);
        userfetchfirebasetecyc.setAdapter(discussionRecyclerAdapter);

        searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        discussionRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
   discussionRecyclerAdapter.stopListening();


    }
    private void processsearch(String s) {

        FirebaseRecyclerOptions<BroadcastModel> options = new FirebaseRecyclerOptions.Builder<BroadcastModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Discussions").child(Uid).orderByChild("topic").startAt(s).endAt(s+ "\uf8ff"), BroadcastModel.class)
                .build();
        discussionRecyclerAdapter = new DiscussionRecyclerAdapter(options,context);
        discussionRecyclerAdapter.startListening();


        userfetchfirebasetecyc.setAdapter(discussionRecyclerAdapter);


    }
}
