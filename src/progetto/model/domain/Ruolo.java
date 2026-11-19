package progetto.model.domain;

public enum Ruolo {
    SEGRETERIA(1),
    GESTIONE_INTERNA(2),
    TECNICO(3);

    private final int id;

    Ruolo(int id) {
        this.id = id;
    }

    public static Ruolo fromInt(int id) {
        for (Ruolo type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }
}
