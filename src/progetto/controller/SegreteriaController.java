package progetto.controller;

import progetto.Utilities;
import progetto.exception.DAOException;
import progetto.model.dao.*;
import progetto.model.domain.*;
import progetto.view.SegreteriaView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class SegreteriaController implements Controller {
    @Override
    public void start() {
        try {
            ConnectionFactory.changeRole(Ruolo.SEGRETERIA);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        boolean exitNow = false;
        while (!exitNow) {
            try {
                int choice;
                choice = SegreteriaView.showMenu();
                switch (choice) {
                    case 1 -> ApriNuovoIntervento();
                    case 2 -> VisualizzaInterventiPerCalcoloCosto();
                    case 3 -> ConcludiIntervento();
                    case 4 -> EliminaIntervento();
                    case 5 -> AggiungiNuovoDispositivoElettronico();
                    case 6 -> AggiungiComponenteElettronicoAdUnDispositivo();
                    case 7 -> exitNow = true;
                    default -> throw new RuntimeException("Scelta non valida");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void AggiungiComponenteElettronicoAdUnDispositivo() {
        Necessita necessita;
        Boolean conferma;
        try {
            necessita= SegreteriaView.inserisciComponentePerUnDispositivoView();
            conferma= new aggiungiComponentiPerDispositivoDAO().execute(necessita.getMarca(), necessita.getModello(), necessita.getCodiceComponenteElettronico());
        }catch (IOException e) {
            throw new RuntimeException(e);
        }catch (DAOException e) {
            throw new RuntimeException(e);
        }
        SegreteriaView.confermaAggiuntaNuovaComponenteAlDispositivo(conferma);

    }


    public void AggiungiNuovoDispositivoElettronico() {
        DispositivoElettronico dispositivoElettronico;
        ArrayList<Necessita> necessita;
        try {
            dispositivoElettronico=SegreteriaView.nuovoDispositivoElettronico();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(dispositivoElettronico.getTipo().equals("1")) {
            dispositivoElettronico.setTipo("Cellulare");
        }else{
            dispositivoElettronico.setTipo("Computer Portatile");
        }
        necessita=AggiungiComponentiElettronichePerIlNuovoDispositivo(dispositivoElettronico.getTipo());
        AggiungiDispositivo(dispositivoElettronico, necessita);
    }

    public ArrayList<Necessita> AggiungiComponentiElettronichePerIlNuovoDispositivo(String TipoDispositivo) {
        ArrayList<Necessita> necessita;
        ArrayList<ComponenteElettronico> componentiElettroniche;
        try {
            componentiElettroniche = new listaComponentiElettronicheDAO().execute(TipoDispositivo);
        }catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return SegreteriaView.selezionaComponentiPerNuovoDispositivo(componentiElettroniche);
    }

    public void AggiungiDispositivo(DispositivoElettronico dispositivoElettronico, ArrayList<Necessita> necessita) {
        Boolean conferma;
        try {
            conferma=new aggiungiDispositivoDAO().execute(dispositivoElettronico.getMarca(), dispositivoElettronico.getModello(), dispositivoElettronico.getTipo());
        }catch (DAOException e) {
            throw new RuntimeException(e);
        }
        SegreteriaView.confermaAggiuntaNuovoDispositivo(conferma);
        try {
            for (int i = 0; i < necessita.size(); i++) {
                System.out.println(necessita.get(i).getCodiceComponenteElettronico());
                conferma = new aggiungiComponentiPerNuovoDispositivoDAO().execute(dispositivoElettronico.getMarca(),dispositivoElettronico.getModello(),necessita.get(i).getCodiceComponenteElettronico());
            }
        }catch (DAOException e) {
            throw new RuntimeException(e);
        }
        SegreteriaView.confermaAggiuntaNuoveComponentiAlDispositivo(conferma);
    }

    public void ApriNuovoIntervento() {
        Intervento intervento;
        ArrayList<DispositivoElettronico> listaDispositiviElettronici = RichiediListaDispositivi();
        Boolean conferma;
        int indiceDispositivo;
        try {
            indiceDispositivo = SegreteriaView.mostraListaDispositivi(listaDispositiviElettronici);
            intervento = SegreteriaView.apriInterventoView();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            conferma = new ApriInterventoDAO().execute(intervento.getID(), intervento.getMalfunzionamento(), listaDispositiviElettronici.get(indiceDispositivo).getMarca(), listaDispositiviElettronici.get(indiceDispositivo).getModello());
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        SegreteriaView.confermaAperturaIntervento(conferma);
    }

    public ArrayList<DispositivoElettronico> RichiediListaDispositivi() {
        ArrayList<DispositivoElettronico> listaDispositiviElettronici;
        try {
            listaDispositiviElettronici = new visualizzaListaDispositiviElettroniciDAO().execute();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return listaDispositiviElettronici;
    }


    public void VisualizzaInterventiPerCalcoloCosto() {
        ArrayList<Intervento> listaInterventi;
        int indiceLista;
        try {
            listaInterventi = new VisualizzaListaInterventiPerCalcoloCostoDAO().execute();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        if (listaInterventi.isEmpty()) {
            SegreteriaView.listaInterventiVuota();
        } else {
            indiceLista = SegreteriaView.stampaListaInterventiPerCalcoloCosto(listaInterventi);
            CalcoloCostoIntervento(listaInterventi.get(indiceLista));
        }
    }

    public void CalcoloCostoIntervento(Intervento intervento) {
        Boolean conferma;
        try {
            conferma = new calcoloCostoInterventoDAO().execute(intervento.getID());
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        SegreteriaView.confermaCalcoloCosto(conferma);
    }

    public void ConcludiIntervento() {
        ArrayList<Intervento> listaInterventi;
        int indiceLista;
        try {
            listaInterventi = new VisualizzaListaInterventiRiparatiDAO().execute();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        if (listaInterventi.isEmpty()) {
            SegreteriaView.listaInterventiVuota();
        } else {
            indiceLista = SegreteriaView.stampaListaInterventiRiparati(listaInterventi);
            SceltoInterventoDaConcludere(listaInterventi.get(indiceLista));
        }
    }

    public void SceltoInterventoDaConcludere(Intervento intervento) {
        Boolean conferma = null;
        try {
            conferma = new ConcludiInterventoDAO().execute(intervento.getID());
        } catch (DAOException e) {
            if (Objects.equals(e.getMessage(), Utilities.eccezioneConcludiIntervento)) {
                GestioneEccezioneConcludiIntervento(intervento);
            } else {
                throw new RuntimeException(e);
            }
        }
        SegreteriaView.confermaInterventoConcluso(conferma);
    }

    public void GestioneEccezioneConcludiIntervento(Intervento intervento) {
        int choice = SegreteriaView.mostraOpzioniPerGestioneEccezioneInterventoConcluso();
        switch (choice) {
            case 1 -> {
                CalcoloCostoIntervento(intervento);
                SceltoInterventoDaConcludere(intervento);
            }
            case 2 -> ConcludiIntervento();
            default -> throw new RuntimeException("Scelta non valida");
        }

    }

    public void EliminaIntervento() {
        ArrayList<Intervento> listaInterventi;
        int indice;
        try {
            listaInterventi = new listaInterventiDAO().execute();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        if (listaInterventi.isEmpty()) {
            SegreteriaView.listaInterventiVuota();
        } else {
            indice = SegreteriaView.stampaListaInterventi(listaInterventi);
            SceltoInterventoDaEliminare(listaInterventi, indice);
        }
    }

    public void SceltoInterventoDaEliminare(ArrayList<Intervento> listaInterventi, int indice) {
        Boolean conferma = null;
        try {
            conferma = new eliminaInterventoDAO().execute(listaInterventi.get(indice).getID());
        } catch (DAOException e) {
            if (Objects.equals(e.getMessage(), Utilities.eccezioneEliminaIntervento)) {
                GestioneEccezioneEliminaIntervento(listaInterventi);
            } else {
                throw new RuntimeException(e);
            }
        }
            SegreteriaView.confermaInterventoEliminato(conferma);
        }
    public void GestioneEccezioneEliminaIntervento(ArrayList<Intervento> listaInterventi) {
        int choice = SegreteriaView.mostraOpzioniPerGestioneEccezioneInterventoEliminato();
        switch (choice) {
            case 1 -> SceltoInterventoDaEliminare(listaInterventi, SegreteriaView.stampaListaInterventi(listaInterventi));
            case 2 -> System.out.println("Reindirizzamento al menù in corso");
            default -> throw new RuntimeException("Scelta non valida");
        }

    }


}