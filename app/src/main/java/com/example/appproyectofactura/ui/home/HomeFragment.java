package com.example.appproyectofactura.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appproyectofactura.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    RequestQueue requestQueue;
    TableLayout table;
    EditText mEditRUC, mEditFactura;
    Button mButtonBuscar;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        table = root.findViewById(R.id.table);
        table.setStretchAllColumns(true);
        obtenerFactura("https://proyectoinformatico03.000webhostapp.com/listar_doc.php");

        mEditRUC = root.findViewById(R.id.EditRUC);
        mEditFactura = root.findViewById(R.id.EditFactura);
        mButtonBuscar = root.findViewById(R.id.ButtonBuscar);

        mButtonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table.removeAllViews();
                obtenerBuscarFactura ("https://proyectoinformatico03.000webhostapp.com/validacion_buscar.php?nro_ruc="+mEditRUC.getText()+"&nro_doc="+mEditFactura.getText()+"");
                mEditRUC.setText("");
                mEditFactura.setText("");
            }
        });

        return root;
    }

    private void obtenerFactura(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    //  @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("datos");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                int id = jsonObject.getInt("id");
                                String nro_ruc = jsonObject.getString("nro_ruc");
                                String nro_doc = jsonObject.getString("nro_doc");

                                //textView.append(id+" , "+nro_ruc+" , "+nro_doc+"\n\n");
                                //Toast.makeText(getActivity().getApplicationContext(), "Fila Número: " + id+" , "+nro_ruc+"\n\n", Toast.LENGTH_LONG).show();

                                TableRow newRow = new TableRow(getActivity().getApplicationContext());

                                TextView IDRow = new TextView(getActivity().getApplicationContext());
                                TextView RUCRow = new TextView(getActivity().getApplicationContext());
                                TextView FACTRow = new TextView(getActivity().getApplicationContext());

                                IDRow.setText(String.valueOf(id));
                                IDRow.setBackgroundColor(Color.parseColor("#013191"));
                                IDRow.setGravity(Gravity.CENTER);
                                IDRow.setTextColor(Color.WHITE);
                                IDRow.setPadding(0, 20, 0, 20);

                                RUCRow.setText(nro_ruc);
                                RUCRow.setBackgroundColor(Color.parseColor("#6AB3DA"));
                                RUCRow.setGravity(Gravity.CENTER);
                                RUCRow.setTextColor(Color.DKGRAY);
                                RUCRow.setPadding(0, 20, 0, 20);

                                FACTRow.setText(nro_doc);
                                FACTRow.setBackgroundColor(Color.parseColor("#FF99C6E9"));
                                FACTRow.setGravity(Gravity.CENTER);
                                FACTRow.setTextColor(Color.DKGRAY);
                                FACTRow.setPadding(0, 20, 0, 20);

                                newRow.addView(IDRow);
                                newRow.setId(id);
                                newRow.addView(RUCRow);
                                newRow.addView(FACTRow);

                                newRow.setPadding(0, 0, 0, 1);

                                table.addView(newRow);
                                table.setPadding(10, 0, 10, 0);

                                newRow.setClickable(true);
                                newRow.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        TableRow tr = (TableRow) v;
                                        TextView id_fact, ruc_fact, num_fact;
                                        id_fact = (TextView) tr.getChildAt(0);
                                        ruc_fact = (TextView) tr.getChildAt(1);
                                        num_fact = (TextView) tr.getChildAt(2);

                                        Bundle datosEnviar = new Bundle();
                                        datosEnviar.putString("id_fact", id_fact.getText().toString());
                                        datosEnviar.putString("ruc_fact", ruc_fact.getText().toString());
                                        datosEnviar.putString("num_fact", num_fact.getText().toString());
                                        HomeFragmentDetail homeFragmentDetail = new HomeFragmentDetail();
                                        homeFragmentDetail.setArguments(datosEnviar);

                                        FragmentManager fragmentManager = getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                        //fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right,ANIMATION_ID_FOR_ENTERING_VIEW,ANIMATION_ID_FOR_EXITING_VIEW);
                                        fragmentTransaction.replace(R.id.nav_host_fragment, homeFragmentDetail).addToBackStack(null);
                                        fragmentTransaction.commit();
                                        //fragmentTransaction.addToBackStack(null);
                                        //   fragmentManager.beginTransaction().replace(R.id.nav_host_fragment,homeFragmentDetail).commit();

                                        //    Toast.makeText(getActivity().getApplicationContext(), "Fila Número: " + id_fact.getText().toString()+" , "+ruc_fact.getText().toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "NO EXISTE LISTADO", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void obtenerBuscarFactura(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    //  @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("datos");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                int id = jsonObject.getInt("id");
                                String nro_ruc = jsonObject.getString("nro_ruc");
                                String nro_doc = jsonObject.getString("nro_doc");

                                //textView.append(id+" , "+nro_ruc+" , "+nro_doc+"\n\n");
                                //Toast.makeText(getActivity().getApplicationContext(), "Fila Número: " + id+" , "+nro_ruc+"\n\n", Toast.LENGTH_LONG).show();

                                TableRow newRow = new TableRow(getActivity().getApplicationContext());

                                TextView IDRow = new TextView(getActivity().getApplicationContext());
                                TextView RUCRow = new TextView(getActivity().getApplicationContext());
                                TextView FACTRow = new TextView(getActivity().getApplicationContext());

                                IDRow.setText(String.valueOf(id));
                                IDRow.setBackgroundColor(Color.parseColor("#013191"));
                                IDRow.setGravity(Gravity.CENTER);
                                IDRow.setTextColor(Color.WHITE);
                                IDRow.setPadding(0, 20, 0, 20);

                                RUCRow.setText(nro_ruc);
                                RUCRow.setBackgroundColor(Color.parseColor("#6AB3DA"));
                                RUCRow.setGravity(Gravity.CENTER);
                                RUCRow.setTextColor(Color.DKGRAY);
                                RUCRow.setPadding(0, 20, 0, 20);

                                FACTRow.setText(nro_doc);
                                FACTRow.setBackgroundColor(Color.parseColor("#FF99C6E9"));
                                FACTRow.setGravity(Gravity.CENTER);
                                FACTRow.setTextColor(Color.DKGRAY);
                                FACTRow.setPadding(0, 20, 0, 20);

                                newRow.addView(IDRow);
                                newRow.setId(id);
                                newRow.addView(RUCRow);
                                newRow.addView(FACTRow);

                                newRow.setPadding(0, 0, 0, 1);

                                table.addView(newRow);
                                table.setPadding(10, 0, 10, 0);

                                newRow.setClickable(true);
                                newRow.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        TableRow tr = (TableRow) v;
                                        TextView id_fact, ruc_fact, num_fact;
                                        id_fact = (TextView) tr.getChildAt(0);
                                        ruc_fact = (TextView) tr.getChildAt(1);
                                        num_fact = (TextView) tr.getChildAt(2);

                                        Bundle datosEnviar = new Bundle();
                                        datosEnviar.putString("id_fact", id_fact.getText().toString());
                                        datosEnviar.putString("ruc_fact", ruc_fact.getText().toString());
                                        datosEnviar.putString("num_fact", num_fact.getText().toString());
                                        HomeFragmentDetail homeFragmentDetail = new HomeFragmentDetail();
                                        homeFragmentDetail.setArguments(datosEnviar);

                                        FragmentManager fragmentManager = getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                        //fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right,ANIMATION_ID_FOR_ENTERING_VIEW,ANIMATION_ID_FOR_EXITING_VIEW);
                                        fragmentTransaction.replace(R.id.nav_host_fragment, homeFragmentDetail).addToBackStack(null);
                                        fragmentTransaction.commit();
                                        //fragmentTransaction.addToBackStack(null);
                                        //   fragmentManager.beginTransaction().replace(R.id.nav_host_fragment,homeFragmentDetail).commit();

                                        //    Toast.makeText(getActivity().getApplicationContext(), "Fila Número: " + id_fact.getText().toString()+" , "+ruc_fact.getText().toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "NO SE ENCONTRO DOCUMENTO", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}

