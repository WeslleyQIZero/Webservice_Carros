package aulas.ddmi.webservice_carros.control;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import aulas.ddmi.webservice_carros.R;

/**
 * Esta classe é um container para os fragmentos NovoCarroFragment, DetalheCarroFragment e EdicaoCarroFragment.
 * Created by vagner on 11/08/16.
 */
public class CarroActivity extends AppCompatActivity {
    private final String TAG = "Webservice_Carros"; //TAG para o LogCat

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carro);

        //obtém do extras da intent recebida o fragmento que deve abrir
        String msg = (String) getIntent().getCharSequenceExtra("qualFragmentAbrir");
        if(msg.equals("NovoCarroFragment")){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NovoCarroFragment()).commit();
        }else if(msg.equals("DetalheCarroFragment")){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DetalheCarroFragment()).commit();
        }
    }
}
