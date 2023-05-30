package com.example.ea6cinema;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

/*This Adapter class is responsible for providing views that represent items in a data set.*/
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    Context context;
    ArrayList<Model> modelArrayList;
    int DBcall = 0;

    public Adapter(Context context, ArrayList<Model> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items, null, false);
        return new ViewHolder(view);
    }

    /*This method listens to the movie cards on the recycle view, When clicking a movie card
     * it starts a appropriate overview activity for the correct movie.
     * This method also updating the movie table on the DB, And displaying the correct info for each movie. */
    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, overviewActivity.class);
                intent.putExtra("description", modelArrayList.get(position).getOverview());
                intent.putExtra("title", modelArrayList.get(position).getTitle());
                intent.putExtra("date", modelArrayList.get(position).getRelease_date());
                intent.putExtra("language", modelArrayList.get(position).getOriginal_language());
                intent.putExtra("id", modelArrayList.get(position).getId());
                intent.putExtra("originalname", modelArrayList.get(position).getOriginal_title());

                context.startActivity(intent);
            }
        });

        if(DBcall == 0){
            new DBupdateMovies(modelArrayList).execute();
            DBcall = 1;
        }

        String title = modelArrayList.get(position).getTitle() == null ? "no title" : modelArrayList.get(position).getTitle();
        String vote = Double.toString(modelArrayList.get(position).getVote_average());
        String adult = modelArrayList.get(position).isAdult() ? "18+" : "12+";
        String image = "https://image.tmdb.org/t/p/w500" + modelArrayList.get(position).getPoster_path();
        vote = vote + "/10"+ " \u2730";

        holder.heading.setText(title);
        holder.vote.setText(vote);
        holder.adult.setText(adult);
        Glide.with(context).load(image).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    /*Inner class to build the movie cards views.*/
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView heading, vote, adult;
        CardView cardView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.head);
            vote = itemView.findViewById(R.id.vote);
            imageView= itemView.findViewById(R.id.imageview);
            adult = itemView.findViewById(R.id.adult);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
}
