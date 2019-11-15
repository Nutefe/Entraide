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
import android.widget.ImageView;
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
import tg.licorne.entraideagro.adapter.FermeAgentAdapter;
import tg.licorne.entraideagro.config.ServerConfig;
import tg.licorne.entraideagro.config.VolleySingleton;
import tg.licorne.entraideagro.helper.DriverItemDecoration;
import tg.licorne.entraideagro.model.Agents;

/**
 * Created by Admin on 18/02/2018.
 */

public class DetailFermeActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imageViewCarte, imageViewPhoto;
    private TextView textViewNom, textViewDimension, textViewEquipe, textViewGerant,
            textViewDesc, textViewTitreDesc, textViewPefecture, textViewCanton, textViewVillage;

    private RecyclerView recyclerView;
    private List<Agents> agentsList = new ArrayList<>();
    private FermeAgentAdapter fermeAgentAdapter;
    private Agents agents;
    private String token;
    private int id_ferme;

    private static double longitude = 0, latitude =0;
    private static String region, prefecture;
    private static String type = "";

    private String urlDetailFerme= ServerConfig.url_sever +"fermes";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_ferme_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.idToolbarDetailFerme);
        toolbar.setTitle("Detail ferme");
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intentExtrat = getIntent();
        if (intentExtrat != null){
            token = intentExtrat.getStringExtra("TOKEN");
            id_ferme = intentExtrat.getIntExtra("ID", 0);
        }
        initView();
        initListener();

        detailFerme();
        agentsFerme();

    }

    private void initView(){
        imageViewCarte = (ImageView) findViewById(R.id.idImageViewCarte);
        imageViewPhoto = findViewById(R.id.idImageViewPhotoDetailFerme);
        textViewNom = findViewById(R.id.idTextViewNomDetailFerme);
        textViewDimension = findViewById(R.id.idTextViewDimensionDetailFerme);
        textViewEquipe = findViewById(R.id.idTextViewEquipeDetailFerme);
        textViewDesc = findViewById(R.id.idTextViewDescriptionFerme);
        textViewTitreDesc = findViewById(R.id.idTextViewTitreDesc);
        textViewPefecture = findViewById(R.id.idTextViewPrefectureDetailFerme);
        textViewCanton = findViewById(R.id.idTextViewCantonDetailFerme);
        textViewVillage = findViewById(R.id.idTextViewVillageDetailFerme);
        recyclerView = findViewById(R.id.idRecycleViewFermeDetail);
    }

    private void initListener(){
        imageViewCarte.setOnClickListener(this);
    }

    private void detailFerme(){

        urlDetailFerme= ServerConfig.url_sever +"fermes/"+id_ferme;

        StringRequest stringDetailUser = new StringRequest(Request.Method.GET, urlDetailFerme,
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
                                textViewNom.setText(resSucces.getString("nom_ferme"));
                                textViewDimension.setText(resSucces.getString("dimensions")+" ha");
                                textViewEquipe.setText(resSucces.getString("nom_equipe"));
                                textViewPefecture.setText(resSucces.getString("prefecture"));
                                textViewCanton.setText(resSucces.getString("canton"));
                                textViewVillage.setText(resSucces.getString("village_proche"));
                                Glide.with(DetailFermeActivity.this)
                                        .load(ServerConfig.url_file +"/"+resSucces.getString("chemin")+""+resSucces.getString("image"))
                                        .asBitmap()
                                        .into(imageViewPhoto);

                                if (!resSucces.getString("description").equals("null")){
                                    textViewDesc.setText(resSucces.getString("description"));
                                } else {
                                    textViewTitreDesc.setVisibility(View.GONE);
                                    textViewDesc.setVisibility(View.GONE);
                                }

                                longitude = resSucces.getDouble("longitude");
                                latitude = resSucces.getDouble("latitude");
                                region = resSucces.getString("region");
                                prefecture = resSucces.getString("prefecture");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(DetailFermeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
    private void agentsFerme(){

        urlDetailFerme= ServerConfig.url_sever +"fermes/"+id_ferme;

        StringRequest stringDetailUser = new StringRequest(Request.Method.GET, urlDetailFerme,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        Log.i("response", response.toString());

                        try {
                            //converting response to json object
                            if (response != null){
                                JSONObject obj = new JSONObject(response);
                                JSONArray resSucces = obj.getJSONArray("membres");
                                //Log.i("success", resSucces.toString());
                                for (int i=0; i<resSucces.length(); i++) {
                                    JSONObject proJson =resSucces.getJSONObject(i);
                                    //Log.i("sec", proJson.toString());
                                    if (proJson.getInt("gerant")!=0){
                                        type = "gerant";
                                    } else if (proJson.getInt("respo_pv")!=0){
                                        type = "responsable production végétal";
                                    } else if (proJson.getInt("respo_pa")!=0){
                                        type = "responsable production animal";
                                    } else if (proJson.getInt("respo_ma")!=0){
                                        type = "responsable production m";
                                    }else if (proJson.getInt("ouvrier")!=0){
                                        type = "ouvrier";
                                    } else {
                                        type="";
                                    }
                                    agents = new Agents(
                                            proJson.getString("nom_agent")+" "+ proJson.getString("prenom_agent"),
                                            type,
                                            proJson.getString("nom_agent").substring(0,1)
                                    );
                                    agentsList.add(agents);
                                }

                                fermeAgentAdapter = new FermeAgentAdapter(agentsList);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.addItemDecoration(new DriverItemDecoration(DetailFermeActivity.this, DriverItemDecoration.VERTICAL_LIST, 0));
                                recyclerView.setAdapter(fermeAgentAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(DetailFermeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.idImageViewCarte:
                Intent intent= new Intent(DetailFermeActivity.this, MapsActivity.class);
                intent.putExtra("LONGITUDE", longitude);
                intent.putExtra("LATITUDE", latitude);
                intent.putExtra("REGION", region);
                intent.putExtra("PREFECTURE", prefecture);
                startActivity(intent);
                break;
        }
    }
}
