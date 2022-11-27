package com.example.organizze.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organizze.R;
import com.example.organizze.activity.helper.MascaraMonetaria;
import com.example.organizze.activity.model.Movimentacao;


import java.text.DecimalFormat;
import java.util.List;



public class AdapterMovimentacao extends RecyclerView.Adapter<AdapterMovimentacao.MyViewHolder> {

    List<Movimentacao> movimentacoes;
    Context context;



    public AdapterMovimentacao(List<Movimentacao> movimentacoes, Context context) {
        this.movimentacoes = movimentacoes;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movimentacao, parent, false);
        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movimentacao movimentacao = movimentacoes.get(position);

        String valorTratado =  String.valueOf( MascaraMonetaria.adiconarMascara(movimentacao.getValor()));
        holder.titulo.setText(movimentacao.getDescricao());
        holder.valor.setText((valorTratado));
        holder.categoria.setText(movimentacao.getCategoria());


        if (movimentacao.getTipo() == "d" || movimentacao.getTipo().equals("d")) {
            holder.valor.setTextColor(context.getResources().getColor(R.color.despesa));
            holder.valor.setText("-" + valorTratado);
        }
    }


    @Override
    public int getItemCount() {
        return movimentacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titulo, valor, categoria;

        public MyViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.textAdapterTitulo);
            valor = itemView.findViewById(R.id.textAdapterValor);
            categoria = itemView.findViewById(R.id.textAdapterCategoria);
        }

    }

}
