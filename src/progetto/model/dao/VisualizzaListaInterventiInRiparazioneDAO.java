package progetto.model.dao;

import progetto.exception.DAOException;
import progetto.model.domain.Intervento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VisualizzaListaInterventiInRiparazioneDAO implements GenericProcedureDAO<ArrayList<Intervento>>{
    @Override
    public ArrayList<Intervento> execute(Object... params) throws DAOException {
        //questo metodo restituisce la lista degli interventi in riparazione.
        // E' utilizzato dall'utente Tecnico per aggiornare lo statoIntervento da "In riparazione" a "Riparato"
        ArrayList<Intervento> lista = new ArrayList<>();
        Intervento intervento;
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{call visualizza_lista_interventi_in_riparazione()}");
            boolean status = cs.execute();
            if(status){
                ResultSet rs = cs.getResultSet();
                while (rs.next()){
                    intervento = new Intervento(rs.getInt(1), rs.getString(2),
                            rs.getString(3), rs.getString(4));
                    lista.add(intervento);
                }
            }
        }catch(SQLException e){
            throw new DAOException("Errore visualizzazione lista interventi: " + e.getMessage());
        }
        return lista;
    }

}
