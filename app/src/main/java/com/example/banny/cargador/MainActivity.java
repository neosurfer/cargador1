package com.example.banny.cargador;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    Button btn_cargar,btn_compartir, btn_leer_pdf,btn_leer_imagen,btn_leer_audio;
    EditText et_compartir;
    ImageView iv_imagen;
    private int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_imagen = (ImageView)findViewById(R.id.iv_imagen);

        btn_cargar = (Button)findViewById(R.id.btn_cargar);
        btn_cargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarImagen();
            }
        });

        btn_compartir = (Button)findViewById(R.id.btn_compartir);
        et_compartir = (EditText)findViewById(R.id.et_compartir);
        btn_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = et_compartir.getText().toString();
                if (mensaje.equals("")){
                    Toast.makeText(getApplicationContext(),"Debes ingresar primero un mensaje",Toast.LENGTH_SHORT).show();
                }else{
                    Compartir(mensaje);
                }
            }
        });

        btn_leer_pdf = (Button)findViewById(R.id.btn_leer_pdf);
        btn_leer_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeerPDF();
            }
        });

        btn_leer_imagen = (Button)findViewById(R.id.btn_leer_imagen);
        btn_leer_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeerIMAGEN();
            }
        });

        btn_leer_audio = (Button)findViewById(R.id.btn_leer_audio);
        btn_leer_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeerAUDIO();
            }
        });
    }

    private void CargarImagen(){
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Seleccione imagen"),PICK_IMAGE_REQUEST);
    }

    private void Compartir(String mensaje){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/*");
        i.putExtra(Intent.EXTRA_TEXT,mensaje);
        Intent chooser = Intent.createChooser(i,"Selecciona el programa");
        startActivity(chooser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE_REQUEST){
            if(resultCode == RESULT_OK){

                Uri uri = data.getData();
                try{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    iv_imagen.setImageBitmap(bitmap);
                    iv_imagen.setScaleType(ImageView.ScaleType.FIT_CENTER);

                }catch(Exception e){
                    Log.e("Mensaje","ocurrio un error");
                    Toast.makeText(getApplicationContext(), "Ocurrio un error mostrando imagen",Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(getApplicationContext(), "Ocurrio un error con la carga",Toast.LENGTH_SHORT).show();
            }

        }
    }

    void Leer(){
        String path = Environment.getExternalStorageDirectory().toString()+ "/Download";
        Log.e("ruta printicpal:",path );

        File f = new File(path);
        File file[] = f.listFiles();
        Log.e("total", "total" + file.length);

        for(int i=0; i<file.length;i++){
            Log.e("Archivo:", file[i].getName());
        }
    }

    void LeerPDF(){
        File file = new File(Environment.getExternalStorageDirectory().toString()+ "/Download/python_para_todos.pdf");
        Uri uri = Uri.fromFile(file);

        Obtener_MimeType(uri); //obtener el mime type

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.fromFile(file), "application/pdf");
        startActivity(i);
    }

    void LeerIMAGEN(){
        File file = new File(Environment.getExternalStorageDirectory().toString()+ "/Download/images.jpg");
        Uri uri = Uri.fromFile(file);

        String mime = Obtener_MimeType(uri); //obtener el mime type
        Log.e("mime",mime);

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.fromFile(file), "image/jpeg");
        startActivity(i);
    }

    void LeerAUDIO(){
        File file = new File(Environment.getExternalStorageDirectory().toString()+ "/Download/hangouts_message.ogg");
        Uri uri = Uri.fromFile(file);

        String mime = Obtener_MimeType(uri); //obtener el mime type
        Log.e("mime",mime);

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.fromFile(file), mime);
        //con el chooser siempre selecionamos un programa disponible de reproducir el archivo
        //se puede obviar si se desea
        Intent chooser = Intent.createChooser(i,"Selecciona el programa");
        startActivity(chooser);

    }

    String Obtener_MimeType(Uri uri){
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        return mimeType;
    }
}
