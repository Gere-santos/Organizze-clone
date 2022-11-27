package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.activity.config.ConfiguracaoFaribase;
import com.example.organizze.activity.helper.Base64Custom;
import com.example.organizze.activity.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;
    private Button buttonCadastrar;
    private EditText editNone, editEmail, editSenha;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        getSupportActionBar().setTitle("Cadastro");

        editEmail = findViewById(R.id.editEmail);
        editNone = findViewById(R.id.editNone);
        editSenha = findViewById(R.id.editSenha);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);


        buttonCadastrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String nome = editNone.getText().toString();
                String email = editEmail.getText().toString();
                String senha = editSenha.getText().toString();

                if (!nome.isEmpty() && !email.isEmpty() && !senha.isEmpty()) {

                   usuario = new Usuario();
                   usuario.setNome(nome);
                   usuario.setEmail(email);
                   usuario.setSenha(senha);
                    cadastraUsuario ();

                } else {
                    Toast.makeText(getApplicationContext(), "Preencha todos os campos",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
  public void cadastraUsuario (){
      autenticacao = ConfiguracaoFaribase.getFirebaseAutenticacao();
      autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()
      ).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
          @RequiresApi(api = Build.VERSION_CODES.O)
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()){
                 String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                 usuario.setIdUsuario(idUsuario);
                 usuario.salvar();
                 finish();

              }else{
              //tratamento de erros ao cadastrar

                String excecao = "";
                 try{
                     throw task.getException();
                 }catch (FirebaseAuthWeakPasswordException e ){
                    excecao = "Digite uma senha mais forte!";
                 }catch (FirebaseAuthInvalidCredentialsException e ){
                     excecao = "Digite uma E-mail válido!";
                 }catch (FirebaseAuthUserCollisionException e){
                     excecao = "Essa conta já foi cadastrada!";
                 }catch ( Exception e){
                     excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                     e.printStackTrace();
                 }

                  Toast.makeText(getApplicationContext(), excecao,
                          Toast.LENGTH_SHORT).show();
              }
          }
      });
  }
}
