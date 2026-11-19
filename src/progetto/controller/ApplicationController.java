package progetto.controller;

import progetto.model.domain.Credenziali;

public class ApplicationController implements Controller {

    Credenziali cred;

    @Override
    public void start() {
        LoginController loginController = new LoginController();
        loginController.start();
        cred = loginController.getCred();

        if(cred.getRuolo() == null) {
            throw new RuntimeException("Credenziali non valide");
        }

        switch(cred.getRuolo()) {
            case SEGRETERIA -> new SegreteriaController().start();
            case GESTIONE_INTERNA -> new GestioneInternaController().start();
            case TECNICO -> new TecnicoController().start();
            default -> throw new RuntimeException("Credenziali non valide");
        }
    }

}
