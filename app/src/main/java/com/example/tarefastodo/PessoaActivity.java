package com.example.tarefastodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.tarefastodo.models.Pessoa;
import com.google.firebase.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class PessoaActivity extends AppCompatActivity {

    private EditText edtNome;
    private EditText edtEmail;
    private ListView listV_dados;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private List<Pessoa> pessoaList = new ArrayList<Pessoa>();
    private ArrayAdapter<Pessoa> arrayAdapterPessoa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pessoa);

        edtNome = (EditText) findViewById(R.id.editTextTextPersonName2);
        edtEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        listV_dados = (ListView) findViewById(R.id.listViewTeste);

        inicializarFirebase();
        eventoDataBase();
    }

    private void eventoDataBase() {
        databaseReference.child("Pessoa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                pessoaList.clear();
                for(DataSnapshot objSnapshot: snapshot.getChildren()){
                    Pessoa pessoa = objSnapshot.getValue(Pessoa.class);
                    pessoaList.add(pessoa);
                }
                arrayAdapterPessoa = new ArrayAdapter<Pessoa>(PessoaActivity.this,
                        android.R.layout.simple_list_item_1,
                        pessoaList);
                listV_dados.setAdapter(arrayAdapterPessoa);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(PessoaActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }


    public void enviar(View view){

        Pessoa pessoa = new Pessoa();

        pessoa.setId(UUID.randomUUID().toString());     //Inserindo ID
        pessoa.setNome(edtNome.getText().toString());   //Nome
        pessoa.setEmail(edtEmail.getText().toString()); //Email

        System.out.println("Nome: "+pessoa.getNome());
        System.out.println("Email:"+pessoa.getEmail());

        databaseReference.child("Pessoa").child(pessoa.getId()).setValue(pessoa);

        limparcampos();

    }

    private void limparcampos(){
        edtNome.setText("");
        edtEmail.setText("");
    }
}