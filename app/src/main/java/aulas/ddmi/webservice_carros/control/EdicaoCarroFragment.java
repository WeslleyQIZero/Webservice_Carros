package aulas.ddmi.webservice_carros.control;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import aulas.ddmi.webservice_carros.R;
import aulas.ddmi.webservice_carros.model.Carro;
import aulas.ddmi.webservice_carros.model.CarroService;
import aulas.ddmi.webservice_carros.util.AlertUtils;

/**
 * Created by vagner on 15/05/16.
 */
public class EdicaoCarroFragment extends Fragment {

    private final String TAG = "Webservice_Carros"; //TAG para o LogCat
    private Carro carro; //uma instância da classe Carro com escopo global para utilização em membros da classe
    private ProgressBar progressBarRest;  //uma progressbar para informar o processamento REST
    //componentes <-> objeto carro
    private RadioButton rbClassicos, rbEsportivos, rbLuxo; //campos referente ao tipo do objeto carro
    private EditText editTextNome; //campo referente ao atributo nome do objeto carro
    private EditText editTextDescricao; //campo referente ao atributo descrição do objeto carro
    private EditText editTextLatitude;  //campo referente ao atributo latitude do objeto carro
    private EditText editTextLongitude; //campo referente ao atributo longitude do objeto carro



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true); //informa ao sistema que o fragment irá adicionar botões na ActionBar

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //infla o layout
        View view = inflater.inflate(R.layout.fragment_edicaocarro, container, false);

        ((CarroActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_fragment_edicaocarro);  //um título para a janela

        //obtém o objeto passado como argumento na chamada deste fragment
        carro = CarrosActivity.carro;
        Log.d(TAG, "Dados do registro = " + carro); //um log para depurar

        //carrega a imagem e controla o progressbar
        Log.d(TAG, "URL foto = " + carro.urlFoto); //um log para depurar
        ImageView imageView = (ImageView) view.findViewById(R.id.imv_card0_fredicaocarro);
        final ProgressBar progressBarCard0 = (ProgressBar) view.findViewById(R.id.pb_card0_fredicaocarro);
        Picasso.with(getContext()).load(carro.urlFoto).fit().into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBarCard0.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                progressBarCard0.setVisibility(View.GONE);
            }
        });

        //carrega o tipo nos RadioButtons
        Log.d(TAG, "Tipo = " + carro.tipo); //um log para depurar
        rbClassicos = (RadioButton) view.findViewById(R.id.rbclassicos_card1_fredicaocarro);
        rbEsportivos = (RadioButton) view.findViewById(R.id.rbesportivos_card1_fredicaocarro);
        rbLuxo = (RadioButton) view.findViewById(R.id.rbluxo_card1_fredicaocarro);
        if (carro.tipo.equals("classicos")) {
            rbClassicos.setChecked(true);
        } else if (carro.tipo.equals("esportivos")) {
            rbEsportivos.setChecked(true);
        } else {
            rbLuxo.setChecked(true);
        }

        //carrega o nome e a descrição
        Log.d(TAG, "Nome = " + carro.nome + "\nDescrição = " + carro.desc); //um log para depurar
        editTextNome = (EditText) view.findViewById(R.id.etNome_card2_fredicaocarro);
        editTextDescricao = (EditText) view.findViewById(R.id.etDescricao_card2_fredicaocarro);
        editTextNome.setText(carro.nome);
        editTextDescricao.setText(carro.desc);

        //carrega a latitude e a longitude
        Log.d(TAG, "Latitude = " + carro.latitude + "\nlongitude = " + carro.longitude); //um log para depurar
        editTextLatitude = (EditText) view.findViewById(R.id.etlatitude_card3_fredicaocarro);
        editTextLongitude = (EditText) view.findViewById(R.id.etlongitude_card3_fredicaocarro);
        editTextLatitude.setText(carro.latitude);
        editTextLongitude.setText(carro.longitude);

        //ProgressBar
        progressBarRest = (ProgressBar) view.findViewById(R.id.pb_fredicaocarro);
        progressBarRest.setVisibility(View.INVISIBLE);

        return view;
    }

    /*
        Infla o menu.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_edicaocarro, menu);
    }

    /*
        Trata eventos dos itens de menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuitem_salvar:{
                //carrega os dados do formulário no objeto
                carro.nome = editTextNome.getText().toString();
                carro.desc = editTextDescricao.getText().toString();
                carro.latitude = editTextLatitude.getText().toString();
                carro.longitude = editTextLongitude.getText().toString();
                if(rbClassicos.isChecked()){
                    carro.tipo = getContext().getResources().getString(R.string.tipo_classicos);
                }else if(rbEsportivos.isChecked()){
                    carro.tipo = getContext().getResources().getString(R.string.tipo_esportivos);
                }else {
                    carro.tipo = getContext().getResources().getString(R.string.tipo_luxo);
                }
                new CarrosTask().execute("put"); //executa a operação REST PUT em uma thread AsyncTask
                break;
            }
            case R.id.menuitem_excluir:{
                new CarrosTask().execute("delete"); //executa a operação REST DELETE em uma thread AsyncTask
                break;
            }
        }
        return false;
    }


    /*
        Classe interna que extende uma AsyncTask.
        Lembrando: A AsyncTask gerência a thread que acessa os dados no web service.
    */
    private class CarrosTask extends AsyncTask<String, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarRest.setVisibility(View.VISIBLE); //faz aparecer a ProgressBar
        }

        @Override
        protected Boolean doInBackground(String... params) {
            //executa a tarefa em background, em uma thread exclusiva para esta tarefa.
            if(params[0].equals("put")){
                try {
                    return CarroService.put("/carros", carro); //URL_BASE
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                if(params[0].equals("delete")){
                    try {
                        return CarroService.delete("/carros/" + carro.id); //URL_BASE + /carros/id
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                progressBarRest.setVisibility(View.INVISIBLE); //faz desaparecer a ProgressBar
                //faz aparecer uma caixa de diálogo confirmando a operação
                AlertUtils.showOk(getContext(), R.string.app_name, R.string.alertdialog_message_rest);
                //volta para a lista de carros
                getActivity().finish();
            }
        }
    }//fim classe interna

}//fim classe externa
