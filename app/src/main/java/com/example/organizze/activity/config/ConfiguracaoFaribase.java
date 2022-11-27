package com.example.organizze.activity.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFaribase {
    private static FirebaseAuth autenticacao;
    private static DatabaseReference firebase;

    //retorna instancia do Firebasedatabase
    public static DatabaseReference getFirebaseDatabase(){
        if (firebase == null){
            firebase = FirebaseDatabase.getInstance().getReference();
        }
     return firebase;
    }

  //atributo static serve para que o valor sempre seja fixo e não mude
  //Retorna a instancia do FirebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao(){

        if (autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }
}
