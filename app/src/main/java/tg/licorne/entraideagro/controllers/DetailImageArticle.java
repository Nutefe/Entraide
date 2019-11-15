package tg.licorne.entraideagro.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import tg.licorne.entraideagro.R;

public class DetailImageArticle extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_image_article);

        Toolbar toolbar = (Toolbar) findViewById(R.id.idToolbarDetailImage);
        toolbar.setTitle("Détail image");
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView imageView = findViewById(R.id.detailImageArticle);
        String path_image;

        Intent intentExtrat = getIntent();
        if (intentExtrat != null){
            path_image = intentExtrat.getStringExtra("PATH_IMAGE");
            Glide.with(this)
                    .load(path_image)
                    .asBitmap()
                    .into(imageView);
        }
    }
}
