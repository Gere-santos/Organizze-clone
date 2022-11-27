package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.activity.CadastroActivity;
import com.example.organizze.activity.LoginActivity;
import com.example.organizze.activity.config.ConfiguracaoFaribase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

import java.io.ByteArrayOutputStream;


public class MainActivity extends IntroActivity {

    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth autenticacao;
    private ImageView imageFoto;
    private Button buttonUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        verificaUsuarioLogado();

     setButtonBackVisible(false);
     setButtonNextVisible(false);

      addSlide(new FragmentSlide.Builder()
              .background(android.R.color.white)
              .fragment(R.layout.intro_1)
              .build()
      );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_2)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_3)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_4)
                .build()
        );
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_cadastro)
                .canGoForward(false)
                .build()
        );

    }
    public void btCadastrar (View view){
        startActivity(new Intent(getApplicationContext(), CadastroActivity.class));
    }
    public void btEntrar (View view){
       startActivity(new Intent(getApplicationContext(), LoginActivity.class));

    }
    public void verificaUsuarioLogado (){
    autenticacao = ConfiguracaoFaribase.getFirebaseAutenticacao();
    //autenticacao.signOut();

    if (autenticacao.getCurrentUser()!=null){
    abrirTelaPrincipal();
    }
    }
    public void abrirTelaPrincipal (){
        startActivity(new Intent(this, PrincipalActivity.class));

    }

    @Override
    protected void onStart() {
        super.onStart();
        verificaUsuarioLogado();
    }
}
