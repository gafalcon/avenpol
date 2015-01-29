package com.example.gabo.myapplication;




import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



import butterknife.ButterKnife;
import butterknife.InjectView;
import databases.Avenpol_db;
import databases.CarDataSource;


public class CarRegister extends Fragment {

    @InjectView(R.id.EditTextMarca) TextView tv_marca;
    @InjectView(R.id.EditTextColor) TextView tv_color;
    @InjectView(R.id.EditTextModelo) TextView tv_modelo;
    @InjectView(R.id.EditTextPlaca) TextView tv_placa;
    @InjectView(R.id.ButtonCarRegister) Button button_register;

    public CarRegister() {
        // Empty constructor required for fragment subclasses
    }
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_register);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_car_register, container, false);

        getActivity().setTitle("Registra Carro");
        ButterKnife.inject(this,rootView);
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carRegister(v);
            }
        });
        return rootView;
    }

    public void carRegister(View view){
        String modelo = tv_modelo.getText().toString();
        String marca = tv_marca.getText().toString();
        String color = tv_color.getText().toString();
        String placa = tv_placa.getText().toString();

        Avenpol_db db = new Avenpol_db(getActivity());
        db.openDb();
        CarDataSource carDataSource = new CarDataSource(db.getDatabase());
        carDataSource.createCar(marca,modelo,placa,color,1);
        Toast.makeText(getActivity(), "Car was created", Toast.LENGTH_LONG).show();
    }

}
