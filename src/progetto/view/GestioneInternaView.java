package progetto.view;

import progetto.model.domain.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class GestioneInternaView {
    public static int showMenu() {
        System.out.println("*********************************");
        System.out.println("*    FixIt Menù    *");
        System.out.println("*********************************\n");
        System.out.println("*** Cosa vuoi fare? ***\n");
        System.out.println("1) Fare un ordine");
        System.out.println("2) Segnare un ordine come 'Ricevuto' ");
        System.out.println("3) Report della giacenza di magazzino");
        System.out.println("4) Inserisci un nuovo fornitore");
        System.out.println("5) Esci");

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

    public static Fornitore InserisciNuovoFornitore() throws IOException {
        String partitaIVA;
        String Nome;
        String Indirizzo;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        new Scanner(System.in);
        System.out.println("partita IVA: ");
        partitaIVA = reader.readLine();
        System.out.println("Nome del fornitore: ");
        Nome = reader.readLine();
        System.out.println("Indirizzo del fornitore: ");
        Indirizzo = reader.readLine();

        return new Fornitore(partitaIVA,Nome,Indirizzo);
    }

    public static Contatto InserisciContattoNuovoFornitore(String TipoContatto) throws IOException {
        String recapito;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        new Scanner(System.in);
        System.out.println("Recapito fornitore: ");
        recapito= reader.readLine();

        return new Contatto(recapito, TipoContatto);
    }


    public static int mostraListaComponenti(ArrayList<ComponenteElettronico> listaComponenti) {
        int indice = 0;
        System.out.printf("Quale componente elettronico desideri ordinare? %n");

        for (ComponenteElettronico componente : listaComponenti) {
            indice++;
            System.out.printf(
                    "%d -> Codice: %s, Descrizione: %s%n",
                    indice, componente.getCodice(), componente.getDescrizione());
        }
        return scegliOpzione(indice);
    }


    public static Ordine apriOrdineView() {
        int ID;
        int quantità;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Scanner scan = new Scanner(System.in);
        System.out.println("ID ordine: ");
        ID = scan.nextInt();
        System.out.println("Quantità ");
        quantità = scan.nextInt();

        return new Ordine(ID, quantità);
    }

    public static void confermaNuovoOrdine(Boolean conferma) {
        if (Boolean.TRUE.equals(conferma)) System.out.println("\nOrdine inserito con successo\n\n\n");
    }

    public static void listaVuota() {
        System.out.println("La lista è vuota");
    }

    public static int mostraListaOrdini(ArrayList<Ordine> listaOrdini) {
        int indice = 0;
        System.out.printf("Quale ordine desideri contrassegnare come 'ricevuto' ? %n");

        for (Ordine ordine : listaOrdini) {
            indice++;
            System.out.printf(
                    "%d -> ID %d, Quantità: %d, Codice Componente: %s%n",
                    indice, ordine.getID(), ordine.getQuantità(), ordine.getCodiceComponenteElettronico());
        }
        return scegliOpzione(indice);
    }

    public static int scegliOpzione(int indice) {
        Scanner input = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.print("Inserisci il numero: ");
            choice = input.nextInt();
            if (choice >= 1 && choice <= indice) {
                break;
            }
            System.out.println("numero non valido");
        }
        return --choice;
    }

    public static void confermaModificaStato(Boolean conferma) {
        if (Boolean.TRUE.equals(conferma)) System.out.println("\nStato ordine modificato con successo\n\n\n");
    }

    public static int showSottoMenu() {
        System.out.println("1) Vuoi fare un nuovo ordine? ");
        System.out.println("2) Torna al menù");

        Scanner input = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.print("Seleziona un operazione: ");
            choice = input.nextInt();
            if (choice >= 1 && choice <= 4) {
                break;
            }
            System.out.println("Opzione non valida");
        }

        return choice;
    }


    public static void mostraListaComponentiConGiacenza(ArrayList<ComponenteElettronico> listaComponenti) {
        int indice = 0;
        for (ComponenteElettronico componente : listaComponenti) {
            indice++;
            System.out.printf(
                    "%d -> Codice: %s, Descrizione: %s, Giacenza: %d%n",
                    indice, componente.getCodice(), componente.getDescrizione(), componente.getGiacenza());
        }
    }

    public static void confermaFornitoreAggiunto(Boolean conferma) {
        if (Boolean.TRUE.equals(conferma)) System.out.println("\nFornitore inserito con successo\n\n\n");
    }

    public static int OpzioniInserisciContatto() {
        System.out.println("Quale recapito vuoi inserire?");
        System.out.println("1) Numero di telefono");
        System.out.println("2) E-mail");
        System.out.println("2) Altro");

        Scanner input = new Scanner(System.in);
        int choice ;
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
}
