package aulas.ddmi.webservice_carros.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import aulas.ddmi.webservice_carros.model.Carro;
import aulas.ddmi.webservice_carros.util.HttpHelper;

import java.lang.reflect.Type;


/**
 *  Esta classe realiza o "parser" dos dados oriundos do web service no formato JSON para uma List.
 *  @author Vagner Pinto da Silva, baseado em Lecheta(2015).
 */


public class CarroService extends ServiceBase{

    /*
        Obtém a lista de carros em JSON do web service e converte para List<Carro>.
     */
    public static List<Carro> getCarros(String url) throws IOException {
        String json = new HttpHelper().doGet(URL_BASE + url); //obtém o objeto JSON do servidor através de um GET

        //Converte JSON para um List
        Type listType = new TypeToken<ArrayList<Carro>>() {}.getType();
        List<Carro> carros = new Gson().fromJson(json, listType);

        return carros;
    }

    /*
     * Deleta um registro no web service utilizando a operação DELETE, e converte um JSON em rest.Response.
     *
     * {
     * "status": "OK",
     * "msg": "Carro deletado com sucesso"
     * }
     */
    public static boolean delete(String url) throws IOException {
        HttpHelper http = new HttpHelper(); //Cria uma instância de util.HpptHelper
        http.setContentType("application/json; charset=utf-8"); //seta o Content-Type e a codificação de caracteres

        Log.d(TAG, "Delete carro: " + URL_BASE + url); //um log para depurar

        // Request HTTP
        String json = http.doDelete(URL_BASE + url); // Request HTTP, REST DELETE
        Log.d(TAG, "JSON delete: " + json); //um log para depurar

        // Converção JSON em Response
        Gson gson = new Gson(); //cria uma instância de GSON
        Response response = gson.fromJson(json, Response.class); //converte JSON para Response
        if (!response.isOk()) { //testa se o servidor respondeu com 200 (ok)
            throw new IOException("Erro ao excluir o registro: " + response.getMsg()); //lança uma exceção se o retorno do servidor não for 200 (ok)
        }

        // A fazer
        return true;
    }

    /*
        Insere um registro no web service utilizando a operação POST, e converte um JSON em rest.Response.
     */
    public static boolean post(String url, Carro carro) throws IOException {
        String jsonCarro = new Gson().toJson(carro); //parse Carro -> JSON
        Log.d(TAG, ">> saveCarro: " + jsonCarro); //um log para depurar

        HttpHelper http = new HttpHelper(); //Cria uma instância de util.HpptHelper
        http.setContentType("application/json; charset=utf-8"); //seta o Content-Type e a codificação de caracteres

        // Request HTTP
        String json = http.doPost(URL_BASE + url, jsonCarro.getBytes(), "UTF-8"); // Request HTTP, REST PUT
        Log.d(TAG, "<< saveCarro: " + json); //um log para depurar

        Response response = new Gson().fromJson(json, Response.class);
        if (!response.isOk()) { //testa se o servidor respondeu com 200 (ok)
            throw new IOException("Erro ao alterar o registro: " + response.getMsg()); //lança uma exceção se o retorno do servidor não for 200 (ok)
        }

        return true;
    }

    /*
        Altera um registro no web service utilizando a operação PUT, e converte um JSON em rest.Response.
     */
    public static boolean put(String url, Carro carro) throws IOException {
        String jsonCarro = new Gson().toJson(carro); //parse Carro -> JSON
        Log.d(TAG, ">> saveCarro: " + jsonCarro); //um log para depurar

        HttpHelper http = new HttpHelper(); //Cria uma instância de util.HpptHelper
        http.setContentType("application/json; charset=utf-8"); //seta o Content-Type e a codificação de caracteres

        // Request HTTP
        String json = http.doPut(URL_BASE + url, jsonCarro.getBytes(), "UTF-8"); // Request HTTP, REST PUT
        Log.d(TAG, "<< saveCarro: " + json); //um log para depurar

        Response response = new Gson().fromJson(json, Response.class);
        if (!response.isOk()) { //testa se o servidor respondeu com 200 (ok)
            throw new IOException("Erro ao alterar o registro: " + response.getMsg()); //lança uma exceção se o retorno do servidor não for 200 (ok)
        }

        return true;
    }
}
