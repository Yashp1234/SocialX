package com.example.newsfeed;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final String url = "https://newsdata.io/api/1/news?apikey=pub_454829fd0696320a7dabd379e8ed7944b77d&language=en";

    private Toolbar toolbar;
    private List<News> newsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private JsonObjectRequest jsonObjectRequest;
    private EditText editText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolBar();
        getData();

        editText = findViewById(R.id.editText);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NewsAdapter(this,newsList);
        recyclerView.setAdapter(adapter);
        adapter.notifyItemInserted(newsList.size());
        adapter.notifyDataSetChanged();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }
    void filter(String text){
        List<News> temp = new ArrayList();
        for(News d: newsList){

            if(d.getTitle().toLowerCase().contains(text.toLowerCase())
                    || d.getDescription().toLowerCase().contains(text.toLowerCase())
                    || d.getSource().toLowerCase().contains(text.toLowerCase()))
            {
                temp.add(d);
            }
        }
        adapter.updateList(temp);
    }
    private void getData() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                JSONArray newsArray = null;
                try {
                    newsArray = response.getJSONArray("results");
                    for (int i = 0;i < newsArray.length();i++){
                        JSONObject newsObj = null;
                        try {
                            newsObj = newsArray.getJSONObject(i);
                            String source = newsObj.getString("source_id");
                            String title = newsObj.getString("title");
                            String description = newsObj.getString("description");
                            String publishedAt = newsObj.getString("pubDate").substring(0,10);

                            String imageUrl = newsObj.getString("image_url");

                            News news = new News(publishedAt,source.toUpperCase(),title,description,imageUrl);
                            newsList.add(news);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "error in networking..", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getmInstance(this).addToRequestQueue(jsonObjectRequest);
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_logout)
                .setTitle("Log out")
                .setMessage("Are you sure you want to Log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    private void setToolBar() {
        toolbar = findViewById(R.id.idToolbarMain);
        setSupportActionBar(toolbar);
    }

}