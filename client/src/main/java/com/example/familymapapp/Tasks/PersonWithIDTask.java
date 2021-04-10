package com.example.familymapapp.Tasks;

import android.os.AsyncTask;

import com.example.familymapapp.Connection.HttpClient;
import com.example.familymapapp.Model.Person;
import com.example.familymapapp.Model.RequestPeopleAndEvents;

public class PersonWithIDTask extends AsyncTask <RequestPeopleAndEvents, Integer, Person> {

    public interface ListenerPersonWithID {
        void onPersonIDError(Error e);
        void onPersonIDUpdate(int percent);
        void onPersonIDComplete(Person result);
    }

    private ListenerPersonWithID listenerPersonWithID;

    public PersonWithIDTask (ListenerPersonWithID _listener) { listenerPersonWithID = _listener; }

    @Override
    protected Person doInBackground(RequestPeopleAndEvents... requestPeopleAndEvents) {
        Person result;
        HttpClient httpClient = new HttpClient();
        result = httpClient.getPersonWithID(requestPeopleAndEvents[0].getAuth_token(), requestPeopleAndEvents[0].getServer_host(),
                requestPeopleAndEvents[0].getServer_port(), requestPeopleAndEvents[0].getPerson_ID());
        return result;
    }

    @Override
    protected void onPostExecute(Person result) {
        listenerPersonWithID.onPersonIDComplete(result);
    }
}
