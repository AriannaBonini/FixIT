package progetto.model.domain;

public class Ordine {

    private int ID;
    private int quantità;
    private String statoOrdine;
    private String codiceComponenteElettronico;

    public Ordine(int ID, int quantità) {
        this.ID=ID;
        this.quantità=quantità;
    }

    public Ordine(int ID, int quantità, String codiceComponenteElettronico) {
        this.ID=ID;
        this.quantità=quantità;
        this.codiceComponenteElettronico=codiceComponenteElettronico;
    }

    public int getID() {return ID;}
    public int getQuantità() {return quantità;}
    public String getCodiceComponenteElettronico() {return codiceComponenteElettronico;}
}
