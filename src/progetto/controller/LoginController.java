package progetto.controller;

import progetto.exception.DAOException;
import progetto.model.domain.Credenziali;
import progetto.model.dao.LoginProcedureDAO;
import progetto.view.LoginView;
import java.io.IOException;

public class LoginController implements Controller {
    Credenziali cred = null;

    @Override
    public void start() {
        try {
            cred = LoginView.authenticate();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        try {
            cred = new LoginProcedureDAO().execute(cred.getUsername(), cred.getPassword());
        } catch(DAOException e) {
            throw new RuntimeException(e);
        }
    }

    public Credenziali getCred() {
        return cred;
    }
}

