package com.example.organizze.activity.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.organizze.activity.config.ConfiguracaoFaribase;
import com.example.organizze.activity.helper.Base64Custom;
import com.example.organizze.activity.helper.DataCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Movimentacao {
    private String data;
    private String categoria;
    private String descricao;
    private String tipo;
    private double valor;
    private String key;

    public Movimentacao() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void salvar(String dataEscolhida){

       FirebaseAuth autenticacao = ConfiguracaoFaribase.getFirebaseAutenticacao();
        DatabaseReference firebase = ConfiguracaoFaribase.getFirebaseDatabase();
        String IdUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());
        firebase.child("movimentacao")
                .child(IdUsuario)
                .child(DataCustom.mesAnoDataEscolhida(dataEscolhida))
                .push()
                .setValue(this);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
