package tg.licorne.entraideagro.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.adapter.ActivitesAdapter;
import tg.licorne.entraideagro.config.ServerConfig;
import tg.licorne.entraideagro.config.VolleySingleton;
import tg.licorne.entraideagro.model.Activites;

/**
 * Created by Admin on 17/02/2018.
 */

public class ActivitesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewActivite;
    private Activites activites;
    List<Activites> activitesList = new ArrayList<>();
    ActivitesAdapter activitesAdapter;

    private String token;
    private int id_user;

    String  ip = "192.168.43.123";
    String port = "80";
//    String  ip = "10.0.2.2";
//    String port = "8000";

    String urlDetail= ServerConfig.url_sever+"details";
    String urlActivite= ServerConfig.url_sever +"activites";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activite_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.idToolbarActivite);
        toolbar.setTitle("Activites");
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intentExtrat = getIntent();
        if (intentExtrat != null){
            token = intentExtrat.getStringExtra("TOKEN");
            id_user = intentExtrat.getIntExtra("ID_USER", 0);
        }

        initView();

        getActivites();

    }

    private void initView(){
        recyclerViewActivite = (RecyclerView) findViewById(R.id.idRecyclerViewActivite);
    }

    private void getActivites(){

        urlActivite = ServerConfig.url_sever +"activites/"+id_user;

        StringRequest stringDetailUser = new StringRequest(Request.Method.GET, urlActivite,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        Log.i("response", response.toString());

                        try {
                            //converting response to json object
                            if (response != null){
                                JSONObject obj = new JSONObject(response);
                                JSONArray resSucces = obj.getJSONArray("success");
                                //String res = obj.getString("success");
                                //Log.i("success", resSucces.toString());
                                for (int i=0; i<resSucces.length(); i++) {
                                    JSONObject proJson =resSucces.getJSONObject(i);
                                    //Log.i("sec", proJson.toString());

                                    activites = new Activites(
                                            proJson.getString("nom_activite"),
                                            proJson.getString("description")
                                    );
                                    activitesList.add(activites);
                                }

                                activitesAdapter = new ActivitesAdapter(activitesList);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerViewActivite.setLayoutManager(layoutManager);
                                recyclerViewActivite.setItemAnimator(new DefaultItemAnimator());
                                recyclerViewActivite.setAdapter(activitesAdapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(ActivitesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
}
