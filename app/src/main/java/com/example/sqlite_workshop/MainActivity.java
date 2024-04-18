package com.example.sqlite_workshop;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editTextRegister, editTextCode, editTextPhoneNumber, editTextPlaca;
    private Button buttonCreate, buttonSearchCode, buttonSearchPlaca, buttonDeleteCode, buttonEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editTextRegister = (EditText) findViewById(R.id.editTextRegister);
        editTextCode = (EditText) findViewById(R.id.editTextCode);
        editTextPhoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
        editTextPlaca = (EditText) findViewById(R.id.editTextPlaca);

        // Boton para crear registro con su OnClickListener
        buttonCreate = (Button) findViewById(R.id.buttonCreate);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearRegistro();
            }
        });

        // Boton para buscar registro por medio del codigo
        buttonSearchCode = (Button) findViewById(R.id.buttonSearchCode);
        buttonSearchCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarRegistroPorCodigo();
            }
        });

        // Boton para buscar registro por medio de la placa
        buttonSearchPlaca = (Button) findViewById(R.id.buttonSearchPlaca);
        buttonSearchPlaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarRegistroPorPlaca();
            }
        });

        // Boton para eliminar registro
        buttonDeleteCode = (Button) findViewById(R.id.buttonDeleteCode);
        buttonDeleteCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarRegistro();
            }
        });

        // Boton para editar registro
        buttonEdit = (Button) findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarRegistro();
            }
        });
    }


    private void crearRegistro() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);

        try (SQLiteDatabase db = admin.getWritableDatabase()) {

            String codigo = editTextRegister.getText().toString();
            String descripcion = editTextCode.getText().toString();
            String numero = editTextPhoneNumber.getText().toString();
            String placa = editTextPlaca.getText().toString();

            // Primero, verificamos si el código ya existe en la base de datos
            Cursor cursor = db.query("solicitud", new String[]{"codigo"}, "codigo = ?", new String[]{codigo}, null, null, null);

            if (cursor.getCount() > 0) {
                Toast.makeText(this, "Ya existe un registro con el código " + codigo, Toast.LENGTH_SHORT).show();
            } else {
                ContentValues registro = new ContentValues();
                registro.put("codigo", codigo);
                registro.put("descripcion", descripcion);
                registro.put("numero", numero);
                registro.put("placa", placa);

                db.insert("solicitud", null, registro);

                editTextRegister.setText("");
                editTextCode.setText("");
                editTextPhoneNumber.setText("");
                editTextPlaca.setText("");

                Toast.makeText(this, "usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
        } catch (SQLiteException e) {
            Toast.makeText(this, "Error al crear el registro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void consultarRegistroPorCodigo(){
        
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase db = admin.getReadableDatabase();

        String codigo = editTextRegister.getText().toString();

        //Cursos es un apuntador, permite acceder a datos que cumplan las condiciones
        //Cursos para consultar
        // Escribir consulta SQL con rawQuery

        Cursor fila = db.rawQuery("select descripcion, numero, placa from solicitud where codigo = "+ codigo, null);
        if (fila.moveToFirst()){
            editTextCode.setText(fila.getString(0));
            editTextPhoneNumber.setText(fila.getString(1));
            editTextPlaca.setText(fila.getString(2));

        }else {
            Toast.makeText(this, "Registro no encontrado", Toast.LENGTH_SHORT).show();
        }

        db.close();

    }
    private void consultarRegistroPorPlaca(){

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase db = admin.getReadableDatabase();

        String placa = editTextPlaca.getText().toString();

        // Se debe agregar comillas simples, debido a que su llamado es de tipo String
        Cursor fila = db.rawQuery("select codigo, descripcion, numero from solicitud where placa = '"+ placa+"'", null);
        if(fila.moveToFirst()){
            editTextRegister.setText(fila.getString(0));
            editTextCode.setText(fila.getString(1));
            editTextPhoneNumber.setText(fila.getString(2));

        }else{

            Toast.makeText(this, "No existe placa o ingrese la placa en mayusculas", Toast.LENGTH_SHORT).show();
        }
        db.close();

    }
    private void eliminarRegistro(){

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase db = admin.getReadableDatabase();

        String codigo = editTextRegister.getText().toString();
        int cantidad = db.delete("solicitud", "codigo="+codigo, null);
        db.close();
        editTextCode.setText("");
        editTextPhoneNumber.setText("");
        editTextPlaca.setText("");

        if(cantidad == 1){
            Toast.makeText(this, "Registro borrado exitosamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No existe ningun registro con este codigo", Toast.LENGTH_SHORT).show();
        }

    }
    private void modificarRegistro(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase db = admin.getReadableDatabase();
        
        String codigo = editTextRegister.getText().toString();
        String descripcion = editTextCode.getText().toString();
        String numero = editTextPhoneNumber.getText().toString();
        String placa = editTextPlaca.getText().toString();

        ContentValues modificar = new ContentValues();

        modificar.put("codigo", codigo);
        modificar.put("descripcion", descripcion);
        modificar.put("numero", numero);
        modificar.put("placa", placa);
        
        int cantidad = db.update("solicitud", modificar,"codigo="+codigo, null );
        db.close();

        if( cantidad == 1){
            Toast.makeText(this, "Articulo modificado correctamente", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "No existe este articulo para modificarse", Toast.LENGTH_SHORT).show();
        }
    }




}