package com.example.organizze.activity.helper;

import java.text.DecimalFormat;

public class MascaraMonetaria {
    public static String adiconarMascara(Double valor){
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        String resultadoFormatado = decimalFormat.format(valor);

        return resultadoFormatado;
    }
}
