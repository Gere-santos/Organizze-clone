package com.example.organizze.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organizze.R;
import com.example.organizze.activity.adapter.AdapterMovimentacao;
import com.example.organizze.activity.config.ConfiguracaoFaribase;
import com.example.organizze.activity.helper.Base64Custom;
import com.example.organizze.activity.helper.MascaraMonetaria;
import com.example.organizze.activity.model.Movimentacao;
import com.example.organizze.activity.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.AEADBadTagException;
//import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
//import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class PrincipalActivity extends AppCompatActivity {
    private MaterialCalendarView materialCalendarView;
    private TextView textSaudacao, textSaldo;
    private FirebaseAuth autenticacao = ConfiguracaoFaribase.getFirebaseAutenticacao();
    private DatabaseReference fireBaseRef = ConfiguracaoFaribase.getFirebaseDatabase();
    private Double receitaTotal = 0.0;
    private Double despesaTotal = 0.0;
    private Double resumoUsuario = 0.0;
    private  DatabaseReference usuarioRef;
    private ValueEventListener valueEventListenerUsuario;
    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private DatabaseReference movimentacosRef;
    private String mesAnoSelecionado;
    private ValueEventListener valueEventListenerMovimentacoes;
    private Movimentacao movimentacao;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Organizze");
        toolbar.setTitleTextColor(Color.WHITE);

        materialCalendarView = findViewById(R.id.calendarView);
        textSaudacao = findViewById(R.id.textSaudacao);
        textSaldo = findViewById(R.id.textSaldo);
        recyclerView = findViewById(R.id.recyclerMovimentos);
        configuraCalendarView();
        swipe();

         adapterMovimentacao = new AdapterMovimentacao(movimentacoes, this);

  ///Configurar recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentacao);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumo();
        recuperarMovimentacoes();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuSair) {

          autenticacao.signOut();
          startActivity(new Intent(this, MainActivity.class));
          finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void adicionarReceita(View view){
startActivity(new Intent(this,ReceitaActivity.class));
    }
    public void adicionarDespesa(View view){
startActivity(new Intent(this, DespesaActivity.class));
    }

    public void configuraCalendarView(){
        CharSequence meses[] = {"Janeiro","Fevereiro","Março","Abril","Maio"
                ,"Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};
    materialCalendarView.setTitleMonths(meses);
    CalendarDay dataAtual = materialCalendarView.getCurrentDate();
    String mes = String.format("%02d", (dataAtual.getMonth() + 1 ));
    mesAnoSelecionado = String.valueOf( mes + "" + dataAtual.getYear());
    materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
            String mes = String.format("%02d", (date.getMonth() + 1 ));
            mesAnoSelecionado = (mes + "" + date.getYear());
           movimentacosRef.removeEventListener(valueEventListenerMovimentacoes);
            recuperarMovimentacoes();
        }
    });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void recuperarResumo(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        usuarioRef = fireBaseRef.child("usuarios").child(idUsuario);
        Log.i("Evento", "evento iniciado");
            valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
                receitaTotal = usuario.getReceitaTotal();
                resumoUsuario = receitaTotal - despesaTotal;
                //formata os zeros do saldo recuperado.
                String valorTratado =  String.valueOf( MascaraMonetaria.adiconarMascara(resumoUsuario));
                    textSaudacao.setText("Olá, " + usuario.getNome());
                    textSaldo.setText("R$ " + valorTratado);






            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void atualizarSaldo (){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        usuarioRef = fireBaseRef.child("usuarios").child(idUsuario);
        if (movimentacao.getTipo().equals("r")){
            receitaTotal = receitaTotal - movimentacao.getValor();
            usuarioRef.child("receitaTotal").setValue(receitaTotal);
        }
        if (movimentacao.getTipo().equals("d")){
            despesaTotal = despesaTotal - movimentacao.getValor();
            usuarioRef.child("despesaTotal").setValue(despesaTotal);
        }


    }
    public void excluirMovimentacao( RecyclerView.ViewHolder viewHolder ){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Excluir movimentação da conta");
        alertDialog.setMessage("Realmente deseja exlcluir essa movimentação ?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                movimentacao = movimentacoes.get(position);
                String emailUsuario = autenticacao.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.codificarBase64(emailUsuario);
                movimentacosRef = fireBaseRef
                        .child("movimentacao")
                        .child(idUsuario)
                        .child(mesAnoSelecionado);
                movimentacosRef.child(movimentacao.getKey()).removeValue();
                adapterMovimentacao.notifyItemRemoved(position);
                atualizarSaldo();

            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PrincipalActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                adapterMovimentacao.notifyDataSetChanged();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void recuperarMovimentacoes (){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
       movimentacosRef = fireBaseRef
               .child("movimentacao")
               .child(idUsuario)
               .child(mesAnoSelecionado);
        valueEventListenerMovimentacoes = movimentacosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            movimentacoes.clear();
             //Recuperar todos os itens
            for (DataSnapshot dados: snapshot.getChildren()){
                Movimentacao movimentacao = dados.getValue(Movimentacao.class);
                movimentacao.setKey(dados.getKey());
                movimentacoes.add(movimentacao);
            }
           adapterMovimentacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void swipe (){
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
               int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
               int swipFlags = ItemTouchHelper.START | ItemTouchHelper.END;
           return makeMovementFlags(dragFlags, swipFlags);
}
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            excluirMovimentacao(viewHolder);
            }
        };
        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);

    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Evento", "evento parado");
        usuarioRef.removeEventListener(valueEventListenerUsuario);
        movimentacosRef.removeEventListener(valueEventListenerMovimentacoes);

    }

}
