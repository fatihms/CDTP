package com.example.greenhouse;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.firebase.firestore.DocumentSnapshot;

public class FirestoreAdapter extends FirestoreRecyclerAdapter<GreenHousesModel, FirestoreAdapter.GreenHousesViewHolder> {
    private OnItemClickListener listener;

    public FirestoreAdapter(@NonNull FirestoreRecyclerOptions<GreenHousesModel> options) {
        super(options);
    }

    // Verileri göstermek için metod
    @Override
    protected void onBindViewHolder(@NonNull GreenHousesViewHolder holder, int position, @NonNull GreenHousesModel model) {

        holder.list_name.setText(model.getName());
        holder.list_temperature.setText(model.getCurrentTemp() + " °C");

        double ct = Double.parseDouble(model.getCurrentTemp());

        if(ct < 10){
            holder.list_description.setText("Aşırı düşük");
            holder.list_image.setImageResource(R.drawable.t1);

        }else if(ct >= 10 && ct <= 20){
            holder.list_description.setText("Normal");
            holder.list_image.setImageResource(R.drawable.t2);
        }else if(ct > 20 && ct <= 30){
            holder.list_description.setText("Fazla");
            holder.list_image.setImageResource(R.drawable.t3);
        }else{
            holder.list_description.setText("Aşırı fazla");
            holder.list_image.setImageResource(R.drawable.t3);
        }

    }

    // viewHolderin başlatılması için metod
    @NonNull
    @Override
    public GreenHousesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
        return new GreenHousesViewHolder(view);
    }

    public class GreenHousesViewHolder extends RecyclerView.ViewHolder  {

        private TextView list_name;
        private TextView list_temperature;
        private TextView list_description;
        private ImageView list_image;

        public GreenHousesViewHolder(@NonNull View itemView) {
            super(itemView);

            list_name = itemView.findViewById(R.id.list_name);
            list_temperature = itemView.findViewById(R.id.list_temperature);
            list_description = itemView.findViewById(R.id.list_description);
            list_image = itemView.findViewById(R.id.list_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

        }

    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


}

