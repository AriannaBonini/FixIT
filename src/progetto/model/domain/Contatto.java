package progetto.model.domain;

public class Contatto {
    private String recapito;
    private String tipoContatto;

    public Contatto(String recapito, String tipoContatto) {
        this.recapito=recapito;
        this.tipoContatto=tipoContatto;
    }

    public String getRecapito() {return recapito;}
    public void setRecapito(String recapito) {this.recapito = recapito;}
    public String getTipoContatto() {return tipoContatto;}
    public void setTipoContatto(String tipoContatto) {this.tipoContatto = tipoContatto;}
}
