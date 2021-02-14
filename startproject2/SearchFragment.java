package com.example.startproject2;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;
import static java.sql.DriverManager.println;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    MovieAdapter adapter;
    RecyclerView recyclerView;
    String uriString =
            "content://com.example.startproject2.movieprovider/movie";
    Movie movie;
    static RequestQueue requestQueue;
     String url = "https://openapi.naver.com/v1/search/movie.json?query=";
     String URLs;
    MovieProvider MP ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);

        adapter = new MovieAdapter();

        recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);


        SearchView searchView = rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
              URLs = url + query;
                 makeRequest();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        requestQueue = Volley.newRequestQueue(getContext());

      Uri  uri = new Uri.Builder().build().parse(uriString);
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null,
                null, null);

        while(cursor.moveToNext())
        {
            movie = new Movie();
            Log.i("tags","this"+cursor.getCount());
            movie.title = cursor.getString(cursor.getColumnIndex("title"));
            movie.director = cursor.getString(cursor.getColumnIndex("director"));
            movie.actor = cursor.getString(cursor.getColumnIndex("actor"));
            movie.userRating = cursor.getInt(cursor.getColumnIndex("rating"));
            movie.image  =cursor.getString(cursor.getColumnIndex("image"));
           // movie.pubDate = cursor.getString(cursor.getColumnIndex("pubData"));
            adapter.addItem(movie);
        }
        adapter.notifyDataSetChanged();

        return rootView;
    }
    void processResponse(String response)
    {
        // 이곳에서 Gson을 통해 json으로 API의 정보를 바꿔옴
        Gson gson = new Gson();
        MovieList movieList = gson.fromJson(response,MovieList.class);
        clearMovie();
        insertMovie(movieList);
        for (int i = 0; i< movieList.items.size();i++)
        {
            movie = movieList.items.get(i);
         //   movie.title = query;
            adapter.addItem(movie);

        }
        adapter.notifyDataSetChanged();
    }
    void makeRequest()
    {
        Log.i("Request","request!!");
        // api의 주소를 GET메소드로 요청
        StringRequest request = new StringRequest(Request.Method.GET, URLs, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("movie",response);
                processResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                println("에러 : " + error);
            }
        }
        )
        {
            // api의 추가정보 - 아이디, 비번 설정 -> AUTHFaileueeroor
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap();
                params.put("X-Naver-Client-Id", "r9ZKQAjLeDIVJV2EiF_P");
                params.put("X-Naver-Client-Secret","6IAV9rNp8Z");
                Log.d("getHedaer =>", ""+ params);
                return params;
            }
        };
        request.setShouldCache(false);
        Toast.makeText(getContext(),request.toString(), Toast.LENGTH_SHORT).show();
        // 요청을 해당 리퀘스트큐에 추가
        requestQueue.add(request);
    }
    private void clearMovie() {
        Uri uri = new Uri.Builder().build().parse(uriString);
        int count = getActivity().getContentResolver().delete(uri, null, null);
    }
    private void insertMovie(MovieList movieList) {
        Uri uri = new Uri.Builder().build().parse(uriString);


        if(movieList.items.size() != 0) {
            for (int i = 0; i < movieList.items.size(); i++) {
                Movie movie = movieList.items.get(i);
                ContentValues values = new ContentValues();
                values.put("title", movie.title);
                values.put("director", movie.director);
                values.put("actor", movie.actor);
                values.put("link", movie.link);
                values.put("rating", movie.userRating);
                values.put("image", movie.image);
                values.put("pubDate", movie.pubDate);
          getActivity().getContentResolver().insert(uri, values);
               //
            }
            uri = new Uri.Builder().build().parse(uriString);
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null,
                    null, null);
        }


    }
}
