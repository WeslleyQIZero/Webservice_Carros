package aulas.ddmi.webservice_carros.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import aulas.ddmi.webservice_carros.R;
import aulas.ddmi.webservice_carros.activity.CarroActivity;

/**
 * Created by vagner on 13/09/16.
 */
public class BaseFragment extends Fragment {
    //TAG para o LogCat
    protected static final String TAG = "web_service_carros";
    //uma caixa de progressão com uma animação de progressão
    private ProgressDialog mProgressDialog;

    //Toast
    protected void toast(int msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    //Mensagem de alerta com botão Ok
    protected void alertOk(int title, int message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext()); //cria um buider
        builder.setTitle(title).setMessage(message); //insere o título e a mensagem
        // Adiciona o botão e trata o evento onClick
        builder.setPositiveButton(R.string.alertdialog_buttom_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(getActivity() instanceof CarroActivity){
                    getActivity().finish();
                }
            }
        });
        AlertDialog dialog = builder.create(); //cria o alerta
        dialog.show(); //apresenta a caixa de diálogo

    }

    //Emite uma ProgressDialog
    //Uma caixa com uma mensagem de progressão e uma barra de progressão
    public void  showWait(final Context context, int title, int message){
        //cria e configura a caixa de diálogo e progressão
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(context.getResources().getString(message));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.show();
    }

    //Faz Dismiss na ProgressDialog
    public void dismissWait(){
        mProgressDialog.dismiss();
    }

    /**
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     */
    public boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return false;
    }
}
