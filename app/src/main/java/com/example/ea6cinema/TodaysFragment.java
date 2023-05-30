package com.example.ea6cinema;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodaysFragment extends Fragment {

    private String api = "Your api key";
    ArrayList<Model> modelArrayList;
    Adapter adapter;

    /*On creating the view of the fragment the recycle view contains all the movies to display, and the adapter is
      in charge of providing the current item(movie) data on the holder. */

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.todaysfragment, null);
        RecyclerView recyclerViewHome = v.findViewById(R.id.todaysview);
        modelArrayList = new ArrayList<>();
        recyclerViewHome.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter(getContext(), modelArrayList);
        recyclerViewHome.setAdapter(adapter);
        getMovies();
        return v;
    }

    /*This method calling the apiUti class to create a retrofit api interface to retrieve the most updated movies on the cinema.
      if the response is successful we add all the json body response to modelArrayList  */
    private void getMovies() {
        ApiUti.getApiInter().getMovies(api).enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {

                if(response.isSuccessful()){
                    modelArrayList.addAll(response.body().getMovies());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {

            }
        });
    }
}