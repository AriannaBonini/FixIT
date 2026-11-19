package progetto.view;

import progetto.model.domain.ComponenteElettronico;
import progetto.model.domain.Intervento;

import java.util.ArrayList;
import java.util.Scanner;

public class TecnicoView {
    public static int showMenu() {
        System.out.println("*********************************");
        System.out.println("*    FixIt Menù    *");
        System.out.println("*********************************\n");
        System.out.println("*** Cosa vuoi fare? ***\n");
        System.out.println("1) Ispezionare un intervento");
        System.out.println("2) Modifica stato intervento ");
        System.out.println("3) Aggiungi componenti elettroniche ad un intervento già ispezionato");
        System.out.println("4) Esci");

        Scanner input = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.print("Seleziona un operazione: ");
            choice = input.nextInt();
            if (choice >= 1 && choice <= 5) {
                break;
            }
            System.out.println("Opzione non valida");
        }

        return choice;
    }


    public static int showSottoMenu() {
        System.out.println("Come vuoi modificare lo stato dell'intervento? ");
        System.out.println("1) In riparazione ");
        System.out.println("2) Riparato ");
        System.out.println("3) Torna al menù principale");

        Scanner input = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.print("Seleziona un operazione: ");
            choice = input.nextInt();
            if (choice >= 1 && choice <= 5) {
                break;
            }
            System.out.println("Opzione non valida");
        }

        return choice;
    }
    public static void listaVuota() {
        System.out.println("La lista è vuota");
    }

    public static int stampaListaInterventi(ArrayList<Intervento> listaInterventi, int opzione) {
        int indice = 0;
        mostraDescrizioneOpzione(opzione);

        for (Intervento intervento : listaInterventi) {
            indice++;
            System.out.printf(
                    "%d -> ID: %d, Malfunzionamento: %s, Marca Dispositivo: %s, Modello dispositivo: %s%n",
                    indice, intervento.getID(), intervento.getMalfunzionamento(), intervento.getMarcaDispositivoElettronico(), intervento.getModelloDispositivoElettronico());
        }

        Scanner input = new Scanner(System.in);
        int choice = 0;
        while (true) {
            System.out.print("Inserisci il numero dell'intervento: ");
            choice = input.nextInt();
            if (choice >= 1 && choice <= indice) {
                break;
            }
            System.out.println("numero non valido");
        }
        return --choice;
    }

    public static void mostraDescrizioneOpzione(int opzione) {
        if(opzione ==1) {
            System.out.printf("Su quale di questi interventi vuoi eseguire l'ispezione? %n");
        }else if (opzione ==2){
            System.out.printf("Su quale di questi interventi già ispezionati vuoi aggiungere altre componenti? %n");
        } else if (opzione==3) {
            System.out.printf("Quale intervento vuoi porre 'in riparazione'? %n");
        } else {
            System.out.printf("Quale intervento vuoi porre come 'riparato'? %n");
        }
    }


    public static int stampaListaComponentiElettroniche(ArrayList<ComponenteElettronico> listaComponentiElettroniche) {
        int indice = 0;

        System.out.printf("La riparazione del dispositivo di quali componenti necessita? %n");
        for (ComponenteElettronico componenteElettronico : listaComponentiElettroniche) {
            indice++;
            System.out.printf(
                    "%d -> Codice: %s, Descrizione: %s%n",
                    indice, componenteElettronico.getCodice(), componenteElettronico.getDescrizione());
        }

        Scanner input = new Scanner(System.in);
        int choice = 0;
        while (true) {
            System.out.print("Inserisci il numero del componente: ");
            choice = input.nextInt();
            if (choice >= 1 && choice <= indice) {
                break;
            }
            System.out.println("numero non valido");
        }
        return --choice;
    }


    public static int mostraOpzioniPerProcedere() {
        System.out.println("Vuoi inserire altri componenti?");
        System.out.println("1) SI");
        System.out.println("2) NO");

        Scanner input = new Scanner(System.in);
        int choice = 0;
        while (true) {
            System.out.print("Risposta: ");
            choice = input.nextInt();
            if (choice == 1 || choice == 2) {
                break;
            }
            System.out.println("Opzione non valida");
        }
        return choice;
    }

    public static void confermaModificaStatoIntervento(Boolean conferma) {
        if (Boolean.TRUE.equals(conferma)) System.out.println("\nModifca stato avvenuto con successo\n\n\n");
    }
}

