package aulas.ddmi.webservice_carros.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import aulas.ddmi.webservice_carros.R;
import aulas.ddmi.webservice_carros.activity.CarroActivity;
import aulas.ddmi.webservice_carros.activity.CarrosActivity;
import aulas.ddmi.webservice_carros.adapter.CarroAdapter;
import aulas.ddmi.webservice_carros.model.Carro;
import aulas.ddmi.webservice_carros.service.CarroService;

/**
 * Este fragmento é responsável pelo conteúdo onde são listados os carros. A navegabilidade
 * é responsabilidade da Activity que "infla" este fragmento.
 * Created by vagner on 15/05/16.
 */
public class CarrosFragment extends BaseFragment
        implements SearchView.OnQueryTextListener{

    //o container onde serão apresentados os dados
    protected RecyclerView recyclerView;
    //o SwipeRefresh. O objeto que identificará o movimento de swipe e reagirá
    private SwipeRefreshLayout swipeRefreshLayout;
    //uma animação para indicar processando
    private ProgressBar progressBar;
    //lista dos carros, utilizada no método de tratamento do onClick() do item da RecyclerView
    private List<Carro> carros;
    //o tipo de carro que é recebido como argumento na construção do fragmento
    private String tipo;

    /*
        Método do ciclo de vida do Fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //se há argumentos, o armazena para filtar na lista de carros
        //este tipo de carro vem do TabsAdapter
        if (getArguments() != null) {
            this.tipo = getArguments().getString("tipo");
        }

        //informa ao sistema que o fragment irá adicionar itens de menu na ActionBar
        setHasOptionsMenu(true);

    }

    /*
        Método do ciclo de vida do Fragment.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish(); //finaliza a app
    }

    /*
        Método do ciclo de vida do Fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //infla o xml da UI e associa ao Fragment
        View view = inflater.inflate(R.layout.fragment_listcarros, container, false);

        //um título para a janela
        ((CarrosActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_fragment_listcarros);

        //configura a RecyclerView
        //mapeia o RecyclerView do layout.
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        //associa um gerenciador de layout Linear ao recyclerView.
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //associa um tipo de animação ao recyclerView.
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        //configura o SwipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swinperefrechlayout);
        swipeRefreshLayout.setOnRefreshListener(OnRefreshListener());
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);

        //Cria um ProgressBar para mostrar uma animação de processando
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        //se houver conexão com a internet, wi-fi ou 3G ...
        if (isNetworkAvailable(getContext())) {
            new CarrosTask().execute(); //executa a operação REST GET em uma thread AsyncTask
        } else {
            alertOk(getContext(), R.string.title_conectividade, R.string.msg_conectividade);
        }

        return view;

    }

    /*
        Infla o menu da ActionBar.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //infla o menu na ActionBar
        inflater.inflate(R.menu.menu_fragment_carros, menu);
        //obtém a SearchView
        SearchView mySearchView = (SearchView) menu.findItem(R.id.menuitem_pesquisar).getActionView();
        //coloca um hint na SearchView
        mySearchView.setQueryHint(getResources().getString(R.string.hint_searchview));
        //cadastra o tratador de eventos na lista de tratadores da SearchView
        mySearchView.setOnQueryTextListener(this);
    }

    /*
        Trata eventos da SearchView
     */
    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        //uma lista para nova camada de modelo da RecyclerView
        List<Carro> carroList = new ArrayList<>();

        //um for-eatch na lista de carros
        for(Carro carro : carros){
            //se o nome do carro começa com o texto digitado
            if(carro.nome.contains(newText)) {
                //adiciona o carro na nova lista
                carroList.add(carro);
            }
        }

        //coloca a nova lista como fonte de dados do novo adaptador da RecyclerView
        //(Context, fonte de dados, tratador do evento onClick)
        recyclerView.setAdapter(new CarroAdapter(getContext(), carroList, onClickCarro()));

        return true;
    }

    /*
        Classe interna que extende uma AsyncTask.
        Lembrando: A AsyncTask gerencia a thread que acessa os dados no web service.
    */
    private class CarrosTask extends AsyncTask<Void, Void, List<Carro>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //faz com que a ProgressBar apareça para o usuário
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Carro> doInBackground(Void... params) {
            //busca os carros em background, em uma thread exclusiva para esta tarefa.
            try {
                if(CarrosFragment.this.tipo.equals(getString(R.string.tipo_todos))){
                    return CarroService.getCarros("/carros");
                }else if(CarrosFragment.this.tipo.equals(getString(R.string.tipo_classicos))){
                    return CarroService.getCarros("/carros/tipo/" + CarrosFragment.this.tipo);
                }else if(CarrosFragment.this.tipo.equals(getString(R.string.tipo_esportivos))){
                    return CarroService.getCarros("/carros/tipo/" + CarrosFragment.this.tipo);
                }
                    return CarroService.getCarros("/carros/tipo/" + CarrosFragment.this.tipo);

            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Exceção ao obter a lista de carros, método .doInBackground()");
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Carro> carros) {
            super.onPostExecute(carros);
            if (carros != null) {
                Log.d(TAG, "Quantidade de carros no onPostExecute(): " + carros.size());
                //atualiza a view na UIThread
                //(Context, fonte de dados, tratador do evento onClick)
                //coloca a lista retornada pelo web service como fonte de dados do adaptador da RecyclerView
                recyclerView.setAdapter(new CarroAdapter(getContext(), carros, onClickCarro()));
                //copia a lista de carros para uso no tratador do onClick
                CarrosFragment.this.carros = carros;
                //para a animação da swipeRefrech
                swipeRefreshLayout.setRefreshing(false);
                //faz com que a ProgressBar desapareça para o usuário
                progressBar.setVisibility(View.INVISIBLE);
            }else{
                //faz com que a ProgressBar desapareça para o usuário
                progressBar.setVisibility(View.INVISIBLE);
                //avisa o usuário da falha no download
                alertOk(getContext(), R.string.title_erro, R.string.msg_erro_falhanodownload); //faz aparecer o AlertDialog para o usuário
            }
        }
    }

    /*
        Este método utiliza a interface declarada na classe CarroAdapter para tratar
        o evento onClick do item da lista.
     */
    protected CarroAdapter.CarroOnClickListener onClickCarro() {
        //chama o contrutor da interface (implícito) para cria uma instância da interface declarada no adaptador.
        return new CarroAdapter.CarroOnClickListener() {
            // Aqui trata o evento onItemClick.
            @Override
            public void onClickCarro(View view, int idx) {
                //armazena o carro que foi clicado na RecyclerView
                Carro carro = carros.get(idx);
                //chama outra Activity para detalhar ou editar o carro clicado pelo usuário
                //configura uma Intent explícita
                Intent intent = new Intent(getContext(), CarroActivity.class);
                //insere um extra com a referência para o objeto Carro
                intent.putExtra("carro", carro);
                //indica para a outra Activity qual o fragmento deve abrir
                intent.putExtra("qualFragmentAbrir", "DetalheCarroFragment");
                //chama outra Activity
                startActivity(intent);
            }
        };
    }
    /*
        Este método trata o evento onRefresh() do SwipeRefreshLayout.
        Ele acontece quando o usuário faz um swipe com o dedo para baixo na View.
     */
    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable(getContext())) { //se houver conexão com a internet, wi-fi ou 3G ...
                    new CarrosTask().execute(); //cria uma instância de AsyncTask
                } else {
                    alertOk(getContext(), R.string.title_conectividade, R.string.msg_conectividade);
                    recyclerView.setAdapter(new CarroAdapter(getContext(), new ArrayList<Carro>(), onClickCarro()));
                }

            }
        };
    }
}
