package progetto.view;

import progetto.model.domain.ComponenteElettronico;
import progetto.model.domain.DispositivoElettronico;
import progetto.model.domain.Intervento;
import progetto.model.domain.Necessita;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class SegreteriaView {

    public static int showMenu() {
        System.out.println("*********************************");
        System.out.println("*    FixIt Menù    *");
        System.out.println("*********************************\n");
        System.out.println("*** Cosa vuoi fare? ***\n");
        System.out.println("1) Aprire un nuovo intervento");
        System.out.println("2) Calcolare il costo di un intervento");
        System.out.println("3) Concludere un intervento");
        System.out.println("4) Eliminare un intervento");
        System.out.println("5) Aggiungi un nuovo dispositivo elettronico");
        System.out.println("6) Aggiungi componenti elettronici ad un dispositivo");
        System.out.println("7) Esci");

        Scanner input = new Scanner(System.in);
        int choice = 0;
        while (true) {
            System.out.print("Seleziona un operazione: ");
            choice = input.nextInt();
            if (choice >= 1 && choice <= 6) {
                break;
            }
            System.out.println("Opzione non valida");
        }

        return choice;
    }


    public static Necessita inserisciComponentePerUnDispositivoView() throws IOException {
        String modello;
        String marca;
        String codiceComponente;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Scanner scan = new Scanner(System.in);
        System.out.println("Marca: ");
        marca= reader.readLine();
        System.out.println("Modello: ");
        modello= reader.readLine();
        System.out.println("Codice componente elettronico: ");
        codiceComponente=reader.readLine();

        return new Necessita(marca, modello, codiceComponente);
    }

    public static Intervento apriInterventoView() throws IOException {
        int ID;
        String malfunzionamento;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Scanner scan = new Scanner(System.in);
        System.out.println("ID intervento: ");
        ID = scan.nextInt();
        System.out.println("malfunzionamento: ");
        malfunzionamento = reader.readLine();

        return new Intervento(ID, malfunzionamento);
    }

    public static void confermaAperturaIntervento(Boolean conferma) {
        if (Boolean.TRUE.equals(conferma)) System.out.println("\nIntervento inserito con successo\n\n\n");
    }

    public static void confermaCalcoloCosto(Boolean conferma) {
        if (Boolean.TRUE.equals(conferma)) System.out.println("\nCosto intervento calcolato con successo\n\n\n");
    }

    public static void confermaInterventoConcluso(Boolean conferma) {
        if (Boolean.TRUE.equals(conferma)) System.out.println("\nL'intervento è stato concluso con successo\n\n\n");
    }

    public static void confermaInterventoEliminato(Boolean conferma) {
        if (Boolean.TRUE.equals(conferma)) System.out.println("\nIntervento eliminato con successo\n\n\n");
    }

    public static void listaInterventiVuota() {
        System.out.println("La lista degli interventi è vuota.\n");
    }

    public static int stampaListaInterventiPerCalcoloCosto(ArrayList<Intervento> listaInterventi) {
        int indice = 0;

        System.out.printf("Su quale di questi interventi vuoi calcolare il costo? %n");
        for (Intervento intervento : listaInterventi) {
            indice++;
            System.out.printf(
                    "%d -> ID: %d, Malfunzionamento: %s, Marca Dispositivo: %s, Modello dispositivo: %s%n",
                    indice, intervento.getID(), intervento.getMalfunzionamento(), intervento.getMarcaDispositivoElettronico(), intervento.getModelloDispositivoElettronico());
        }
        return selezionaIntervento(indice);
    }

    public static int mostraListaDispositivi(ArrayList<DispositivoElettronico> listaDispositivi) {
        int indice = 0;
        System.out.printf("Quale marca e modello di dispositivo deve essere riparato? %n");

        for (DispositivoElettronico dispositivo : listaDispositivi) {
            indice++;
            System.out.printf(
                    "%d -> Marca Dispositivo: %s, Modello dispositivo: %s%n",
                    indice, dispositivo.getMarca(), dispositivo.getModello());
        }

        Scanner input = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.print("Inserisci il numero del dispositivo: ");
            choice = input.nextInt();
            if (choice >= 1 && choice <= indice) {
                break;
            }
            System.out.println("numero non valido");
        }
        return --choice;
    }

    public static int stampaListaInterventiRiparati(ArrayList<Intervento> listaInterventi) {
        int indice = 0;
        System.out.printf("Quale intervento vuoi concludere? %n");

        for (Intervento intervento : listaInterventi) {
            indice++;
            if (intervento.getCostoIntervento() == 0.0f) {
                System.out.printf(
                        "%d -> ID: %d, Malfunzionamento: %s, Marca Dispositivo: %s, Modello dispositivo: %s, CostoIntervento: DA CALCOLARE %n",
                        indice, intervento.getID(), intervento.getMalfunzionamento(), intervento.getMarcaDispositivoElettronico(), intervento.getModelloDispositivoElettronico());
            } else {
                System.out.printf(
                        "%d -> ID: %d, Malfunzionamento: %s, Marca Dispositivo: %s, Modello dispositivo: %s, CostoIntervento: %f%n",
                        indice, intervento.getID(), intervento.getMalfunzionamento(), intervento.getMarcaDispositivoElettronico(), intervento.getModelloDispositivoElettronico(), intervento.getCostoIntervento());
            }
        }
        return selezionaIntervento(indice);
    }

    public static int stampaListaInterventi(ArrayList<Intervento> listaInterventi) {
        int indice = 0;
        System.out.printf("Quale intervento vuoi eliminare %n");

        for (Intervento intervento : listaInterventi) {
            indice++;
            if (intervento.getCostoIntervento() == 0.0f) {
                System.out.printf(
                        "%d -> ID: %d, Malfunzionamento: %s%n",
                        indice, intervento.getID(), intervento.getMalfunzionamento());
            }
        }
        return selezionaIntervento(indice);
    }

    public static int selezionaIntervento(int indice) {
        Scanner input = new Scanner(System.in);
        int choice;
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

    public static int mostraOpzioniPerGestioneEccezioneInterventoConcluso() {
        System.out.println("Il costo dell'intervento non è stato calcolato. Vuoi calcolarlo ?");
        System.out.println("1) Si");
        System.out.println("2) Torna alla lista degli interventi");

        Scanner input = new Scanner(System.in);
        int choice = 0;
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

    public static int mostraOpzioniPerGestioneEccezioneInterventoEliminato() {
        System.out.println("L'intervento non può essere eliminato, come vuoi procedere ?");
        System.out.println("1) Torna alla lista degli interventi");
        System.out.println("2) Torna al menù principale");

        Scanner input = new Scanner(System.in);
        int choice = 0;
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

    public static DispositivoElettronico nuovoDispositivoElettronico() throws IOException {
        String marca;
        String modello;
        String tipo;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        new Scanner(System.in);
        System.out.println("Marca: ");
        marca= reader.readLine();
        System.out.println("modello: ");
        modello = reader.readLine();

        while (true) {
            System.out.println("Digita 1 se è un cellulare, 2 se è un computer portatile ");
            tipo= reader.readLine() ;
            if (tipo.equals("1") || tipo.equals("2")) {
                break;
            }
            System.out.println("Opzione non valida");
        }
        return new DispositivoElettronico(marca, modello, tipo);
    }

    public static ArrayList<Necessita> selezionaComponentiPerNuovoDispositivo(ArrayList<ComponenteElettronico> componentiElettroniche) {
        int indice = 0;
        ArrayList<Necessita> necessita= new ArrayList<>();
        ArrayList<Integer> componentiScelti=new ArrayList<>();
        System.out.printf("Quale componente elettronica vuoi associare al nuovo dispositivo? %n");

        for (ComponenteElettronico componenteElettronico : componentiElettroniche) {
            indice++;
            System.out.printf(
                    "%d -> Codice: %s, Descrizione: %s%n",
                    indice, componenteElettronico.getCodice(), componenteElettronico.getDescrizione());
        }
        Scanner input = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.print("Inserisci il numero del componente presente nella lista: ");
            choice = input.nextInt();

            if (choice >= 1 && choice <= indice && !componentiScelti.contains(choice)  ) {
                componentiScelti.add(choice);
                necessita.add(new Necessita(componentiElettroniche.get(--choice).getCodice()));
                System.out.println("Vuoi inserire un altro componente? ");
                System.out.println("1) SI");
                System.out.println("2) NO");
                choice = input.nextInt();
                if (choice == 2) {
                    break;
                }
            } else {
                System.out.println("numero non valido");
            }
        }
        return necessita;
    }

    public static void confermaAggiuntaNuovoDispositivo(Boolean conferma) {
        if (Boolean.TRUE.equals(conferma)) System.out.println("\nIl dispositivo è stato aggiunto con successo\n\n\n");
    }

    public static void confermaAggiuntaNuoveComponentiAlDispositivo(Boolean conferma) {
        if (Boolean.TRUE.equals(conferma)) System.out.println("\nLe componente sono state aggiunte con successo\n\n\n");
    }

    public static void confermaAggiuntaNuovaComponenteAlDispositivo(Boolean conferma) {
        if (Boolean.TRUE.equals(conferma)) System.out.println("\nLa componente è stata aggiunta con successo\n\n\n");
    }
}
