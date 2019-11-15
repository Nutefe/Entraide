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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.adapter.DetailRapportAdapter;
import tg.licorne.entraideagro.adapter.FermeAgentAdapter;
import tg.licorne.entraideagro.config.ServerConfig;
import tg.licorne.entraideagro.config.VolleySingleton;
import tg.licorne.entraideagro.helper.DriverItemDecoration;
import tg.licorne.entraideagro.model.Agents;
import tg.licorne.entraideagro.model.RapportDetail;

/**
 * Created by Admin on 18/02/2018.
 */

public class DetailRapportActivity extends AppCompatActivity {

    private TextView textViewContenu, textViewObjet, textViewDate;

    private RecyclerView recyclerView;
    private List<RapportDetail> rapportDetails = new ArrayList<>();
    private DetailRapportAdapter detailRapportAdapter;
    private RapportDetail rapportDetail;

    private String urlDetailRapport= ServerConfig.url_sever +"rapport/detail/";
    private int id_rapport;
    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_rapport_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.idToolbarDetailRapport);
        toolbar.setTitle("Détail Rapport");
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intentExtrat = getIntent();
        if (intentExtrat != null){
            token = intentExtrat.getStringExtra("TOKEN");
            id_rapport = intentExtrat.getIntExtra("ID_RAPPORT", 0);
        }

        initView();

        Toast.makeText(this, String.valueOf(id_rapport), Toast.LENGTH_SHORT).show();

        detailRapport();
        imageRapport();
    }

    private void initView(){
        textViewObjet = findViewById(R.id.idTextViewObjetDetailRapport);
        textViewContenu = findViewById(R.id.idTextViewContenuDetailRapport);
        textViewDate = findViewById(R.id.idTextViewDateDetailRapport);
        recyclerView = findViewById(R.id.idRecycleViewDetailRapportFile);
    }

    private void detailRapport(){

        urlDetailRapport= ServerConfig.url_sever +"rapport/detail/"+id_rapport;

        StringRequest stringDetailUser = new StringRequest(Request.Method.GET, urlDetailRapport,
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

                                textViewObjet.setText(resSucces.getString("objet"));
                                textViewContenu.setText(resSucces.getString("contenu"));
                                textViewDate.setText(resSucces.getString("created_at").substring(0,10));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(DetailRapportActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
    private void imageRapport(){
        urlDetailRapport= ServerConfig.url_sever +"rapport/detail/"+id_rapport;
        StringRequest stringDetailUser = new StringRequest(Request.Method.GET, urlDetailRapport,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        Log.i("response", response.toString());

                        try {
                            //converting response to json object
                            if (response != null){
                                JSONObject obj = new JSONObject(response);
                                JSONArray resSucces = obj.getJSONArray("fichiers");
                                //Log.i("success", resSucces.toString());
                                for (int i=0; i<resSucces.length(); i++) {
                                    JSONObject proJson =resSucces.getJSONObject(i);
                                    //Log.i("sec", proJson.toString());

                                    rapportDetail = new RapportDetail(
                                            proJson.getInt("id"),
                                            ServerConfig.url_file+""+proJson.getString("chemin")+"/"+proJson.getString("nom_fichier"),
                                            proJson.getString("nom_fichier"),
                                            token
                                    );
                                    rapportDetails.add(rapportDetail);
                                }

                                detailRapportAdapter = new DetailRapportAdapter(rapportDetails);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                //recyclerView.addItemDecoration(new DriverItemDecoration(DetailRapportActivity.this, DriverItemDecoration.VERTICAL_LIST, DriverItemDecoration.VERTICAL_LIST));
                                recyclerView.setAdapter(detailRapportAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(DetailRapportActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
