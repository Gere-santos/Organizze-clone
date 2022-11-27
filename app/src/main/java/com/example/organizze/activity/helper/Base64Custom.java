package com.example.organizze.activity.helper;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Base64;

public class Base64Custom {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String codificarBase64(String texto){


           return Base64.getEncoder().encodeToString(texto.getBytes()).replaceAll("(\\n|\\r)","");

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decodificarBase64(String textoDecodificado){

       return new String(Base64.getDecoder().decode(textoDecodificado));
    }
}
