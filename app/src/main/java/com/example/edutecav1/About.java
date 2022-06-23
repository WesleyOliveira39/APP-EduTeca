package com.example.edutecav1;

import static com.example.edutecav1.WebView.setEndereco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.example.edutecav1.databinding.ActivityAboutBinding;


public class About extends Base {

    ActivityAboutBinding activityAboutBinding;
    CardView myCardView;
    ImageView imgInsta, imgFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAboutBinding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(activityAboutBinding.getRoot());
        alocarTitulo("Sobre");

        loadLocale();

        myCardView = findViewById(R.id.cardQuestionario);
        imgInsta = findViewById(R.id.imgInsta);
        imgFace = findViewById(R.id.imgFace);

        imgInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEndereco("https://www.instagram.com/projeto.eduteca/");
                startActivity(new Intent(About.this, WebView.class));
            }
        });

        imgFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEndereco("https://www.facebook.com/conectandoEduTeca");
                startActivity(new Intent(About.this, WebView.class));
            }
        });


        myCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEndereco("https://docs.google.com/forms/d/e/1FAIpQLSdKo3lgb0N5aN77ookvYsHsKjhS-Y4Dd5zO9vxafRMG1mW7kg/viewform");
                startActivity(new Intent(About.this, WebView.class));
            }
        });
    }
}
