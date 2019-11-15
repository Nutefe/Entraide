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
import android.widget.Toast;

import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.fragment.FragmentPromoteurArticle;
import tg.licorne.entraideagro.fragment.FragmentPromoteurFerme;
import tg.licorne.entraideagro.fragment.FragmentPromoteurProfile;
import tg.licorne.entraideagro.helper.BottomNavigationViewHelper;

/**
 * Created by Admin on 09/04/2018.
 */

public class DashboardPromoteurActivity extends AppCompatActivity {

    private String token;
    private int id_role;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_promoteur_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.idToolbarDashboardPromoteur);
        toolbar.setLogo(getResources().getDrawable(R.mipmap.ic_entraide_round));
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        Intent intentExtrat = getIntent();
        if (intentExtrat != null){
            token = intentExtrat.getStringExtra("TOKEN");
            id_role = intentExtrat.getIntExtra("ID_ROLE", 0);
        }

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.idBottomNavigationPromoteur);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.idItemArticle:
                        selectedFragment = new FragmentPromoteurArticle();
                        Bundle bundleArticle = new Bundle();
                        bundleArticle.putString("TOKEN", token);
                        bundleArticle.putInt("ID_ROLE", id_role);
                        selectedFragment.setArguments(bundleArticle);
                        break;
                    case R.id.idItemFerme:
                        selectedFragment = new FragmentPromoteurFerme();
                        Bundle bundleFerme = new Bundle();
                        bundleFerme.putString("TOKEN", token);
                        bundleFerme.putInt("ID_ROLE", id_role);
                        selectedFragment.setArguments(bundleFerme);
                        break;
                    case R.id.idItemProfileUser:
                        selectedFragment = new FragmentPromoteurProfile();
                        Bundle bundleProfil = new Bundle();
                        bundleProfil.putString("TOKEN", token);
                        bundleProfil.putInt("ID_ROLE", id_role);
                        selectedFragment.setArguments(bundleProfil);
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.idFrameLayoutPromoteur, selectedFragment).addToBackStack("tag");
                transaction.commit();
                return true;
            }
        });

        FragmentPromoteurArticle fragmentPromoteurArticle = new FragmentPromoteurArticle();
        Bundle bundle = new Bundle();
        bundle.putString("TOKEN", token);
        bundle.putInt("ID_ROLE", id_role);
        fragmentPromoteurArticle.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.idFrameLayoutPromoteur, fragmentPromoteurArticle);
        transaction.commit();
    }
}
