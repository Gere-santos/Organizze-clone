package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.activity.config.ConfiguracaoFaribase;
import com.example.organizze.activity.helper.Base64Custom;
import com.example.organizze.activity.helper.DataCustom;
import com.example.organizze.activity.model.Movimentacao;
import com.example.organizze.activity.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DespesaActivity extends AppCompatActivity {

    private TextInputEditText editData, editDescricao, editCategoria;
    private EditText editValor;
    private FloatingActionButton fabSalvar;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseref = ConfiguracaoFaribase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFaribase.getFirebaseAutenticacao();
    private Double despesaTotal;
    private Double despesaGerada;
    private Double despesaAtualizada;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

        editData = findViewById(R.id.editData);
        editDescricao = findViewById(R.id.editDescricao);
        editCategoria = findViewById(R.id.editCategoria);
        editValor = findViewById(R.id.editValor);
        fabSalvar = findViewById(R.id.fabSalvar);

        //Campo data atual
        editData.setText(DataCustom.dataAtual());
        recuperarDespesaTotal();


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void salvarDespesa (View view) {

    if(validarCamposDespesa()){
        movimentacao = new Movimentacao();
        movimentacao.setValor(Double.parseDouble(editValor.getText().toString()));
        movimentacao.setCategoria(editCategoria.getText().toString());
        movimentacao.setDescricao(editDescricao.getText().toString());
        movimentacao.setData(editData.getText().toString());
        movimentacao.setTipo("d");
        movimentacao.salvar(editData.getText().toString());

        despesaGerada = Double.parseDouble(editValor.getText().toString());
        despesaAtualizada = despesaTotal + despesaGerada;
        atualizaDespesa(despesaAtualizada);
        finish();
    }



    }
    public Boolean validarCamposDespesa(){
        String  editDataString = editData.getText().toString();
        String  editDescricaoString = editDescricao.getText().toString();
        String  editCategoriaString = editCategoria.getText().toString();
        String  editValorString = editValor.getText().toString();

        if (!editDataString.isEmpty() && !editDescricaoString.isEmpty()
                && !editCategoriaString.isEmpty() && !editValorString.isEmpty()) {
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Preencha todos os campos",
                    Toast.LENGTH_SHORT).show();
            return false;
        }


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void recuperarDespesaTotal (){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseref.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override  //pega o retorno do FB e tranforma em objeto.
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void atualizaDespesa (Double despesa){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseref.child("usuarios").child(idUsuario);
        usuarioRef.child("despesaTotal").setValue(despesa);
    }
}
