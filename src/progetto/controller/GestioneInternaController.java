package progetto.controller;

import progetto.exception.DAOException;
import progetto.model.dao.*;
import progetto.model.domain.*;
import progetto.view.GestioneInternaView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class GestioneInternaController implements Controller {
    @Override
    public void start() {
        try {
            ConnectionFactory.changeRole(Ruolo.GESTIONE_INTERNA);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        boolean exitNow = false;
        while (!exitNow) {
            try {
                int choice;
                choice = GestioneInternaView.showMenu();
                switch (choice) {
                    case 1 -> NuovoOrdine();
                    case 2 -> OrdineRicevuto();
                    case 3 -> ReportGiacenzaMagazzino();
                    case 4 -> InserisciNuovoFornitore();
                    case 5 -> exitNow = true;
                    default -> throw new RuntimeException("Scelta non valida");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void InserisciNuovoFornitore() throws IOException {
        Fornitore fornitore;
        Contatto contatto;
        try {
            fornitore = GestioneInternaView.InserisciNuovoFornitore();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        contatto = OpzioniNuovoContatto();
        InserisciFornitore(fornitore, contatto);
    }

    public Contatto OpzioniNuovoContatto() throws IOException {
        Contatto contatto;
        int choice;
        choice = GestioneInternaView.OpzioniInserisciContatto();
        switch (choice) {
            case 1 -> contatto = GestioneInternaView.InserisciContattoNuovoFornitore("Telefono");
            case 2 -> contatto = GestioneInternaView.InserisciContattoNuovoFornitore("E-mail");
            case 3 -> contatto = GestioneInternaView.InserisciContattoNuovoFornitore("Altro");
            default -> throw new RuntimeException("Scelta non valida");
        }
        return contatto;
    }

    public void InserisciFornitore(Fornitore fornitore, Contatto contatto) {
        Boolean conferma;
        try {
            conferma = new inserisciFornitoreDAO().execute(fornitore.getPartitaIVA(), fornitore.getNome(), fornitore.getIndirizzo(), contatto.getRecapito(), contatto.getTipoContatto());
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        GestioneInternaView.confermaFornitoreAggiunto(conferma);
    }


    public void NuovoOrdine() {
        ArrayList<ComponenteElettronico> listaComponentiElettroniche;
        int indiceComponente;
        try {
            listaComponentiElettroniche = new ListaComponentiElettronichePerNuovoOrdineDAO().execute();

        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        if (listaComponentiElettroniche.isEmpty()) {
            GestioneInternaView.listaVuota();
        } else {
            indiceComponente = GestioneInternaView.mostraListaComponenti(listaComponentiElettroniche);
            CreaOrdine(listaComponentiElettroniche.get(indiceComponente));
        }
    }

    public void CreaOrdine(ComponenteElettronico componenteElettronico) {
        Ordine ordine;
        ordine = GestioneInternaView.apriOrdineView();
        Boolean conferma;
        try {
            conferma = new NuovoOrdineDAO().execute(ordine.getID(), ordine.getQuantità(), componenteElettronico.getCodice());
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        GestioneInternaView.confermaNuovoOrdine(conferma);
    }

    public void OrdineRicevuto() {
        ArrayList<Ordine> listaOrdini;
        int indice;
        try {
            listaOrdini = new ListaOrdiniDAO().execute();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        if (listaOrdini.isEmpty()) {
            GestioneInternaView.listaVuota();
        } else {
            indice = GestioneInternaView.mostraListaOrdini(listaOrdini);
            ModificaStatoOrdineComeRicevuto(listaOrdini.get(indice));
        }
    }

    public void ModificaStatoOrdineComeRicevuto(Ordine ordine) {
        Boolean conferma;
        try {
            conferma=new ModificaStatoOrdineDAO().execute(ordine.getID());
        }catch (DAOException e) {
            throw new RuntimeException(e);
        }
        GestioneInternaView.confermaModificaStato(conferma);
    }

    public void Opzioni() {
        boolean exitNow = false;
        while (!exitNow) {
            try {
                int choice;
                choice = GestioneInternaView.showSottoMenu();
                switch (choice) {
                    case 1 -> NuovoOrdine();
                    case 2 -> exitNow = true;
                    default -> throw new RuntimeException("Scelta non valida");
                }
            }       catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private void ReportGiacenzaMagazzino() {
        ArrayList<ComponenteElettronico> listaComponentiElettronici;
        try {
            listaComponentiElettronici= new ListaComponentiElettronicheConGiacenzaDAO().execute();
        }catch (DAOException e) {
            throw new RuntimeException(e);
        }
        GestioneInternaView.mostraListaComponentiConGiacenza(listaComponentiElettronici);
        Opzioni();
    }

}
