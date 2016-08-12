package aulas.ddmi.webservice_carros.control;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import aulas.ddmi.webservice_carros.R;
import aulas.ddmi.webservice_carros.adapter.TabsAdapter;
import aulas.ddmi.webservice_carros.model.Carro;

public class CarrosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener {

    private final String TAG = "Webservice_Carros"; //TAG para o LogCat
    protected ActionBarDrawerToggle toggle; //para acesso em um fragment
    private ViewPager viewPager; //paginação de view para responder ao swipe, para direita ou para esquerda
    protected static Carro carro; //um objeto estático para armazenar o carro clicado pelo usuário

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carros);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); //mapeia a Toolbar
        setSupportActionBar(toolbar); //a adiciona na Actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //habilita a navegação pelo botão esquerdo da ActionBar (padrão Android)

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab); //isso foi alterado para o escopo global, assim o localiza no NovoCarroFragment
        //adiciona um tratador de eventos ao FloatButtom
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarrosActivity.this, CarroActivity.class); //configura uma Intent explícita
                intent.putExtra("qualFragmentAbrir", "NovoCarroFragment");
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
            Colocar o carregamento do viewPager aqui faz com que as operações REST seja imediatamente percebidas pelo usuário.
         */
        // ViewPager
        viewPager = (ViewPager) findViewById(R.id.tabanim_viewpager);
        TabsAdapter adapter = new TabsAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);

        // Tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setOnTabSelectedListener(this);

    }

    /*
                Trata eventos dos itens de menu da Navigation Drawer]0
             */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_fragment_list_carros:{
                //Não faz nada nesta Activity
                break;
            }
            case R.id.nav_fragment_sobre:{
                Toast.makeText(CarrosActivity.this, "Chamar a SobreFragment.", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_settings:{
                Toast.makeText(CarrosActivity.this, "Chamar a ConfiguracoesFragment.", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
