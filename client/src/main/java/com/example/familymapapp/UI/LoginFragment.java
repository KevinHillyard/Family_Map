package com.example.familymapapp.UI;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.familymapapp.Model.AssociatedPeopleAndEvents;
import com.example.familymapapp.Model.LoginInput;
import com.example.familymapapp.Model.LoginResult;
import com.example.familymapapp.Model.Person;
import com.example.familymapapp.Model.RegisterInput;
import com.example.familymapapp.Model.RequestPeopleAndEvents;
import com.example.familymapapp.R;
import com.example.familymapapp.Tasks.LoginTask;
import com.example.familymapapp.Tasks.PeopleAndEventsTask;
import com.example.familymapapp.Tasks.PersonWithIDTask;
import com.example.familymapapp.Tasks.RegisterTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements LoginTask.ListenerLogin,
        PeopleAndEventsTask.ListenerPeopleEvents, PersonWithIDTask.ListenerPersonWithID {

    public interface LoginCompleteListener {
        void completedLogin(boolean success);
    }

    private TextView textViewServerHost;
    private EditText editTextServerHost;
    private TextView textViewServerPort;
    private EditText editTextServerPort;
    private TextView textViewUsername;
    private EditText editTextUsername;
    private TextView textViewPassword;
    private EditText editTextPassword;
    private TextView textViewFirstName;
    private EditText editTextFirstName;
    private TextView textViewLastName;
    private EditText editTextLastName;
    private TextView textViewEmail;
    private EditText editTextEmail;
    private RadioButton radioButtonMale;
    private RadioButton radioButtonFemale;
    private Button buttonLogin;
    private Button buttonRegister;

    private Person loginPerson = null;
    private AssociatedPeopleAndEvents peopleAndEvents;
    private String auth_token;

    private LoginCompleteListener listener;

    public LoginFragment(LoginCompleteListener loginCompleteListener) {
        listener = loginCompleteListener;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    public Person getLoginPerson() {
        return this.loginPerson;
    }

    public AssociatedPeopleAndEvents getPeopleAndEvents() {
        return this.peopleAndEvents;
    }

    public String getAuth_token() {
        return this.auth_token;
    }

    private void checkEnableRegisterButton() {
        String serverHostInput = editTextServerHost.getText().toString().trim();
        String serverPortInput = editTextServerPort.getText().toString().trim();
        String usernameInput = editTextUsername.getText().toString().trim();
        String passwordInput = editTextPassword.getText().toString().trim();
        String firstNameInput = editTextFirstName.getText().toString().trim();
        String lastNameInput = editTextLastName.getText().toString().trim();
        String emailInput = editTextEmail.getText().toString().trim();
        boolean maleChecked = radioButtonMale.isChecked();
        boolean femaleChecked = radioButtonFemale.isChecked();

        if (maleChecked || femaleChecked) {
            buttonRegister.setEnabled(!serverHostInput.isEmpty() && !serverPortInput.isEmpty() && !usernameInput.isEmpty()
                    && !passwordInput.isEmpty() && !firstNameInput.isEmpty() && !lastNameInput.isEmpty() && !emailInput.isEmpty());
        }
    }

    private void loginButtonClicked() {
        LoginInput loginInput = this.getLoginInput();
        LoginTask loginTask = new LoginTask(this);
        loginTask.execute(loginInput);
    }

    private void registerButtonClicked() {
        RegisterInput registerInput = this.getRegisterInput();
        RegisterTask registerTask = new RegisterTask(this);
        registerTask.execute(registerInput);
    }

    private void successToast(String first_name, String last_name) {
        String message = "Welcome " + first_name + " " + last_name; // I can't use the firstName edit text because that doesn't have to be filled out to log in.
        Context context = getContext();
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        listener.completedLogin(true);
    }

    private void failureToast() {
        Context context = getContext();
        Toast.makeText(context, R.string.login_failed_toast, Toast.LENGTH_SHORT).show();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        textViewServerHost = (TextView) view.findViewById(R.id.text_view_server_host);
        editTextServerHost = (EditText) view.findViewById(R.id.edit_text_server_host);
        textViewServerPort = (TextView) view.findViewById(R.id.text_view_server_port);
        editTextServerPort = (EditText) view.findViewById(R.id.edit_text_server_port);
        textViewUsername = (TextView) view.findViewById(R.id.text_view_username);
        editTextUsername = (EditText) view.findViewById(R.id.edit_text_username);
        textViewPassword = (TextView) view.findViewById(R.id.text_view_password);
        editTextPassword = (EditText) view.findViewById(R.id.edit_text_password);
        textViewFirstName = (TextView) view.findViewById(R.id.text_view_first_name);
        editTextFirstName = (EditText) view.findViewById(R.id.edit_text_first_name);
        textViewLastName = (TextView) view.findViewById(R.id.text_view_last_name);
        editTextLastName = (EditText) view.findViewById(R.id.edit_text_last_name);
        textViewEmail = (TextView) view.findViewById(R.id.text_view_email);
        editTextEmail = (EditText) view.findViewById(R.id.edit_text_email);
        radioButtonMale = (RadioButton) view.findViewById(R.id.radio_button_male);
        radioButtonFemale = (RadioButton) view.findViewById(R.id.radio_button_female);
        buttonLogin = (Button) view.findViewById(R.id.login_button);
        buttonRegister = (Button) view.findViewById(R.id.register_button);

        editTextServerHost.addTextChangedListener(loginTextWatcher);
        editTextServerPort.addTextChangedListener(loginTextWatcher);
        editTextUsername.addTextChangedListener(loginTextWatcher);
        editTextPassword.addTextChangedListener(loginTextWatcher);

        editTextServerHost.addTextChangedListener(registerTextWatcher);
        editTextServerPort.addTextChangedListener(registerTextWatcher);
        editTextUsername.addTextChangedListener(registerTextWatcher);
        editTextPassword.addTextChangedListener(registerTextWatcher);
        editTextFirstName.addTextChangedListener(registerTextWatcher);
        editTextLastName.addTextChangedListener(registerTextWatcher);
        editTextEmail.addTextChangedListener(registerTextWatcher);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButtonClicked();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerButtonClicked();
            }
        });

        return view;
    }

    public void checkRegisterInfo(View view) {
        checkEnableRegisterButton();
    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String serverHostInput = editTextServerHost.getText().toString().trim();
            String serverPortInput = editTextServerPort.getText().toString().trim();
            String usernameInput = editTextUsername.getText().toString().trim();
            String passwordInput = editTextPassword.getText().toString().trim();

            buttonLogin.setEnabled(!serverHostInput.isEmpty() && !serverPortInput.isEmpty()
                    && !usernameInput.isEmpty() && !passwordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher registerTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkEnableRegisterButton();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public LoginInput getLoginInput() {
        LoginInput loginInput = new LoginInput();
        loginInput.setIP_address(editTextServerHost.getText().toString().trim());
        loginInput.setPort_num(editTextServerPort.getText().toString().trim());
        loginInput.setUsername(editTextUsername.getText().toString().trim());
        loginInput.setPassword(editTextPassword.getText().toString().trim());

        return loginInput;
    }

    public RegisterInput getRegisterInput() {
        RegisterInput regIn = new RegisterInput();
        regIn.setIP_address(editTextServerHost.getText().toString().trim());
        regIn.setPort_num(editTextServerPort.getText().toString().trim());
        regIn.setUsername(editTextUsername.getText().toString().trim());
        regIn.setPassword(editTextPassword.getText().toString().trim());
        regIn.setFirst_name(editTextFirstName.getText().toString().trim());
        regIn.setLast_name(editTextLastName.getText().toString().trim());
        regIn.setEmail(editTextEmail.getText().toString().trim());
        if (radioButtonFemale.isChecked()) {
            regIn.setGender('f');
        }
        else {
            regIn.setGender('m');
        }

        return regIn;
    }

    @Override
    public void onError(Error e) {

    }

    @Override
    public void onProgressUpdate(int percent) {

    }

    @Override
    public void onLoginComplete(LoginResult result) {
        if (result.getAuthToken() != null) {
            this.auth_token = result.getAuthToken();
            RequestPeopleAndEvents requestPeopleAndEvents = new RequestPeopleAndEvents();
            requestPeopleAndEvents.setAuth_token(result.getAuthToken());
            requestPeopleAndEvents.setServer_host(editTextServerHost.getText().toString().trim());
            requestPeopleAndEvents.setServer_port(editTextServerPort.getText().toString().trim());
            requestPeopleAndEvents.setPerson_ID(result.getPersonID());
            PersonWithIDTask personWithIDTask = new PersonWithIDTask(this);
            personWithIDTask.execute(requestPeopleAndEvents);
            PeopleAndEventsTask peopleAndEventsTask = new PeopleAndEventsTask(this);
            peopleAndEventsTask.execute(requestPeopleAndEvents);
        }
        else {
            this.failureToast();
        }
    }

    @Override
    public void onLoadError(Error e) {

    }

    @Override
    public void onLoadUpdate(int percent) {

    }

    @Override
    public void onLoadComplete(AssociatedPeopleAndEvents result) {
        if (result != null) {
            this.peopleAndEvents = result;
            if ((this.loginPerson.getFirst_name() != null) && (this.loginPerson.getLast_name() != null)) {
                successToast(this.loginPerson.getFirst_name(), this.loginPerson.getLast_name());
            }
            else {
                this.failureToast();
            }
        }
        else {
            this.failureToast();
        }
    }

    @Override
    public void onPersonIDError(Error e) {

    }

    @Override
    public void onPersonIDUpdate(int percent) {

    }

    @Override
    public void onPersonIDComplete(Person result) {
        this.loginPerson = result;
    }
}
