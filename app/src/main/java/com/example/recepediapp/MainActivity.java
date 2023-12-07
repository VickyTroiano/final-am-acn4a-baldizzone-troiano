package com.example.recepediapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);

        GridLayout gridLayout = findViewById(R.id.gridLayout);

        int[] images = {
                R.drawable.empanada,
                R.drawable.pasta,
                R.drawable.arrollado,
                R.drawable.guacamole,
                R.drawable.omellette,
                R.drawable.panqueque,
                R.drawable.guiso,
                R.drawable.desayuno,
                R.drawable.cheesecake,
                R.drawable.papas
        };

        String[] titles = {
                "Empanada",
                "Pasta",
                "Arrollado",
                "Guacamole",
                "Omellette",
                "Panqueque",
                "Guiso",
                "Desayuno",
                "Cheesecake",
                "Papas"
        };

        // Creo un image view por cada imagen iterada y le agrego la imagen
        for (int i = 0; i < images.length; i++) {
            // Contenedor para cada imagen
            LinearLayout container = new LinearLayout(this);
            container.setOrientation(LinearLayout.VERTICAL);

            ImageView imageView = new ImageView(this);
            imageView.setImageResource(images[i]);

            // Obtengo el nombre de la imagen
            String imageName = getResources().getResourceEntryName(images[i]);
            imageView.setContentDescription("Imagen de" + imageName);

            // Personalizamos en gridlayout
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 500;
            params.height = 500;
            params.setMargins(8, 8, 8, 8);
            imageView.setLayoutParams(params);

            // Título de cada comida
            TextView textView = new TextView(this);
            textView.setText(titles[i]);

            // Cargar la fuente personalizada desde la carpeta res/font
            Typeface typeface = ResourcesCompat.getFont(this, R.font.lato_bold_italic);
            textView.setTypeface(typeface, Typeface.BOLD);
            textView.setTextSize(22);
            int textColor = getResources().getColor(R.color.black);
            textView.setTextColor(textColor);

            textView.setPadding(0, 0, 0, 8);

            // Boton
            Button buttonOpen = new Button(this);
            // Aplicar el fondo redondeado
            buttonOpen.setBackgroundResource(R.drawable.boton_redondeado);
            buttonOpen.setId(i);

            buttonOpen.setText("Ver receta");

            // LinearLayout para el botón
            LinearLayout buttonContainer = new LinearLayout(this);
            LinearLayout.LayoutParams paramsBtn = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            );
            paramsBtn.setMargins(5, 0, 5, 20);
            paramsBtn.width = 200;

            buttonOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int buttonId = buttonOpen.getId();
                    String imageName = titles[buttonId];
                    int imageId = images[buttonId];

                    // Iniciar la nueva actividad y enviar datos
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("image_id", imageId);
                    intent.putExtra("image_name", imageName);
                    startActivity(intent);
                }
            });

            // Agregar la imagen y el texto al contenedor lineal
            container.addView(imageView);
            container.addView(textView);
            container.addView(buttonOpen, paramsBtn);

            // Agregar el contenedor al GridLayout
            gridLayout.addView(container);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            String email = currentUser.getEmail();
            Log.i("firebase email", email);
        } else {
            Log.i("firebase", "NO hay un usuario. Hay que loguearse");
            //redireccion al login activity
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            //this.login("victoria.troiano@davinci.edu.ar", "vicky123");
        }
    }

    public void logout(View v){
        mAuth.signOut();

    }
}
