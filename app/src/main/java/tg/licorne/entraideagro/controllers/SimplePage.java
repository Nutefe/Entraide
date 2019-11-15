package tg.licorne.entraideagro.controllers;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.view.Menu;
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
import tg.licorne.entraideagro.adapter.ArticlesAdapter;
import tg.licorne.entraideagro.config.ServerConfig;
import tg.licorne.entraideagro.config.VolleySingleton;
import tg.licorne.entraideagro.model.Artilces;

public class SimplePage extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private List<Artilces> artilcesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ArticlesAdapter articlesAdapter;
    private Artilces artilces;
    private BottomNavigationView bottomNavigationView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static int id_user_detail;
    private String token, type_compte;

    String urlDetail= ServerConfig.url_sever +"details";
    String urlArticle= ServerConfig.url_sever +"articles";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.idToolbarDashboardSimple);
        toolbar.setLogo(getResources().getDrawable(R.mipmap.ic_entraide_round));
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        initView();
        initListner();

        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        if(checkNetworkConnexion()){
            getArticles();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.simple_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.idItemConnecter:
                startConnectActivity();
                break;
                case R.id.idItemEntraide:
                startEntraideActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startConnectActivity(){
        Intent intent = new Intent(SimplePage.this, LoginActivity.class);
        startActivity(intent);
    }
    private void startEntraideActivity(){
        Intent intent = new Intent(SimplePage.this, EntraideActivity.class);
        startActivity(intent);
    }

    private void initView(){
        recyclerView =findViewById(R.id.idRVFragmentArticle);
        swipeRefreshLayout = findViewById(R.id.idSwipeRefreshLayoutArticle);
    }

    private void initListner(){
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void getArticles(){
        StringRequest stringDetailUser = new StringRequest(Request.Method.GET, urlArticle,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        Log.i("response", response.toString());

                        try {
                            //converting response to json object
                            if (response != null){
                                JSONObject obj = new JSONObject(response);
                                JSONArray resSucces = obj.getJSONArray("success");
                                Log.i("success", resSucces.toString());
                                artilcesList.clear();

                                for (int i = 0; i<resSucces.length(); i++){
                                    JSONObject articleObjet = resSucces.getJSONObject(i);
                                    artilces = new Artilces(
                                            articleObjet.getInt("id"),
                                            articleObjet.getString("username"),
                                            ServerConfig.url_file +"/uploads/avatars/"+articleObjet.getString("avatar"),
                                            articleObjet.getString("sujet"),
                                            articleObjet.getString("contenu"),
                                            articleObjet.getString("created_at").substring(0,10),
                                            "     à "+articleObjet.getString("created_at").substring(11,16),
                                            ServerConfig.url_file+""+articleObjet.getString("image"),
                                            articleObjet.getInt("commentaires"),
                                            token,
                                            id_user_detail
                                    );
                                    artilcesList.add(artilces);
                                }

                                articlesAdapter = new ArticlesAdapter(artilcesList);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SimplePage.this);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(articlesAdapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(SimplePage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("error", error.getMessage().toString());
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(stringDetailUser);
    }

    public boolean checkNetworkConnexion() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            /*final android.net.NetworkInfo wifi = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            final android.net.NetworkInfo mobile = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);*/

            return (networkInfo != null && networkInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getArticles();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 5000);
    }
}
