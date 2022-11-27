package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.activity.config.ConfiguracaoFaribase;
import com.example.organizze.activity.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;
    private EditText editLoginEmail, editLoginSenha;
    private Button buttonLogar;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editLoginEmail = findViewById(R.id.editLoginEmail);
        editLoginSenha = findViewById(R.id.editLoginSenha);
        buttonLogar = findViewById(R.id.buttonLogar);

        buttonLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String email = editLoginEmail.getText().toString();
            String senha = editLoginSenha.getText().toString();

            if(!email.isEmpty() && !senha.isEmpty()){
                usuario = new Usuario();
                usuario.setEmail(email);
                usuario.setSenha(senha);
                validarLogin();

            }
                else{
                Toast.makeText(LoginActivity.this, "Preencha todos os campos",
                        Toast.LENGTH_SHORT).show();
            }

            }
        });

    }
    public void validarLogin(){
     autenticacao = ConfiguracaoFaribase.getFirebaseAutenticacao();
     autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(
             new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     if (task.isSuccessful()){
                        abrirTelaPrincipal();
                     }else {
                         String excecao = "";
                         try{
                             throw task.getException();
                         }catch (FirebaseAuthInvalidUserException e){
                             excecao = "Usuário não cadastrado!";
                         }
                         catch (FirebaseAuthInvalidCredentialsException e){
                             excecao = "E-mail ou senha incorreto!";
                         }
                         catch ( Exception e){
                             excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                             e.printStackTrace();
                         }



                         Toast.makeText(getApplicationContext(), excecao,
                            Toast.LENGTH_SHORT).show();
                     }
                 }
             }
     );
    }
    public void abrirTelaPrincipal (){
startActivity(new Intent(this, PrincipalActivity.class));
 finish();
    }
}