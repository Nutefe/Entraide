package tg.licorne.entraideagro.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.fragment.FragmentAcceuilDirecteur;
import tg.licorne.entraideagro.fragment.FragmentAgentRapport;
import tg.licorne.entraideagro.fragment.FragmentCoordinateurAcceuil;
import tg.licorne.entraideagro.fragment.FragmentDirecteurRapport;
import tg.licorne.entraideagro.fragment.FragmentPromoteurFerme;
import tg.licorne.entraideagro.fragment.FragmentPromoteurProfile;
import tg.licorne.entraideagro.helper.BottomNavigationViewHelper;

public class DashboardDirecteur extends AppCompatActivity {
    private String token;
    private int id_role;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_directeur);

        Toolbar toolbar = (Toolbar) findViewById(R.id.idToolbarDashboardDirecteur);
        toolbar.setLogo(getResources().getDrawable(R.mipmap.ic_entraide_round));
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        Intent intentExtrat = getIntent();
        if (intentExtrat != null){
            token = intentExtrat.getStringExtra("TOKEN");
            id_role = intentExtrat.getIntExtra("ID_ROLE", 0);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.idBottomNavigationDirecteur);

        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.idItemAcceuilCoor:
                        selectedFragment = new FragmentAcceuilDirecteur();
                        Bundle bundleAcceuil = new Bundle();
                        bundleAcceuil.putString("TOKEN", token);
                        bundleAcceuil.putInt("ID_ROLE", id_role);
                        selectedFragment.setArguments(bundleAcceuil);
                        break;
                    case R.id.idItemFermeCoor:
                        selectedFragment = new FragmentPromoteurFerme();
                        Bundle bundleFerme = new Bundle();
                        bundleFerme.putString("TOKEN", token);
                        bundleFerme.putInt("ID_ROLE", id_role);
                        selectedFragment.setArguments(bundleFerme);
                        break;
                    case R.id.idItemRapportCoor:
                        selectedFragment = new FragmentDirecteurRapport();
                        Bundle bundleRapport = new Bundle();
                        bundleRapport.putString("TOKEN", token);
                        bundleRapport.putInt("ID_ROLE", id_role);
                        selectedFragment.setArguments(bundleRapport);
                        break;
                    case R.id.idItemProfileCoor:
                        selectedFragment = new FragmentPromoteurProfile();
                        Bundle bundleProfil = new Bundle();
                        bundleProfil.putString("TOKEN", token);
                        bundleProfil.putInt("ID_ROLE", id_role);
                        selectedFragment.setArguments(bundleProfil);
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.idFrameLayoutDirecteur, selectedFragment).addToBackStack("tag");
                transaction.commit();
                return true;
            }
        });

        FragmentAcceuilDirecteur fragmentAcceuilDirecteur = new FragmentAcceuilDirecteur();
        Bundle bundle = new Bundle();
        bundle.putString("TOKEN", token);
        bundle.putInt("ID_ROLE", id_role);
        fragmentAcceuilDirecteur.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.idFrameLayoutDirecteur, fragmentAcceuilDirecteur);
        transaction.commit();
    }
}
