package io.quicktalk.agilean.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import io.quicktalk.agilean.Model.LiveCountModel;
import io.quicktalk.agilean.R;

public class LiveCountAdapter extends FirebaseRecyclerAdapter<LiveCountModel, LiveCountAdapter.myviewholder> {

    private Context context;

    public LiveCountAdapter(@NonNull FirebaseRecyclerOptions<LiveCountModel> options, Context context) {
        super(options);
        this.context = context;
    }



    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull LiveCountModel model) {
    holder.Name.setText(model.getName());
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.livecountrecycleritem, parent,false);


        return new myviewholder(view);

    }

    public class myviewholder extends RecyclerView.ViewHolder{

        TextView Name;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
           Name = itemView.findViewById(R.id.livecountnameitem);





        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
