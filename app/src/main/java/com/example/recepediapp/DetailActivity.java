package com.example.recepediapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Recibir datos enviados desde la actividad principal
        Intent intent = getIntent();
        int imageId = intent.getIntExtra("image_id", 0); // Valor predeterminado en caso de que no se reciba
        String imageName = intent.getStringExtra("image_name");


        ImageView imageViewItem = findViewById(R.id.imageViewItem);
        TextView nameItemText = findViewById(R.id.nameItemText);

        // Configurar la imagen y el nombre en las vistas
        imageViewItem.setImageResource(imageId);
        nameItemText.setText(imageName);

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setText("Volver");
        buttonBack.setOnClickListener(new View.OnClickListener() {
@Override
            public void onClick(View view) {
                // Iniciar la MainActivity
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });










    }
}