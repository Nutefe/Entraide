package tg.licorne.entraideagro.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.config.VolleySingleton;
import tg.licorne.entraideagro.fragment.FragmentAgentAcceuil;
import tg.licorne.entraideagro.fragment.FragmentAgentDoccument;
import tg.licorne.entraideagro.fragment.FragmentAgentProfile;
import tg.licorne.entraideagro.fragment.FragmentAgentRapport;
import tg.licorne.entraideagro.fragment.FragmentPromoteurProfile;
import tg.licorne.entraideagro.helper.BottomNavigationViewBehavior;
import tg.licorne.entraideagro.helper.BottomNavigationViewHelper;

/**
 * Created by Admin on 09/04/2018.
 */

public class DashboardAgentActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private String token;
    private int id_role;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_agent_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.idToolbarDashboardAgent);
        toolbar.setLogo(getResources().getDrawable(R.mipmap.ic_entraide_round));
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        initView();

        Intent intentExtrat = getIntent();
        if (intentExtrat != null){
            token = intentExtrat.getStringExtra("TOKEN");
            id_role = intentExtrat.getIntExtra("ID_ROLE", 0);
        }

        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.idItemAcceuil:
                        selectedFragment = new FragmentAgentAcceuil();
                        Bundle bundleAcceuil = new Bundle();
                        bundleAcceuil.putString("TOKEN", token);
                        bundleAcceuil.putInt("ID_ROLE", id_role);
                        selectedFragment.setArguments(bundleAcceuil);
                        break;
                    case R.id.idItemRapport:
                        selectedFragment =new  FragmentAgentRapport();
                        Bundle bundleRapport = new Bundle();
                        bundleRapport.putString("TOKEN", token);
                        bundleRapport.putInt("ID_ROLE", id_role);
                        selectedFragment.setArguments(bundleRapport);
                        break;
                    case R.id.idItemDocument:
                        selectedFragment = new FragmentAgentDoccument();
                        Bundle bundleDocument = new Bundle();
                        bundleDocument.putString("TOKEN", token);
                        bundleDocument.putInt("ID_ROLE", id_role);
                        selectedFragment.setArguments(bundleDocument);
                        break;
                    case R.id.idItemProfile:
                        selectedFragment = new FragmentAgentProfile();
                        Bundle bundleProfile = new Bundle();
                        bundleProfile.putString("TOKEN", token);
                        bundleProfile.putInt("ID_ROLE", id_role);
                        selectedFragment.setArguments(bundleProfile);
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.idFrameLayoutAgent, selectedFragment).addToBackStack("tag");
                transaction.commit();
                return true;
            }
        });

        FragmentAgentAcceuil fragmentAgentAcceuil = new FragmentAgentAcceuil();
        Bundle bundle = new Bundle();
        bundle.putString("TOKEN", token);
        bundle.putInt("ID_ROLE", id_role);
        fragmentAgentAcceuil.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.idFrameLayoutAgent, fragmentAgentAcceuil);
        transaction.commit();
    }

    private void initView(){
        bottomNavigationView = findViewById(R.id.idBottomNavigationAgent);
    }

    /*@Override
    public void onBackPressed() {
        //super.onBackPressed();
        FragmentAgentAcceuil fragmentAgentAcceuil = new FragmentAgentAcceuil();
        Bundle bundle = new Bundle();
        bundle.putString("TOKEN", token);
        bundle.putInt("ID_ROLE", id_role);
        fragmentAgentAcceuil.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.idFrameLayoutAgent, fragmentAgentAcceuil);
        transaction.commit();
    }*/
}
