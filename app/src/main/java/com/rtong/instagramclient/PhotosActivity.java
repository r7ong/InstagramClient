package com.rtong.instagramclient;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "2524a65ade084873856bc3bb9c49532b";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        // send out api request to popular photos
        photos = new ArrayList<>();
        aPhotos = new InstagramPhotosAdapter(this, photos);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPopularPhotos();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        fetchPopularPhotos();


    }

    // trigger api request
    public void fetchPopularPhotos() {
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        // create network client
        AsyncHttpClient client = new AsyncHttpClient();
        // trigger get request
        client.get(url, null, new JsonHttpResponseHandler(){
            // onSuccess (worked, 200)

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                photos.clear();
                // iterate each of the photo items and decode the itme into a java object
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data");
                    // iterate array of posts
                    for(int i = 0; i < photosJSON.length(); i++){
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photoJSON.getJSONObject("user").getString("username");

                        if (!photoJSON.isNull("caption") && photoJSON.getJSONObject("caption") != null){
                            photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        }

                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        photo.profilePicUrl = photoJSON.getJSONObject("user").getString("profile_picture");
                        photo.createdTime = photoJSON.getString("created_time");

                        photo.commentCount = photoJSON.getJSONObject("comments").getInt("count");
                        JSONArray comments = photoJSON.getJSONObject("comments").getJSONArray("data");
                        String usrName;
                        String comment;
                        if(comments.length() > 0){
                            usrName = comments.getJSONObject(0).getJSONObject("from").getString("username");
                            comment = comments.getJSONObject(0).getString("text");
                            photo.comment1 = "<b><font color='#3E6C8F'>" + usrName + "</font></b>" + " "  + comment;
                        }
                        if(comments.length() > 1){
                            usrName = comments.getJSONObject(1).getJSONObject("from").getString("username");
                            comment = comments.getJSONObject(1).getString("text");
                            photo.comment2 = "<b><font color='#3E6C8F'>" + usrName + "</font></b>" + " "  + comment;
                        }

                        photos.add(photo);
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }

                //callback
                aPhotos.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);

            }
            // onFailure


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", "Fetch photos error: ");

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
