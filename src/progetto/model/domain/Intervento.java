package progetto.model.domain;

public class Intervento {

    private int ID;
    private String malfunzionamento;
    private float costoIntervento;
    private String statoIntervento;
    private String marcaDispositivoElettronico;
    private String modelloDispositivoElettronico;

    public Intervento(int ID, String malfunzionamento) {
        this.ID=ID;
        this.malfunzionamento=malfunzionamento;
    }

    public Intervento(int ID, String malfunzionamento, String marcaDispositivoElettronico, String modelloDispositivoElettronico) {
        this.ID=ID;
        this.malfunzionamento=malfunzionamento;
        this.marcaDispositivoElettronico=marcaDispositivoElettronico;
        this.modelloDispositivoElettronico=modelloDispositivoElettronico;
    }

    public Intervento(int ID, String malfunzionamento, String marcaDispositivoElettronico, String modelloDispositivoElettronico, float costoIntervento) {
        this.ID=ID;
        this.malfunzionamento=malfunzionamento;
        this.marcaDispositivoElettronico=marcaDispositivoElettronico;
        this.modelloDispositivoElettronico=modelloDispositivoElettronico;
        this.costoIntervento=costoIntervento;
    }


    public int getID() {return ID;}

    public void setID(int ID) {this.ID = ID;}

    public String getMalfunzionamento() {return malfunzionamento;}

    public void setMalfunzionamento(String malfunzionamento) {this.malfunzionamento = malfunzionamento;}

    public float getCostoIntervento() {return costoIntervento;}

    public void setCostoIntervento(float costoIntervento) {this.costoIntervento = costoIntervento;}

    public String getStatoIntervento() {return statoIntervento;}

    public void setStatoIntervento(String statoIntervento) {this.statoIntervento = statoIntervento;}

    public String getMarcaDispositivoElettronico() {return marcaDispositivoElettronico;}

    public void setMarcaDispositivoElettronico(String marcaDispositivoElettronico) {this.marcaDispositivoElettronico = marcaDispositivoElettronico;}

    public String getModelloDispositivoElettronico() {return modelloDispositivoElettronico;}

    public void setModelloDispositivoElettronico(String modelloDispositivoElettronico) {this.modelloDispositivoElettronico = modelloDispositivoElettronico;}
}
