package aulas.ddmi.webservice_carros.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import aulas.ddmi.webservice_carros.R;
import aulas.ddmi.webservice_carros.adapter.TabsAdapter;

/**
 * Esta Activity inicializa a app. O seu conteúdo é controlado pelo fragmento CarrosFragment.
 * Created by vagner on 11/08/16.
 */
public class CarrosActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        TabLayout.OnTabSelectedListener {

    //o botão para fazer aparecer e desaparecer a NavigationView
    protected ActionBarDrawerToggle toggle;
    //paginação de view para responder ao swipe para direita ou para esquerda
    private ViewPager viewPager;

    /*
        Método do ciclo de vida da Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //associa um layout a esta Activity
        setContentView(R.layout.activity_carros);

        //mapeia a Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //insere a ToolBar como ActionBar (o estilo deve estar como .NoActionBar, veja o arquivo values/styles).
        setSupportActionBar(toolbar);

        //mapeia o FloatButton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //adiciona um tratador de eventos ao FloatButton
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //configura uma Intent explícita
                Intent intent = new Intent(CarrosActivity.this, CarroActivity.class);
                //indica para a outra Activity qual o fragmento deve abrir
                intent.putExtra("qualFragmentAbrir", "CarroNovoFragment");
                //chama outra Activity
                startActivity(intent);
            }
        });

        //Mapeia o DrawerLayout, local onde está a NavigationView
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //cria e configura o botão Toggle no canto esquerdo superior da UI
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Mapeia e configura a NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    /*
        Método do ciclo de vida da Activity.
     */
    @Override
    protected void onResume() {
        super.onResume();
        /*
            Colocar o carregamento do viewPager aqui faz com que as operações REST
            sejam imediatamente percebidas pelo usuário.
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*
        Trata eventos dos itens de menu da Navigation Drawer.
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


    /*
        Trata eventos das Tabs, abas da navegabilidade.
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //seta a página atual na ViewPager
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
