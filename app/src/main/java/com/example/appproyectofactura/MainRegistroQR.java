package com.example.appproyectofactura;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class MainRegistroQR extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    TextView textoQR, textoRUC, textoFACT, textoIGV, textoTotal, textoEmision, resultRegistro;
    Button buttonReg;
    RequestQueue requestQueue;
    private ZXingScannerView scannerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(getApplicationContext());
        setContentView(scannerView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Toast.makeText(getApplicationContext(), "Permission already granted!", Toast.LENGTH_LONG).show();
            } else {
                requestPermission();
            }
        }
    }


    // Validar GET
    private void documentValidation(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        if (jsonObject.getString("estado") != null) {
                            resultRegistro.setText("");
                            Toast.makeText(getApplicationContext(), "LA FACTURA YA EXISTE", Toast.LENGTH_LONG).show();
                        }
                        // resultRegistro.setText(jsonObject.getString("estado"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                registrarDoc("https://proyectoinformatico03.000webhostapp.com/registrar_doc.php");
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }


    ///// Registrar POST
    private void registrarDoc(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //              if(!response.isEmpty()){
                resultRegistro.setText("REGISTRADO");
                //                  Toast.makeText(MainActivity.this, "REGISTRADO", Toast.LENGTH_SHORT).show();

/*
                }else{
                    Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("nro_ruc", textoRUC.getText().toString());
                parametros.put("nro_doc", textoFACT.getText().toString());
                parametros.put("igv", textoIGV.getText().toString());
                parametros.put("total", textoTotal.getText().toString());
                parametros.put("fecha_emision", textoEmision.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }


    @Override
    public void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if (scannerView == null) {
                    scannerView = new ZXingScannerView(getApplicationContext());
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                displayAlertMessage("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void displayAlertMessage(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getApplicationContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(Result result) {
        String myResult = result.getText();

        String arraymyResult[] = myResult.split("\\|");
        final String ruc = arraymyResult[0];
        final String factura = arraymyResult[2] + "-" + arraymyResult[3];
        final String igv = arraymyResult[4];
        final String total = arraymyResult[5];
        final String emision = arraymyResult[6];

        setContentView(R.layout.main_registro_qr_fragment);
        scannerView.stopCamera();

        textoQR = findViewById(R.id.TextQR);
        textoQR.setText(myResult);
        textoRUC = findViewById(R.id.TextRUC);
        textoRUC.setText(ruc);
        textoFACT = findViewById(R.id.TextFactura);
        textoFACT.setText(factura);
        textoIGV = findViewById(R.id.TextIGV);
        textoIGV.setText(igv);
        textoTotal = findViewById(R.id.TextTotal);
        textoTotal.setText(total);
        textoEmision = findViewById(R.id.TextEmision);
        textoEmision.setText(emision);

        buttonReg = findViewById(R.id.ButtonReg);
        resultRegistro = findViewById(R.id.ResultRegistro);

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    documentValidation("https://proyectoinformatico03.000webhostapp.com/validacion.php?nro_ruc=" + textoRUC.getText() + "&nro_doc=" + textoFACT.getText() + "");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "ERROR DE CONEXIÓN", Toast.LENGTH_SHORT).show();
                }
            }
        });

/*        runOnUiThread(new Runnable(){
            public void run() {
                try {
                    documentValidation("https://proyectoinformatico03.000webhostapp.com/validacion.php?nro_ruc=" + textoRUC.getText() + "&nro_doc=" + textoFACT.getText()+"");
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }
}
