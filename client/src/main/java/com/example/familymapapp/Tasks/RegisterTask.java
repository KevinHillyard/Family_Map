package com.example.familymapapp.Tasks;

import android.os.AsyncTask;

import com.example.familymapapp.Connection.HttpClient;
import com.example.familymapapp.Model.LoginInput;
import com.example.familymapapp.Model.LoginResult;
import com.example.familymapapp.Model.RegisterInput;

public class RegisterTask extends AsyncTask <RegisterInput, Integer, LoginResult> {

    private LoginTask.ListenerLogin listener_login;
    private HttpClient httpClient;

    public RegisterTask (LoginTask.ListenerLogin _listener) { listener_login = _listener; }

    @Override
    protected LoginResult doInBackground(RegisterInput... inputs) {
        httpClient = new HttpClient();
        LoginResult result = httpClient.registerRequest(inputs[0]);
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
