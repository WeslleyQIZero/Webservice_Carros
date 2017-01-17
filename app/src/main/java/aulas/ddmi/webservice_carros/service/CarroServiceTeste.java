package aulas.ddmi.webservice_carros.service;

import java.util.ArrayList;
import java.util.List;

import aulas.ddmi.webservice_carros.R;
import aulas.ddmi.webservice_carros.model.Carro;


public class CarroServiceTeste {
    private static final String TAG = "CarroServiceTeste";
    private static List<Carro> carros_todos = new ArrayList<>();

    public static List<Carro> getCarros(String tipo) {
        List<Carro> carros = new ArrayList<>();
        if(!tipo.equals("Todos")){
            for (int i = 0; i < 20; i++) {
                Carro c = new Carro();
                c.nome = "Carro " + tipo + ": " + i;
                c.desc = "Desc " + i;
                //Você deve alterar o atributo da classe de modelo para int antes de
                //utilizar este gerador de dados fictícios
                //c.urlFoto = R.drawable.car_background;
                carros.add(c);
            }
            carros_todos.addAll(carros);
            return carros;
        }

        return carros_todos;

    }
}