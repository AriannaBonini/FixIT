package progetto.controller;

import progetto.exception.DAOException;
import progetto.model.dao.*;
import progetto.model.domain.ComponenteElettronico;
import progetto.model.domain.Intervento;
import progetto.model.domain.Ruolo;
import progetto.model.domain.Sostituisce;
import progetto.view.TecnicoView;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class TecnicoController implements Controller {
    @Override
    public void start() {
        try {
            ConnectionFactory.changeRole(Ruolo.TECNICO);
            System.out.println("Sono un tecnico");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        boolean exitNow = false;
        while (!exitNow) {
            try {
                int choice;
                choice = TecnicoView.showMenu();
                switch (choice) {
                    case 1 -> IspezionaIntervento();
                    case 2 -> SceltaNuovoStatoIntervento();
                    case 3 -> ListaInterventiIspezionati();
                    case 4 -> exitNow = true;
                    default -> throw new RuntimeException("Scelta non valida");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }


    public void SceltaNuovoStatoIntervento() {
        boolean exitNow = false;
        while (!exitNow) {
            try {
                int choice;
                choice = TecnicoView.showSottoMenu();
                switch (choice) {
                    case 1 -> StatoInterventoInRiparazione();
                    case 2 -> StatoInterventoRiparato();
                    case 3 -> exitNow = true;
                    default -> throw new RuntimeException("Scelta non valida");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void StatoInterventoRiparato() {
        ArrayList<Intervento> listaInterventi;
        int indiceIntervento;
        try {
            listaInterventi= new VisualizzaListaInterventiInRiparazioneDAO().execute();
        }catch (DAOException e) {
            throw new RuntimeException(e);
        }
        if (listaInterventi.isEmpty()) {
            TecnicoView.listaVuota();
        } else {
            indiceIntervento=TecnicoView.stampaListaInterventi(listaInterventi,4);
            ModificaStatoInterventoComeRiparato(listaInterventi.get(indiceIntervento));

        }
    }

    public void ModificaStatoInterventoComeRiparato(Intervento intervento) {
        Boolean conferma;
        try {
            conferma=new ModifcaInterventoComeRiparatoDAO().execute(intervento.getID());
        }catch (DAOException e) {
            throw new RuntimeException(e);
        }
        TecnicoView.confermaModificaStatoIntervento(conferma);
    }

    public void IspezionaIntervento() {
        ArrayList<Intervento> listaInterventi;
        int indiceLista;
        try {
            listaInterventi = new VisualizzaListaInterventiDaIspezionareDAO().execute();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        if (listaInterventi.isEmpty()) {
            TecnicoView.listaVuota();
        } else {
            indiceLista = TecnicoView.stampaListaInterventi(listaInterventi,1);
            OttieniComponentiElettroniche(listaInterventi.get(indiceLista));
        }
    }

    public void OttieniComponentiElettroniche(Intervento intervento) {
        ArrayList<ComponenteElettronico> listaComponentiElettroniche;
        try {
            listaComponentiElettroniche = new ComponentiElettronichePerInterventoDAO().execute(intervento.getMarcaDispositivoElettronico(), intervento.getModelloDispositivoElettronico());
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        if (listaComponentiElettroniche.isEmpty()) {
            TecnicoView.listaVuota();
        } else {
            sceltaComponente(intervento, listaComponentiElettroniche);
        }
    }

    public void sceltaComponente(Intervento intervento, ArrayList<ComponenteElettronico> listaComponentiElettroniche) {
        int indiceComponente;
        Sostituisce sostituisce=new Sostituisce();

        indiceComponente = TecnicoView.stampaListaComponentiElettroniche(listaComponentiElettroniche);
        sostituisce.setIdIntervento(intervento.getID());
        sostituisce.setCodiceComponenteElettronico(listaComponentiElettroniche.get(indiceComponente).getCodice());

        InserimentoComponenteElettronico(sostituisce);

        int choice;
        choice = TecnicoView.mostraOpzioniPerProcedere();
        switch (choice) {
            case 1 -> listaComponentiPerInterventiIspezionati(intervento);
        }

    }

    public void InserimentoComponenteElettronico(Sostituisce sostituisce) {
        try {
            new inserisciCoppiaInterventoComponenteElettronicoDAO().execute(sostituisce.getIdIntervento(), sostituisce.getCodiceComponenteElettronico());
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }


    public void StatoInterventoInRiparazione() {
        ArrayList<Intervento> listaInterventi;
        int indiceLista;
        try {
            listaInterventi = new VisualizzaListaInterventiIspezionatiDAO().execute();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        if (listaInterventi.isEmpty()) {
            TecnicoView.listaVuota();
        } else {
            indiceLista = TecnicoView.stampaListaInterventi(listaInterventi,3);
            ModificaStatoInterventoComeInRiparazione(listaInterventi.get(indiceLista));
        }

    }

    public void ModificaStatoInterventoComeInRiparazione(Intervento intervento) {
        Boolean conferma;
        try {
            conferma=new ModificaInterventoComeInRiparazioneDAO().execute(intervento.getID());
        }catch (DAOException e) {
            throw new RuntimeException(e);
        }
        TecnicoView.confermaModificaStatoIntervento(conferma);

    }

    public void ListaInterventiIspezionati() {
        ArrayList<Intervento> listaInterventi;
        int indiceLista;
        try {
            listaInterventi = new VisualizzaListaInterventiIspezionatiDAO().execute();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        if (listaInterventi.isEmpty()) {
            TecnicoView.listaVuota();
        } else {
            indiceLista = TecnicoView.stampaListaInterventi(listaInterventi,2);
            listaComponentiPerInterventiIspezionati(listaInterventi.get(indiceLista));
        }
    }

    public void listaComponentiPerInterventiIspezionati(Intervento intervento) {
        ArrayList<ComponenteElettronico> listaComponentiElettroniche;
        try {
            listaComponentiElettroniche = new ComponentiElettronichePerInterventoIspezionatoDAO().execute(intervento.getMarcaDispositivoElettronico(), intervento.getModelloDispositivoElettronico(), intervento.getID());
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        if (listaComponentiElettroniche.isEmpty()) {
            TecnicoView.listaVuota();
        } else {
            sceltaComponente(intervento, listaComponentiElettroniche);
        }
    }

}