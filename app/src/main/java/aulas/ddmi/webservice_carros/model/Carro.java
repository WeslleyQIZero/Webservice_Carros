package aulas.ddmi.webservice_carros.model;

/**
 * Created by vagner on 15/05/16.
 */

import android.graphics.Bitmap;
import android.os.Parcelable;

import java.io.Serializable;

public class Carro implements Serializable {
    private static final long serialVersionUID = 6601006766832473959L;

    public Long id;
    public String nome;
    public String desc;
    public String tipo;
    public String urlFoto;
    public String urlVideo;
    public String latitude;
    public String longitude;

    @Override
    public String toString() {
        return "Carro{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", desc='" + desc + '\'' +
                ", tipo='" + tipo + '\'' +
                ", urlFoto='" + urlFoto + '\'' +
                ", urlVideo='" + urlVideo + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
