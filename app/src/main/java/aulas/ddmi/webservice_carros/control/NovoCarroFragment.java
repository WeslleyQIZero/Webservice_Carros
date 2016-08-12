package aulas.ddmi.webservice_carros.control;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.IOException;

import aulas.ddmi.webservice_carros.R;
import aulas.ddmi.webservice_carros.model.Carro;
import aulas.ddmi.webservice_carros.model.CarroService;
import aulas.ddmi.webservice_carros.util.AlertUtils;

/**
 * Created by vagner on 25/05/16.
 */
public class NovoCarroFragment extends Fragment {

    private final String TAG = "Webservice_Carros"; //TAG para o LogCat
    private Carro carro; //uma instância da classe Carro com escopo global para utilização em membros da classe
    private ProgressBar progressBarImg;  //uma progressbar para informar o processamento REST
    //componentes <-> objeto carro
    private RadioButton rbClassicos, rbEsportivos, rbLuxo; //campos referente ao tipo do objeto carro
    private EditText editTextNome; //campo referente ao atributo nome do objeto carro
    private EditText editTextDescricao; //campo referente ao atributo descrição do objeto carro
    private EditText editTextLatitude;  //campo referente ao atributo latitude do objeto carro
    private EditText editTextLongitude; //campo referente ao atributo longitude do objeto carro
    private EditText editTextURLFoto;  //campo referente ao atributo url_foto do objeto carro
    private EditText editTextURLVideo; //campo referente ao atributo url_video do objeto carro

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true); //informa ao sistema que o fragment irá adicionar botões na ActionBar

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novocarro, container, false); //infla o xml da UI e associa ao Fragment

        ((CarroActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_fragment_novocarro);  //um título para a janela

        //Cria uma instancia da classe de modelo
        carro = new Carro();

        //mapeia e inicializa os componentes da UI
        //Card0
        ImageView imageView = (ImageView) view.findViewById(R.id.imv_card0_frnovocarro);
        progressBarImg = (ProgressBar) view.findViewById(R.id.pb_card0_frnovocarro);
        progressBarImg.setVisibility(View.INVISIBLE);
        //Card1
        rbClassicos = (RadioButton) view.findViewById(R.id.rb_classicos_card1_frnovocarro);
        rbClassicos.setChecked(true);
        rbEsportivos = (RadioButton) view.findViewById(R.id.rb_esportivo_card1_frnovocarro);
        rbLuxo = (RadioButton) view.findViewById(R.id.rb_luxo_card1_frnovocarro);
        //Card2
        editTextNome = (EditText) view.findViewById(R.id.etNome_card2_frnovocarro);
        editTextDescricao = (EditText) view.findViewById(R.id.etDescricao_card2_frnovocarro);
        //Card3
        editTextLatitude = (EditText) view.findViewById(R.id.etlatitude_card3_frnovocarro);
        editTextLongitude = (EditText) view.findViewById(R.id.etlongitude_card3_frnovocarro);
        //Card4
        editTextURLFoto = (EditText) view.findViewById(R.id.eturlfoto_card4_frnovocarro);
        editTextURLVideo = (EditText) view.findViewById(R.id.eturlvideo_card4_frnovocarro);

        return view;
    }

    /*
        Infla o menu.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_novocarro, menu);
    }

    /*
        Trata eventos dos itens de menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem_salvar:{
                //carrega os dados do formulário no objeto
                if(!editTextNome.getText().toString().isEmpty()) {
                    carro.id = 0L; //evita problemas com a conversão do GSON
                    carro.nome = editTextNome.getText().toString();
                    carro.desc = editTextDescricao.getText().toString();
                    carro.latitude = editTextLatitude.getText().toString();
                    carro.longitude = editTextLongitude.getText().toString();
                    if(editTextURLFoto.getText().toString().isEmpty()){
                        carro.urlFoto = null; //para evitar problemas com bibliotecas de terceiros
                    }else{
                        carro.urlFoto = editTextURLFoto.getText().toString();
                    }
                    if(editTextURLVideo.getText().toString().isEmpty()){
                        carro.urlVideo = null; //para evitar problemas com bibliotecas de terceiros
                    }else{
                        carro.urlVideo = editTextURLVideo.getText().toString();
                    }
                    if(rbClassicos.isChecked()){
                        carro.tipo = getContext().getResources().getString(R.string.tipo_classicos);
                    }else if(rbEsportivos.isChecked()){
                        carro.tipo = getContext().getResources().getString(R.string.tipo_esportivos);
                    }else {
                        carro.tipo = getContext().getResources().getString(R.string.tipo_luxo);
                    }
                    new CarrosTask().execute(); //executa a operação REST POST em uma thread AsyncTask
                }else{
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.val_dadosinputs), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

        return false;
    }

    /*
        Classe interna que extende uma AsyncTask.
        Lembrando: A AsyncTask gerência a thread que acessa os dados no web service.
    */
    private class CarrosTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //emite uma caixa processando
            AlertUtils.showWait(getContext(), R.string.app_name, R.string.progressdialog_wait);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                return CarroService.post("/carros", carro); //URL_BASE + /carros
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                AlertUtils.dismissWait(); //fecha a caixa processando
                //faz aparecer uma caixa de diálogo confirmando a operação
                AlertUtils.showOk(getContext(), R.string.app_name, R.string.alertdialog_message_rest);
                //volta para a lista de carros
                getActivity().finish();
            }
        }
    }//fim classe interna

}//fim classe
