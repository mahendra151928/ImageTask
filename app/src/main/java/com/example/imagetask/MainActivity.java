package com.example.imagetask;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UserRVAdapter userRVAdapter;
    private RecyclerView userRV;
    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;

    // creating a variable for our page and limit as 10
    // as our api is having highest limit as 10 so
    // we are setting a limit = 10
    int page = 1, limit = 10;
    private ArrayList<Photo> photoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // creating a new array list.
        photoList = new ArrayList<>();
        // initializing our views.
        userRV = findViewById(R.id.idRVUsers);
        loadingPB = findViewById(R.id.idPBLoading);
        nestedSV = findViewById(R.id.idNestedSV);

        // calling a method to load our api.
        getDataFromAPI(page, limit);

        // adding on scroll change listener method for our nested scroll view.
        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++;
                    loadingPB.setVisibility(View.VISIBLE);
                    getDataFromAPI(page, limit);
                }
            }
        });
    }

    private void getDataFromAPI(int page, int limit) {
        if (page > limit) {
            // checking if the page number is greater than limit.
            // displaying toast message in this case when page>limit.
            Toast.makeText(this, "That's all the data..", Toast.LENGTH_SHORT).show();

            // hiding our progress bar.
            loadingPB.setVisibility(View.GONE);
            return;
        }
        // creating a string variable for url .
        String url = "https://api.unsplash.com/photos/?client_id=LccS_JIVqtxRtojaBavkMw7m7Em5daueG7hXmhiboYU&page=" + page+ "&per_page=500";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        // creating a variable for our json object request and passing our url to it.
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    // on below line we are extracting data from our json array.
                 //   JSONArray dataArray =new JSONArray(response);

                    // passing data from our json array in our array list.
                    ArrayList<Photo> itemList = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        // Parse JSON object to Item model
                        Photo item = new Photo();
                        item.setId(jsonObject.getString("id"));
                     //   Urls urls =new Urls();
                        JSONObject detailsObject = jsonObject.getJSONObject("urls");
                        Urls details = new Urls();
                        details.setRegular(detailsObject.getString("regular"));
                      //  details.setPrice(detailsObject.getDouble("price"));

                        item.setUrls(details);

                        itemList.add(item);

                        // on below line we are extracting data from our json object.
                      //  userModalArrayList.add(new UserModal(jsonObject.getString("first_name"), jsonObject.getString("last_name"), jsonObject.getString("email"), jsonObject.getString("avatar")));

                        // passing array list to our adapter class.
                       /* userRVAdapter = new UserRVAdapter(itemList, MainActivity.this);

                        // setting layout manager to our recycler view.
                        userRV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        userRV.setHasFixedSize(true);

                        // setting adapter to our recycler view.
                        userRV.setAdapter(userRVAdapter);*/
                    }

                    photoList.addAll(itemList);

                    // Initialize the adapter if it's null
                    if (userRVAdapter == null) {
                        userRVAdapter = new UserRVAdapter(photoList, MainActivity.this);
                        userRV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        userRV.setHasFixedSize(true);
                        userRV.setAdapter(userRVAdapter);
                    }

                    // Notify adapter that new data has been added
                    userRVAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // handling on error listener method.

                Toast.makeText(MainActivity.this, "Fail to get data..", Toast.LENGTH_SHORT).show();
                loadingPB.setVisibility(View.GONE);
                getDataFromAPI(page, limit);
            }
        });
        // calling a request queue method
        // and passing our json object
        queue.add(jsonObjectRequest);
    }
}
