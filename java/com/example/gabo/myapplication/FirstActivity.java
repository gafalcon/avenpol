package com.example.gabo.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class FirstActivity extends ActionBarActivity {
    Button btnIniciar, btnSalir, btnAcercade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        btnIniciar=(Button)findViewById(R.id.btnIniciar);
        btnAcercade=(Button)findViewById(R.id.btnAcercade);
        btnSalir=(Button)findViewById(R.id.btnSalir);

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarLogin(view);
            }
        });

        btnAcercade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarAcercade(view);
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    public void lanzarAcercade(View view){
        Intent i= new Intent(this, Acercade.class);
        startActivity(i);
    }
    public void lanzarLogin(View view){
        Intent int1= new Intent(this, Login.class);
        startActivity(int1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first, menu);
        return true;
    }
}
        /*btnIniciar=(Button)findViewById(R.id.btnIniciar);
        btnAcercade=(Button)findViewById(R.id.btnAcercade);
        btnSalir=(Button)findViewById(R.id.btnSalir);

        btnIniciar.setOnClickListener(this);
        btnSalir.setOnClickListener(this);
        btnAcercade.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View arg0){
        switch(arg0.getId()){
            case R.id.btnIniciar:
                Intent int1= new Intent(this,Login.class);
                startActivity(int1);
                break;
            case R.id.btnAcercade:
                Intent int2= new Intent(this,Acercade.class);
                startActivity(int2);
                finish();
                break;
            case R.id.btnSalir:
                finish();
                break;
        }
    }

}*/
