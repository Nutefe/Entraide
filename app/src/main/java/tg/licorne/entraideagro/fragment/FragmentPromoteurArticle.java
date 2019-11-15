package tg.licorne.entraideagro.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import tg.licorne.entraideagro.adapter.DocumentAdapter;
import tg.licorne.entraideagro.config.ServerConfig;
import tg.licorne.entraideagro.config.VolleySingleton;
import tg.licorne.entraideagro.helper.DriverItemDecoration;
import tg.licorne.entraideagro.model.Artilces;
import tg.licorne.entraideagro.model.Documents;

/**
 * Created by Admin on 09/04/2018.
 */

public class FragmentPromoteurArticle extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

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

    public static FragmentPromoteurArticle newInstance(){
        FragmentPromoteurArticle fragment = new FragmentPromoteurArticle();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promoteur_article, container, false);
        initView(view);
        initListner();

        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );


        Bundle bundle = getArguments();
        if (bundle!=null){
            if (bundle.containsKey("TOKEN")){
                token = bundle.getString("TOKEN");
            }
        }

        //detail();
        //getArticles();

        if(checkNetworkConnexion()){
            detail();
            getArticles();
        }

        return view;
    }

    private void initView(View view){
        recyclerView =(RecyclerView) view.findViewById(R.id.idRVFragmentArticle);
        swipeRefreshLayout = view.findViewById(R.id.idSwipeRefreshLayoutArticle);
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
                                Log.i("success", resSucces.toString());
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
                        //Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("error", error.getMessage().toString());
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                String auth= "Bearer "+ token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringDetailUser);
    }

    private void getArticles(){
        artilcesList.clear();
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
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
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
                        //Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringDetailUser);
    }

    public boolean checkNetworkConnexion() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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
                detail();
                getArticles();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 5000);
    }
}
