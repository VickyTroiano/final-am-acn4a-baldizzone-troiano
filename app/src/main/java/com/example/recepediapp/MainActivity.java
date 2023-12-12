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



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);

        GridLayout gridLayout = findViewById(R.id.gridLayout);

        //obtengo todas las recetas
        db.collection("recetas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nombre = document.getString("nombre");
                                String img = document.getString("img");
                                String procedure = document.getString("procedure");

                                Log.d("TAG", "Document ID: " + document.getId());

                                // Agrega registros de depuración
                                Log.d("TAG", "Nombre: " + nombre);
                                Log.d("TAG", "Imagen: " + img);
                                Log.d("TAG", "Procedure: " + procedure);

                                // Verificar si las cadenas son nulas o vacías
                                if (nombre != null && !nombre.trim().isEmpty() &&
                                        img != null && !img.trim().isEmpty() &&
                                        procedure != null && !procedure.trim().isEmpty()) {
                                    // Continuar con el procesamiento
                                    String[] imgArray = img.split(",");
                                    String[] nombresArray = nombre.split(",");
                                    String[] procedimientosArray = procedure.split(",");

                                    //Recorro las imagenes
                                    for (int i = 0; i < imgArray.length; i++) {

                                        // Obtener referencia al archivo en Firebase Storage
                                        String imageUrl = imgArray[i].trim();

                                        // Contenedor para cada imagen
                                        LinearLayout container = new LinearLayout(MainActivity.this);
                                        container.setOrientation(LinearLayout.VERTICAL);

                                        //uso de la libreria Picasso para cargar las imagenes desde la url
                                        ImageView imageView = new ImageView(MainActivity.this);
                                        // Descargar la imagen y cargarla en la ImageView
                                        Picasso.get().load(imageUrl).into(imageView);


                                        imageView.setContentDescription("Imagen de" + nombresArray[i]);

                                        // Personalizamos en gridlayout
                                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                                        params.width = 500;
                                        params.height = 500;
                                        params.setMargins(8, 8, 8, 8);
                                        imageView.setLayoutParams(params);

                                        // Título de cada comida
                                        TextView textView = new TextView(MainActivity.this);
                                        textView.setText(nombresArray[i]);

                                        // Cargar la fuente personalizada desde la carpeta res/font
                                        Typeface typeface = ResourcesCompat.getFont(MainActivity.this, R.font.lato_bold_italic);
                                        textView.setTypeface(typeface, Typeface.BOLD);
                                        textView.setTextSize(22);
                                        int textColor = getResources().getColor(R.color.black);
                                        textView.setTextColor(textColor);

                                        textView.setPadding(0, 0, 0, 8);

                                        // Boton
                                        Button buttonOpen = new Button(MainActivity.this);
                                        // Aplicar el fondo redondeado
                                        buttonOpen.setBackgroundResource(R.drawable.boton_redondeado);
                                        buttonOpen.setId(i);

                                        buttonOpen.setText("Ver receta");

                                        // LinearLayout para el botón
                                        LinearLayout buttonContainer = new LinearLayout(MainActivity.this);
                                        LinearLayout.LayoutParams paramsBtn = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                                        );
                                        paramsBtn.setMargins(5, 0, 5, 20);
                                        paramsBtn.width = 200;

                                        buttonOpen.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                int buttonId = buttonOpen.getId();
                                                String imageName = nombresArray[buttonId];
                                                String imageId = imgArray[buttonId];
                                                String imageProcedure = procedimientosArray[buttonId];

                                                // Iniciar la nueva actividad y enviar datos
                                                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                                                intent.putExtra("image_id", imageId);
                                                intent.putExtra("image_name", imageName);
                                                intent.putExtra("image_procedure", imageProcedure);
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
                                }else{
                                    Log.e("TAG", "Alguna cadena es nula para el documento: " + document.getId());
                                    }
                                }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });



    }

    @Override
    public void onStart() {
        super.onStart();
        TextView textViewWelcome2 = findViewById(R.id.textViewWelcome2);

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            //obtengo el uid de firestore
            String uid = currentUser.getUid();

            String email = currentUser.getEmail();
            Boolean verificado = currentUser.isEmailVerified();
            Log.i("firebase email", email);
            String mensaje = "Hola, tu email es: \n" + email;
            if (verificado) {
                mensaje += "\n y esta verificado";
                this.db.collection("users").whereEqualTo("uid", uid)
                        .get()
                        .addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String id = document.getId();
                                        db.collection("users").document(id)
                                                .update("verified", true);

                                        Log.d("TAG", document.getId() + " => " + document.getData());
                                    }
                                }
                            }
                        }
                );
            } else {
                mensaje += "\n y no esta verificado";
                currentUser.sendEmailVerification();
            }
            textViewWelcome2.setText(mensaje);
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
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);

    }
}
