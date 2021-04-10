package com.example.familymapapp.Tasks;

import android.os.AsyncTask;

import com.example.familymapapp.Connection.HttpClient;
import com.example.familymapapp.Model.AssociatedPeopleAndEvents;
import com.example.familymapapp.Model.RequestPeopleAndEvents;

public class PeopleAndEventsTask extends AsyncTask <RequestPeopleAndEvents, Integer, AssociatedPeopleAndEvents> {
    public interface ListenerPeopleEvents {
        void onLoadError(Error e);
        void onLoadUpdate(int percent);
        void onLoadComplete(AssociatedPeopleAndEvents result);
    }

    private ListenerPeopleEvents listenerPeopleEvents;

    public PeopleAndEventsTask (ListenerPeopleEvents _listener) { listenerPeopleEvents = _listener; }

    @Override
    protected AssociatedPeopleAndEvents doInBackground(RequestPeopleAndEvents... strings) {
        AssociatedPeopleAndEvents result;
        HttpClient httpClient = new HttpClient();
        result = httpClient.loadPeopleAndEvents(strings[0]);

        return result;
    }

    @Override
    protected void onPostExecute(AssociatedPeopleAndEvents result) {
        listenerPeopleEvents.onLoadComplete(result);
    }
}
