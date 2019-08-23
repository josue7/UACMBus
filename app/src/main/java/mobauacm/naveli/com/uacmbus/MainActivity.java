package mobauacm.naveli.com.uacmbus;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
//import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Dialog customDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //existeInternet();

        //Se comienza a implementar lo la logica de tab
        Resources res= getResources();

        TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("UACM bus");
        tabs.addTab(spec);

        spec = tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Expreso");
        tabs.addTab(spec);

        tabs.setCurrentTab(0);
        tabs.setHorizontalScrollBarEnabled(true);

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                Log.i("AndroidTabsDemo", "Pulsada pestaña: " + tabId);
                if (tabId.equals("mitab1"))
                    mensaje("UACMBus es de uso exclusivo y gratuito para trabajadores y estudiantes de la UACM presentando credencial.");
                if (tabId.equals("mitab2"))
                    mensaje("RTP tiene un costo de $4.00 en caso de ser Ecológico y $2.00 en caso de ser Naranja o Atenea.");
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*public void existeInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (!(netInfo != null && netInfo.isConnected())) {
            Toast.makeText(this, "Su dispositivo no esta conectado a internet", Toast.LENGTH_LONG).show();
        }
    }*/

    public  void mensaje(String mensaje){
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DatabaseReference baseUacm = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mensaje;
        Intent mainIntent = new Intent();

        int id = item.getItemId();
        final TextView fecha = (TextView) findViewById(R.id.fecha);
        final TextView fecha2 = (TextView) findViewById(R.id.fecha2);
        final TextView estado1 = (TextView) findViewById(R.id.estado1);
        final TextView estado2 = (TextView) findViewById(R.id.estado2);
        final TextView estado3 = (TextView) findViewById(R.id.estado3);
        final TextView estado4 = (TextView) findViewById(R.id.estado4);
        final TextView estado5 = (TextView) findViewById(R.id.estado5);
        //existeInternet();

        if (id == R.id.dia1) {
            mensaje = baseUacm.child("M1");
            //Cambio de Fecha para tab1 y tab2
            mensaje.child("L").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        fecha.setText(dataSnapshot.getValue(String.class));
                        fecha2.setText(dataSnapshot.getValue(String.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.i("Snackbar", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin del cambio de Fecha para tab1 y tab2

            //Cambio de estado 1
            mensaje.child("F1").child("Va").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado1.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado1.setTextColor(estado1.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado1.setTextColor(estado1.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 1

            //Cambio de estado 2
            mensaje.child("F1").child("Vb").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado2.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado2.setTextColor(estado2.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado2.setTextColor(estado2.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 2

            //Cambio de estado 3
            mensaje.child("F1").child("Vc").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado3.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado3.setTextColor(estado3.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado3.setTextColor(estado3.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 3

            //Cambio de estado 4
            mensaje.child("F1").child("Vd").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado4.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado4.setTextColor(estado4.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado4.setTextColor(estado4.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 4

            //Cambio de estado 5
            mensaje.child("F1").child("Ve").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado5.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado5.setTextColor(estado5.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado5.setTextColor(estado5.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 5


            // Handle the camera action
        } else if (id == R.id.dia2) {
            mensaje = baseUacm.child("M2");
            //Cambio de Fecha para tab1 y tab2
            mensaje.child("M").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        fecha.setText(dataSnapshot.getValue(String.class));
                        fecha2.setText(dataSnapshot.getValue(String.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin del cambio de Fecha para tab1 y tab2

            //Cambio de estado 1
            mensaje.child("F2").child("Va").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado1.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado1.setTextColor(estado1.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado1.setTextColor(estado1.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 1

            //Cambio de estado 2
            mensaje.child("F2").child("Vb").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado2.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado2.setTextColor(estado2.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado2.setTextColor(estado2.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 2

            //Cambio de estado 3
            mensaje.child("F2").child("Vc").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado3.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado3.setTextColor(estado3.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado3.setTextColor(estado3.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 3

            //Cambio de estado 4
            mensaje.child("F2").child("Vd").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado4.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado4.setTextColor(estado4.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado4.setTextColor(estado4.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 4

            //Cambio de estado 5
            mensaje.child("F2").child("Ve").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado5.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado5.setTextColor(estado5.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado5.setTextColor(estado5.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 5

        } else if (id == R.id.dia3) {
            mensaje = baseUacm.child("M3");
            //Cambio de Fecha para tab1 y tab2
            mensaje.child("Mi").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        fecha.setText(dataSnapshot.getValue(String.class));
                        fecha2.setText(dataSnapshot.getValue(String.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin del cambio de Fecha para tab1 y tab2

            //Cambio de estado 1
            mensaje.child("F3").child("Va").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado1.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado1.setTextColor(estado1.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado1.setTextColor(estado1.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 1

            //Cambio de estado 2
            mensaje.child("F3").child("Vb").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado2.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado2.setTextColor(estado2.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado2.setTextColor(estado2.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 2

            //Cambio de estado 3
            mensaje.child("F3").child("Vc").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado3.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado3.setTextColor(estado3.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado3.setTextColor(estado3.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 3

            //Cambio de estado 4
            mensaje.child("F3").child("Vd").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado4.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado4.setTextColor(estado4.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado4.setTextColor(estado4.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 4

            //Cambio de estado 5
            mensaje.child("F3").child("Ve").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado5.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado5.setTextColor(estado5.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado5.setTextColor(estado5.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 5


        } else if (id == R.id.dia4) {
            mensaje = baseUacm.child("M4");
            //Cambio de Fecha para tab1 y tab2
            mensaje.child("J").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        fecha.setText(dataSnapshot.getValue(String.class));
                        fecha2.setText(dataSnapshot.getValue(String.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin del cambio de Fecha para tab1 y tab2

            //Cambio de estado 1
            mensaje.child("F4").child("Va").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado1.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado1.setTextColor(estado1.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado1.setTextColor(estado1.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 1

            //Cambio de estado 2
            mensaje.child("F4").child("Vb").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado2.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado2.setTextColor(estado2.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado2.setTextColor(estado2.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 2

            //Cambio de estado 3
            mensaje.child("F4").child("Vc").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado3.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado3.setTextColor(estado3.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado3.setTextColor(estado3.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 3

            //Cambio de estado 4
            mensaje.child("F4").child("Vd").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado4.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado4.setTextColor(estado4.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado4.setTextColor(estado4.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 4

            //Cambio de estado 5
            mensaje.child("F4").child("Ve").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado5.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado5.setTextColor(estado5.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado5.setTextColor(estado5.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 5


        } else if (id == R.id.dia5) {
            mensaje = baseUacm.child("M5");
            //Cambio de Fecha para tab1 y tab2
            mensaje.child("V").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        fecha.setText(dataSnapshot.getValue(String.class));
                        fecha2.setText(dataSnapshot.getValue(String.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin del cambio de Fecha para tab1 y tab2

            //Cambio de estado 1
            mensaje.child("F5").child("Va").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado1.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado1.setTextColor(estado1.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado1.setTextColor(estado1.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 1

            //Cambio de estado 2
            mensaje.child("F5").child("Vb").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado2.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado2.setTextColor(estado2.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado2.setTextColor(estado2.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 2

            //Cambio de estado 3
            mensaje.child("F5").child("Vc").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado3.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado3.setTextColor(estado3.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado3.setTextColor(estado3.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 3

            //Cambio de estado 4
            mensaje.child("F5").child("Vd").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado4.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado4.setTextColor(estado4.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado4.setTextColor(estado4.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 4

            //Cambio de estado 5
            mensaje.child("F5").child("Ve").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        estado5.setText(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals("A Tiempo"))
                            estado5.setTextColor(estado5.getContext().getResources().getColor(R.color.grisText));
                        else
                            estado5.setTextColor(estado5.getContext().getResources().getColor(R.color.rojoText));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    View view;
                    Log.d("AndroidTabsDemo", "Fallo al leer el valor : " + databaseError.toException());

                }
            });
            //Fin de cambio de estado 5

        } else if (id == R.id.develop){
            //FragmentManager fragmentManager = getSupportFragmentManager();
            //fragmentManager.executePendingTransactions();
            android.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.executePendingTransactions();
            Desarrollador dialogo = new Desarrollador();
            dialogo.show(fragmentManager, "");
        } else if (id == R.id.comentario){
            mainIntent = new Intent().setClass(MainActivity.this, Comentario.class);
            startActivity(mainIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
