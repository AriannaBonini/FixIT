package progetto.model.domain;

public class Necessita {

    private String marca;
    private String modello;
    private String codiceComponenteElettronico;
    public Necessita(String codiceComponenteElettronico) {
        this.codiceComponenteElettronico=codiceComponenteElettronico;
    }

    public Necessita(String marca, String modello, String codiceComponenteElettronico) {
        this.marca=marca;
        this.modello=modello;
        this.codiceComponenteElettronico=codiceComponenteElettronico;
    }
    public String getCodiceComponenteElettronico() {return codiceComponenteElettronico;}
    public String getMarca() {return marca;}
    public void setMarca(String marca) {this.marca = marca;}
    public String getModello() {return modello;}
    public void setModello(String modello) {this.modello = modello;}
}
