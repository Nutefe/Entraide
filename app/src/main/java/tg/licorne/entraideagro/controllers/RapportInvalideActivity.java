package tg.licorne.entraideagro.controllers;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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
import tg.licorne.entraideagro.config.ServerConfig;
import tg.licorne.entraideagro.config.VolleySingleton;
import tg.licorne.entraideagro.helper.BottomNavigationViewHelper;
import tg.licorne.entraideagro.helper.DriverItemDecoration;
import tg.licorne.entraideagro.adapter.RapportsAdapter;
import tg.licorne.entraideagro.model.Rapports;

/**
 * Created by Admin on 18/02/2018.
 */

public class RapportInvalideActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton floatingActionButton;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RapportsAdapter rapportsAdapter;
    private List<Rapports> rapportsList = new ArrayList<>();
    private Rapports rapport;

    private String token;
    private int id_role;
        String  ip = "192.168.43.123";
        String port = "80";
//    String  ip = "10.0.2.2";
//    String port = "8000";
    String urlDetail= ServerConfig.url_sever +"details";
    String urlGetRapport= ServerConfig.url_sever +"rapport/send/";

    private static int id_user_detail, zone_id =0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rapport_invalide_layout);

        Toolbar toolbar = findViewById(R.id.idToolbarRapportInvalide);
        toolbar.setLogo(getResources().getDrawable(R.mipmap.ic_entraide_round));
        toolbar.setTitle("Rapport Envoyer");
        setSupportActionBar(toolbar);

        Intent intentExtrat = getIntent();
        if (intentExtrat != null){
            token = intentExtrat.getStringExtra("TOKEN");
        }

        initView();
        initListener();

        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        detail();
    }



    private void initView(){
        recyclerView =  findViewById(R.id.idRecycleViewRapportInvalide);
        floatingActionButton = findViewById(R.id.idFloatingActionButtonRapportInvalide);
        swipeRefreshLayout = findViewById(R.id.idSwipeRefreshLayoutInvalide);
    }

    private void initListener(){
        floatingActionButton.setOnClickListener(this);
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
                                zone_id = resSucces.getInt("zone_id");
                                //Log.i("id", String.valueOf(id_user_detail));
                                listRapport();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(RapportInvalideActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        VolleySingleton.getInstance(RapportInvalideActivity.this).addToRequestQueue(stringDetailUser);
    }

    private void listRapport(){
        urlGetRapport = ServerConfig.url_sever +"rapport/send/"+id_user_detail;
        rapportsList.clear();
        StringRequest stringDetailUser = new StringRequest(Request.Method.GET, urlGetRapport,
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
                                    rapport = new Rapports(
                                            proJson.getInt("id"),
                                            proJson.getString("objet"),
                                            proJson.getString("created_at").substring(0,10),
                                            token
                                    );
                                    rapportsList.add(rapport);
                                }
                                rapportsAdapter = new RapportsAdapter(rapportsList);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.addItemDecoration(new DriverItemDecoration(RapportInvalideActivity.this, DriverItemDecoration.VERTICAL_LIST, 0));
                                recyclerView.setAdapter(rapportsAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(RapportInvalideActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        VolleySingleton.getInstance(RapportInvalideActivity.this).addToRequestQueue(stringDetailUser);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.idFloatingActionButtonRapportInvalide:
                Intent intent = new Intent(RapportInvalideActivity.this, EditeRapportActivity.class);
                intent.putExtra("TOKEN", token);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //listRapport();
                detail();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 5000);
    }
}
