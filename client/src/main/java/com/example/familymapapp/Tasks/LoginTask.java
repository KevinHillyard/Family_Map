package com.example.familymapapp.Tasks;

import android.os.AsyncTask;

import com.example.familymapapp.Connection.HttpClient;
import com.example.familymapapp.Model.LoginInput;
import com.example.familymapapp.Model.LoginResult;

public class LoginTask extends AsyncTask <LoginInput, Integer, LoginResult> {

    public interface ListenerLogin {
        void onError(Error e);
        void onProgressUpdate(int percent);
        void onLoginComplete(LoginResult result);
    }

    private ListenerLogin listener_login;
    private HttpClient httpClient;

    public LoginTask (ListenerLogin _listener) { listener_login = _listener; }

    @Override
    protected LoginResult doInBackground(LoginInput... inputs) {
        httpClient = new HttpClient();
        LoginResult result = httpClient.loginRequest(inputs[0].getIP_address(), inputs[0].getPort_num(), inputs[0].getUsername(), inputs[0].getPassword());
        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... integers) {

    }

    @Override
    protected void onPostExecute(LoginResult result) {
        listener_login.onLoginComplete(result);
    }
}
