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
import tg.licorne.entraideagro.adapter.FermesAdapter;
import tg.licorne.entraideagro.config.ServerConfig;
import tg.licorne.entraideagro.config.VolleySingleton;
import tg.licorne.entraideagro.helper.DriverItemDecoration;
import tg.licorne.entraideagro.model.Fermes;

/**
 * Created by Admin on 09/04/2018.
 */

public class FragmentPromoteurFerme extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private List<Fermes> fermesList = new ArrayList<>();
    private FermesAdapter fermeAdapter;
    private Fermes fermes;
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String urlFerme= ServerConfig.url_sever +"fermes";
    private String token;

    public static FragmentPromoteurFerme newInstance(){
        FragmentPromoteurFerme fragment = new FragmentPromoteurFerme();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promoteur_ferme, container, false);
        initView(view);
        initListner();

        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        Bundle bundle = getArguments();
        if (bundle!=null){
            if (bundle.containsKey("TOKEN") || bundle.containsKey("ID_ROLE")){
                token = bundle.getString("TOKEN");
            }
        }

        if (checkNetworkConnexion()){
            listeFermes();
        }

        return view;
    }

    private void initView(View view){
        recyclerView =(RecyclerView) view.findViewById(R.id.idRVFragmentFerme);
        swipeRefreshLayout = view.findViewById(R.id.idSwipeRefreshLayoutFerme);
    }

    private void initListner(){
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void listeFermes(){

        StringRequest stringDetailUser = new StringRequest(Request.Method.GET, urlFerme,
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

                                for (int i=0; i<resSucces.length(); i++) {
                                    JSONObject proJson =resSucces.getJSONObject(i);
                                    //Log.i("success", proJson.toString());
                                    fermes = new Fermes(
                                            proJson.getInt("id"),
                                            proJson.getString("nom_ferme"),
                                            proJson.getString("dimensions"),
                                            proJson.getString("nom_equipe"),
                                            proJson.getString("region"),
                                            ServerConfig.url_file +"/"+
                                                    proJson.getString("chemin")+""+
                                                    proJson.getString("image"),
                                            token
                                    );
                                    fermesList.add(fermes);
                                }

                                fermeAdapter = new FermesAdapter(fermesList);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                if (getContext()!=null){
                                    recyclerView.addItemDecoration(new DriverItemDecoration(getContext(), DriverItemDecoration.VERTICAL_LIST, 110));
                                }
                                recyclerView.setAdapter(fermeAdapter);

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
                listeFermes();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 5000);
    }
}
