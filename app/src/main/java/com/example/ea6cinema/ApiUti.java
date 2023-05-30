package com.example.ea6cinema;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUti {

    /*This ApiUti class creates a java interface which adapted to HTTP calls using retrofit library and GSON converter
    * to serialize the JSON response. */

    private static Retrofit retrofit=null;

    public static ApiInterface getApiInter(){

        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(ApiInterface.base_url).addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiInterface.class);
    }
}
