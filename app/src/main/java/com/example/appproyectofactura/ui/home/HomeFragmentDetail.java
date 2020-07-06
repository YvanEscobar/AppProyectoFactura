package com.example.appproyectofactura.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appproyectofactura.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeFragmentDetail extends Fragment {
    TextView textoID, textoRUC, textoFactura, textoIGV, textoTotal, textoEstado, textoFEmision, textoFRegistro;
    RequestQueue requestQueue;

    public HomeFragmentDetail() {
        // Required empty public constructor
    }

    public static HomeFragmentDetail newInstance(String param1, String param2) {
        HomeFragmentDetail fragment = new HomeFragmentDetail();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_detail, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String id_fact = bundle.getString("id_fact");
            String ruc_fact = bundle.getString("ruc_fact");
            String num_fact = bundle.getString("num_fact");

            textoID = view.findViewById(R.id.TextoID);
            textoID.setText(id_fact);
            textoRUC = view.findViewById(R.id.TextoRUC);
            textoRUC.setText(ruc_fact);
            textoFactura = view.findViewById(R.id.TextoFactura);
            textoFactura.setText(num_fact);
            //    Toast.makeText(getContext(), "ID : " + id_fact , Toast.LENGTH_LONG).show();
        }

        textoIGV = view.findViewById(R.id.TextoIGV);
        textoTotal = view.findViewById(R.id.TextoTotal);
        textoFEmision = view.findViewById(R.id.TextoFEmision);
        textoFRegistro = view.findViewById(R.id.TextoFRegistro);
        textoEstado = view.findViewById(R.id.TextoEstado);

        documentValidation("https://proyectoinformatico03.000webhostapp.com/validacion.php?nro_ruc=" + textoRUC.getText() + "&nro_doc=" + textoFactura.getText() + "");
                return view;
            }

            // Association with Validation Result
            private void documentValidation(String URL) {
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject jsonObject = null;
                double igv, total;
                String femision;
                for (int i = 0; i < response.length(); i++) {

                    try {
                        jsonObject = response.getJSONObject(i);
                        textoFRegistro.setText(jsonObject.getString("fecha_reg"));
                        textoEstado.setText(jsonObject.getString("estado"));

                        femision = jsonObject.getString("fecha_emision");
                        textoFEmision.setText(femision);

                        igv = jsonObject.getDouble("igv");
                        if (igv == 0) {
                            textoIGV.setText("");
                        } else {
                            textoIGV.setText(String.valueOf(igv));
                        }

                        total = jsonObject.getDouble("total");
                        if (total == 0) {
                            textoTotal.setText("");
                        } else {
                            textoTotal.setText(String.valueOf(total));
                        }

                        registrarDocLogDetalle("https://proyectoinformatico03.000webhostapp.com/registrar_log_det.php");

                    } catch (JSONException e) {
                        Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mResultEtVal.setText("");
                Toast.makeText(getActivity().getApplicationContext(), "NO SE ENCONTRÓ DOCUMENTO", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }

    ///// Registrar POST
    private void registrarDocLogDetalle(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //              if(!response.isEmpty()){
                //resultRegistro.setText("REGISTRADO");
                //                  Toast.makeText(MainActivity.this, "REGISTRADO", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("nro_ruc", textoRUC.getText().toString());
                parametros.put("nro_doc", textoFactura.getText().toString());
                parametros.put("usuario", textoEstado.getText().toString());
                parametros.put("estado", textoEstado.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }
}