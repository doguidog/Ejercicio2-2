package com.example.ejercicio2_2;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ejercicio2_2.Configuraciones.Configuracion;
import com.example.ejercicio2_2.Contextos.Cuentas;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityApi extends AppCompatActivity {

    ListView list;
    ArrayList<String> titulos = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    EditText buscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);

        obtenerUsuario();

        list = (ListView) findViewById(R.id.lista);

        buscar = (EditText) findViewById(R.id.txtBuscar);

        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    buscarUsuario(buscar.getText().toString());
                    if (buscar.getText().toString().equals("")){
                        obtenerUsuario();
                    }
                } catch (Exception ex){
                    Toast.makeText(getApplicationContext(),"Valor invalido",Toast.LENGTH_SHORT).show();
                }

            }
        });





    }


    private void obtenerUsuario() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Configuracion interfaceusers = retrofit.create(Configuracion.class);

        Call <List<Cuentas>> llamada = interfaceusers.getUsuarios();

        llamada.enqueue(new Callback<List<Cuentas>>() {
            @Override
            public void onResponse(Call<List<Cuentas>> call, Response<List<Cuentas>> response) {
                titulos.clear();
                for (Cuentas cuentas : response.body()){
                    titulos.add(cuentas.getTitle());

                    Log.i("On Response", cuentas.getTitle());
                }

                arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, titulos);
                list.setAdapter(arrayAdapter);
                //notifica si la data ha cambiado
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Cuentas>> call, Throwable t) {

            }
        });

    }

    private void buscarUsuario(String valor) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Configuracion interfaceusers = retrofit.create(Configuracion.class);


        Call <Cuentas> llamada = interfaceusers.getUsuario(valor);

        llamada.enqueue(new Callback<Cuentas>() {
            @Override
            public void onResponse(Call<Cuentas> call, Response<Cuentas> response) {


               try {
                   Cuentas cuentas = response.body();

                   titulos.clear();//eliminar todo los registros
                   titulos.add(cuentas.getTitle());
                   arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, titulos);
                   list.setAdapter(arrayAdapter);

                   //Log.i("On Response", usuarios.getTitle());

                   //notifica si la data ha cambiado
                   arrayAdapter.notifyDataSetChanged();
               }catch (Exception ex){
                   titulos.clear();
                   buscar.setText("");
                   obtenerUsuario();
                   Toast.makeText(getApplicationContext(),"Valor no encontrado",Toast.LENGTH_SHORT).show();
               }

            }

            @Override
            public void onFailure(Call<Cuentas> call, Throwable t) {

            }


        });

    }
}