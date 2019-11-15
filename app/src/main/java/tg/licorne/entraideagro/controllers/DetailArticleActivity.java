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
import tg.licorne.entraideagro.adapter.DetailArticleAdapter;
import tg.licorne.entraideagro.adapter.FermesAdapter;
import tg.licorne.entraideagro.config.ServerConfig;
import tg.licorne.entraideagro.config.VolleySingleton;
import tg.licorne.entraideagro.helper.CircleTransform;
import tg.licorne.entraideagro.helper.DriverItemDecoration;
import tg.licorne.entraideagro.model.ArticleDetail;
import tg.licorne.entraideagro.model.Fermes;

/**
 * Created by Admin on 19/02/2018.
 */

public class DetailArticleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView textViewUser, textViewDate, textViewSujet, textViewContenu;
    private ImageView imageViewAvatar;

    private DetailArticleAdapter detailArticleAdapter;
    private ArticleDetail articleDetail;
    private List<ArticleDetail> articleDetailList = new ArrayList<>();

    private String token;
    private int id;

    private static int id_user_detail;

    String urlDetail= ServerConfig.url_sever+"details";
    String urlArticle= ServerConfig.url_sever +"articles";
    String urlChemin= ServerConfig.url_sever +"articles";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_article_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.idToolbarArticleDetail);
        toolbar.setTitle("Article Detail");
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intentExtrat = getIntent();
        if (intentExtrat != null){
            token = intentExtrat.getStringExtra("TOKEN");
            id = intentExtrat.getIntExtra("ID_ARTICLE", 0);
        }

        initView();

        //detail();
        listePhoto();
    }

    private void initView(){
        recyclerView = findViewById(R.id.idRecycleViewDetaitArticle);
        textViewUser = findViewById(R.id.idTExtViewNomDetailArticle);
        textViewDate = findViewById(R.id.idTextViewDateDetailArticle);
        textViewSujet = findViewById(R.id.idTextViewSujetDetailArticle);
        textViewContenu = findViewById(R.id.idTextViewContenuDetailArticle);
        imageViewAvatar = findViewById(R.id.idImageViewAvatarDetailArticle);
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
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(DetailArticleActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        VolleySingleton.getInstance(DetailArticleActivity.this).addToRequestQueue(stringDetailUser);
    }

    private void listePhoto(){

        urlArticle = ServerConfig.url_sever +"articles/"+id;
        StringRequest stringDetailUser = new StringRequest(Request.Method.GET, urlArticle,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        Log.i("response", response.toString());

                        try {
                            //converting response to json object
                            if (response != null){
                                JSONObject obj = new JSONObject(response);
                                JSONObject resSucces = obj.getJSONObject("success");
                                JSONArray resFichiers= obj.getJSONArray("fichiers");
                                //Log.i("success", resSucces.toString());
                                String date = resSucces.getString("created_at").substring(0,10)+" à "
                                        +resSucces.getString("created_at").substring(11,16);

                                Glide.with(DetailArticleActivity.this)
                                        .load(ServerConfig.url_file +"/uploads/avatars/"+resSucces.getString("avatar"))
                                        .asBitmap()
                                        .transform(new CircleTransform(DetailArticleActivity.this))
                                        .into(imageViewAvatar);
                                textViewUser.setText(resSucces.getString("username"));
                                textViewDate.setText(date);
                                textViewSujet.setText(resSucces.getString("sujet"));
                                textViewContenu.setText(resSucces.getString("contenu"));

                                for (int i=0; i<resFichiers.length(); i++) {
                                    JSONObject proJson =resFichiers.getJSONObject(i);
                                    //Log.i("success", proJson.toString());
                                    articleDetail = new ArticleDetail(
                                            proJson.getInt("id"),
                                            ServerConfig.url_file+""+proJson.getString("chemin")+"/"+proJson.getString("nom_fichier")
                                    );
                                    articleDetailList.add(articleDetail);
                                }

                                detailArticleAdapter = new DetailArticleAdapter(articleDetailList);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DetailArticleActivity.this);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.addItemDecoration(new DriverItemDecoration(DetailArticleActivity.this, DriverItemDecoration.VERTICAL_LIST, 0));
                                recyclerView.setAdapter(detailArticleAdapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(DetailArticleActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        VolleySingleton.getInstance(DetailArticleActivity.this).addToRequestQueue(stringDetailUser);
    }
}
