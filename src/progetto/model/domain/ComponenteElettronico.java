package progetto.model.domain;

public class ComponenteElettronico {
    private String codice;
    private String descrizione;
    private float prezzo;
    private int giacenza;
    private String partitaIVA_fornitore;
    private String categoria;

    public ComponenteElettronico(String codice, String descrizione) {
        this.codice=codice;
        this.descrizione=descrizione;
    }

    public ComponenteElettronico(String codice, String descrizione, int giacenza) {
        this.codice=codice;
        this.descrizione=descrizione;
        this.giacenza=giacenza;
    }

    public String getCodice() {return codice;}
    public String getDescrizione() {return descrizione;}
    public int getGiacenza() {return giacenza;}

}
