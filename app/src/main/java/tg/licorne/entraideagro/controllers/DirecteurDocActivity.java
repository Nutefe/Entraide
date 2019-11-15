package tg.licorne.entraideagro.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.adapter.DocumentAdapter;
import tg.licorne.entraideagro.config.ServerConfig;
import tg.licorne.entraideagro.config.VolleySingleton;
import tg.licorne.entraideagro.helper.DriverItemDecoration;
import tg.licorne.entraideagro.model.Documents;

public class DirecteurDocActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private DocumentAdapter documentAdapter;
    private List<Documents> documentsList = new ArrayList<>();

    private int id_user_detail, id_role;
    private String token, type_compte;

    String urlDetail= ServerConfig.url_sever +"details";
    String urlDoc= ServerConfig.url_sever +"documents";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directeur_doc_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.idToolbarDocument);
        toolbar.setTitle("Entraide Agro");
        setSupportActionBar(toolbar);

        initView();
        initListner();

        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        Intent intentExtrat = getIntent();
        if (intentExtrat != null){
            token = intentExtrat.getStringExtra("TOKEN");
        }

        detail();
    }

    private void initView(){
        recyclerView = findViewById(R.id.idRecycleViewDocumentCoor);
        swipeRefreshLayout = findViewById(R.id.idSwipeRefreshLayoutDocument);
    }

    private void initListner(){
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void detail(){
        StringRequest stringDetailUser = new StringRequest(Request.Method.GET, urlDetail,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        Log.i("response", response.toString());

                        try {
                            //converting response to json object
                            if (response != null){
                                JSONObject obj = new JSONObject(response);
                                JSONObject resSucces = obj.getJSONObject("success");
                                //Log.i("success", resSucces.toString());
                                id_user_detail = resSucces.getInt("id");
                                type_compte = resSucces.getString("type_compte");
                                //Log.i("id", String.valueOf(id_user_detail));

                                documents();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(DirecteurDocActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("error", error.getMessage().toString());
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // headers.put("Content-Type", "application/json");
                String auth= "Bearer "+ token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringDetailUser);
    }

    private void documents(){
        documentsList.clear();
        urlDoc= ServerConfig.url_sever +"documents/all/"+id_user_detail;
        StringRequest stringDetailUser = new StringRequest(Request.Method.GET, urlDoc,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        Log.i("response", response.toString());

                        try {
                            //converting response to json object
                            if (response != null){
                                JSONObject obj = new JSONObject(response);
                                JSONArray resSucces = obj.getJSONArray("success");
                                //Log.i("success", resSucces.toString());

                                for (int i = 0; i<resSucces.length(); i++){
                                    JSONObject docObjet = resSucces.getJSONObject(i);
                                    //Log.i("doc", docObjet.toString());
                                    Documents documents = new Documents(
                                            docObjet.getString("nom_fichier"),
                                            docObjet.getString("description")
                                    );
                                    documentsList.add(documents);
                                }
                                documentAdapter = new DocumentAdapter(documentsList);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DirecteurDocActivity.this);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.addItemDecoration(new DriverItemDecoration(DirecteurDocActivity.this, DriverItemDecoration.VERTICAL_LIST, 0));
                                recyclerView.setAdapter(documentAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(DirecteurDocActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("error", error.getMessage().toString());
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // headers.put("Content-Type", "application/json");
                String auth= "Bearer "+ token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringDetailUser);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                detail();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 5000);
    }

    @Override
    public void onClick(View view) {

    }
}
