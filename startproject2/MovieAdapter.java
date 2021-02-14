package com.example.startproject2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.LogRecord;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    ArrayList<Movie> items = new ArrayList<Movie>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        final View itemView = inflater.inflate(R.layout.movie_item, parent, false);
        Setparent(parent);
        return new ViewHolder(itemView);
    }
    public ViewHolder holder;

    // MovieAdapter의 context를 갖고 오기 위해 ViewHolder의 Parent를 가져옴
    public void Setparent(ViewGroup group)
    {
       Parent = group;
    }


ViewGroup Parent;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Movie item = items.get(position);
        holder.setItem(item);
        holder.layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                // 인텐트를 통해 해당 아이템의 link이동
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(items.get(position).link));
                Parent.getContext().startActivity(intent);
                Log.i("click","click!!!!");
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Movie item){
        items.add(item);
    }

    public void setItems(ArrayList<Movie> items){
        this.items = items;
    }

    public Movie getItem(int position) {
        return items.get(position);
    }

    public void clearItems() {
        this.items.clear();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView, textView2, textView3, textView4;
        ImageView imageView;
        RatingBar ratingBar;
        public final View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            imageView = itemView.findViewById(R.id.imageView);
            ratingBar = itemView.findViewById(R.id.ratingBar);

            layout = itemView;
        }
        Handler handler = new Handler();
        // Bitmap _bitmap;
        //비트맵 로드하는 쓰레드
        class BitmapLoadThread extends Thread
        {
            String _url;
            public BitmapLoadThread(String url)
            {
                _url = url;
            }
            @Override
            public void run() {
                try
                {
                    //쓰레드에서 url을 비트맵으로 바꿔주고
                    final Bitmap  _bitmap =   getBitmap(_url);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                        // 핸들러에서 이미지뷰에 적용시킨다.
                            imageView.setImageBitmap(_bitmap);
                        }
                    });
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        public void setItem(Movie item) {
            item.title = item.title.replace("<b>","");
            item.title = item.title.replace("</b>","");
            textView.setText(item.title);
            textView2.setText("감독: " + item.director);
            textView3.setText("출연: " + item.actor);
            BitmapLoadThread BT = new BitmapLoadThread(item.image);
            BT.start();


            if(item.userRating != 0) {
                textView4.setText(item.userRating + " ");
                ratingBar.setVisibility(View.VISIBLE);
                ratingBar.setRating(item.userRating / 2);
            } else {
                textView4.setText("평점 없음");
                ratingBar.setVisibility(View.GONE);
            }

        }
    }
    // 비트맵 함수
    private Bitmap getBitmap(String url)
    {
        URL imgUrl = null;
        HttpURLConnection connection = null;
        InputStream is = null;
        Bitmap retBitmap = null;
        try{ imgUrl = new URL(url); connection = (HttpURLConnection) imgUrl.openConnection(); connection.setDoInput(true);
            connection.connect();
            is = connection.getInputStream();
            retBitmap = BitmapFactory.decodeStream(is); }catch(Exception e)
        { e.printStackTrace(); return null; }finally { if(connection!=null) {  } return retBitmap; }
    }




}
