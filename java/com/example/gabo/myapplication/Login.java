package com.example.gabo.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class Login extends Activity implements View.OnClickListener {

    @InjectView(R.id.etUser) EditText etUser;
    @InjectView(R.id.etPass) EditText etPass;
    @InjectView(R.id.btnIngresar) Button btnIngresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.inject(this);


        btnIngresar.setOnClickListener(this);
    }



    @Override
    public void onClick(View arg0){
        switch(arg0.getId()){
            case R.id.btnIngresar:
                String user=etUser.getText().toString();
                String pswd=etPass.getText().toString();
                if(true){
                    //Mostrar pantalla-Actividad
                    //Intent int1= new Intent("com.example.appcomedores.Administrador");
                    //startActivity(int1);
                    Intent int1= new Intent(this, MyDrawerActivity.class);
                    startActivity(int1);
                    break;
                }
                else if(user.equals("sromero") && pswd.equals("123")){
                    //Mostrar pantalla-Actividad
                    //Intent int2= new Intent("com.example.appcomedores.Dueno");
                    // startActivity(int2);
                    finish();
                    break;

                }else{
                    //Toast Presenta un cuadro de dialogo
                    Toast t=Toast.makeText(this,"User o Password Incorrecto",Toast.LENGTH_SHORT);
                    t.show();
                }
                break;
        }

    }
}