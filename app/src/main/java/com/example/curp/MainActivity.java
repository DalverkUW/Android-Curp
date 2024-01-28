package com.example.curp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.util.Properties;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    Button boton;
    EditText nom,apPat,apMat,Correo;
    RadioButton generMale,generFem;
    Spinner day,mnt,yer,enty;
    String dia="01",mes="01",año="1980",entidad="AS - AGUASCALIENTES";
    char vocals[]={'A','E','I','O','U'},conson[]={'B','C','D','F','G','H','J','K','L','M','N','P','Q','R','S','T','V','W','X','Y','Z'};
    String nombre,paterno,materno,CURP,correo;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boton=(Button) findViewById(R.id.Clacular);
        boton.setOnClickListener(this);

        nom=(EditText) findViewById(R.id.Editname);
        apPat=(EditText) findViewById(R.id.Editappat);
        apMat=(EditText) findViewById(R.id.Editapmat);
        Correo=(EditText) findViewById(R.id.EditCorreo);
        generMale=(RadioButton) findViewById(R.id.GenerMale);
        generFem=(RadioButton) findViewById(R.id.GenerFem);
        day=(Spinner) findViewById(R.id.dia);
        mnt=(Spinner) findViewById(R.id.mes);
        yer=(Spinner) findViewById(R.id.anio);
        enty=(Spinner) findViewById(R.id.Enty);

        ArrayAdapter<CharSequence> Entidades = ArrayAdapter.createFromResource(this,R.array.enty, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> Dias = ArrayAdapter.createFromResource(this,R.array.dias, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> Meses = ArrayAdapter.createFromResource(this,R.array.meses, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> Anios = ArrayAdapter.createFromResource(this,R.array.años, android.R.layout.simple_spinner_item);

        enty.setAdapter(Entidades);
        day.setAdapter(Dias);
        mnt.setAdapter(Meses);
        yer.setAdapter(Anios);

        enty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                entidad=adapterView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                dia=adapterView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        mnt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mes=adapterView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        yer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                año=adapterView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }
    
    public void CalcualrCURP(){
        CURP="";
        nombre=nom.getText().toString();
        paterno=apPat.getText().toString();
        materno=apMat.getText().toString();

        CURP+=paterno.charAt(0);
        c1:
        for(int i=1;i<paterno.length();i++){
            for(int j=0;j<vocals.length;j++){
                if(paterno.charAt(i)==vocals[j]){
                    CURP+=paterno.charAt(i);
                    break c1;
                }
            }
        }
        CURP+=materno.charAt(0);
        CURP+=nombre.charAt(0);
        CURP+=año.charAt(2);
        CURP+=año.charAt(3);
        CURP+=mes+dia;

        if(generMale.isChecked()){
            CURP+='H';
        }else if(generFem.isChecked()){
            CURP+='M';
        }
        CURP+=entidad.charAt(0);
        CURP+=entidad.charAt(1);
        c2:
        for(int i=1;i<paterno.length();i++){
            for(int j=0;j<conson.length;j++){
                if(paterno.charAt(i)==conson[j]){
                    CURP+=paterno.charAt(i);
                    break c2;
                }
            }
        }
        c3:
        for(int i=1;i<materno.length();i++){
            for(int j=0;j<conson.length;j++){
                if(materno.charAt(i)==conson[j]){
                    CURP+=materno.charAt(i);
                    break c3;
                }
            }
        }
        c4:
        for(int i=1;i<nombre.length();i++){
            for(int j=0;j<conson.length;j++){
                if(nombre.charAt(i)==conson[j]){
                    CURP+=nombre.charAt(i);
                    break c4;
                }
            }
        }

        if(Double.parseDouble(año)<2000){
            CURP+="0";
        }else{
            CURP+="A";
        }
        CURP+=Math.round(Math.floor(Math.random()*10));
    }
    public void enviarMensaje(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Properties properties = new Properties();
        properties.put("mail.smtp.host","smtp.googlemail.com");
        properties.put("mail.smtp.socketFactory.port","465");
        properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.port","465");

        try {
            session= javax.mail.Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("pruebacurp315@gmail.com","CURPexamen315");
                }
            });

            if(session!=null){
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress("pruebacurp315@gmail.com"));
                message.setSubject("Calculo de Curp");
                message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(Correo.getText().toString()));
                message.setContent("La CURP calculada es: \n"+CURP, "text/html; charset=utf-8");
                Transport.send(message);
                Toast toast1 = Toast.makeText(getApplicationContext(), "Mensaje enviado con exito", Toast.LENGTH_LONG);
                toast1.show();
            }
        } catch (Exception e) {
            Toast toast1 = Toast.makeText(getApplicationContext(), "ERROR  AL ENVIAR MENSAJE", Toast.LENGTH_LONG);
            toast1.show();
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.Clacular){
            CalcualrCURP();
            enviarMensaje();
        }
    }
}