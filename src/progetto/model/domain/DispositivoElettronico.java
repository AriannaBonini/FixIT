package progetto.model.domain;

public class DispositivoElettronico {
    private String marca;
    private String modello;
    private String tipo;

    public DispositivoElettronico(String marca, String modello) {
        this.marca=marca;
        this.modello=modello;
    }

    public DispositivoElettronico(String marca, String modello, String tipo) {
        this.marca=marca;
        this.modello=modello;
        this.tipo=tipo;
    }

    public String getMarca() {return marca;}
    public void setMarca(String marca) {this.marca = marca;}
    public String getModello() {return modello;}
    public void setModello(String modello) {this.modello = modello;}
    public String getTipo() {return tipo;}
    public void setTipo(String tipo) {this.tipo = tipo;}
}
