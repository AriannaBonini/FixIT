package progetto.model.domain;

public class Fornitore {
    private String partitaIVA;
    private String nome;
    private String indirizzo;

    public Fornitore(String partitaIVA, String nome, String indirizzo) {
        this.partitaIVA=partitaIVA;
        this.nome=nome;
        this.indirizzo=indirizzo;
    }
    public String getPartitaIVA() {return partitaIVA;}
    public void setPartitaIVA(String partitaIVA) {this.partitaIVA = partitaIVA;}
    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}
    public String getIndirizzo() {return indirizzo;}
    public void setIndirizzo(String indirizzo) {this.indirizzo = indirizzo;}
}
