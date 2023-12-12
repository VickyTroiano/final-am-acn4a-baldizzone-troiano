package com.example.recepediapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Recibir datos enviados desde la actividad principal
        Intent intent = getIntent();
        String imageId = intent.getStringExtra("image_id");
        String imageName = intent.getStringExtra("image_name");
        String imageProcedure = intent.getStringExtra("image_procedure");

        ImageView imageViewItem = findViewById(R.id.imageViewItem);
        TextView nameItemText = findViewById(R.id.nameItemText);
        TextView preparationText = findViewById(R.id.preparationText);

        // Configurar la imagen y el nombre en las vistas utilizando Picasso
        Picasso.get().load(imageId).into(imageViewItem);
        nameItemText.setText(imageName);
        preparationText.setText(imageProcedure);

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
