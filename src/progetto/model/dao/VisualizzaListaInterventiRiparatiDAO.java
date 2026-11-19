package progetto.model.dao;

import progetto.exception.DAOException;
import progetto.model.domain.Intervento;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VisualizzaListaInterventiRiparatiDAO implements GenericProcedureDAO<ArrayList<Intervento>> {
    @Override
    public ArrayList<Intervento> execute(Object... params) throws DAOException {
        //questo metodo restituisce la lista degli interventi "Riparati"
        ArrayList<Intervento> lista = new ArrayList<>();
        Intervento intervento;
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{call visualizza_lista_interventi_riparati}");
            boolean status = cs.execute();
            if(status){
                ResultSet rs = cs.getResultSet();
                while (rs.next()){
                    intervento = new Intervento(rs.getInt(1), rs.getString(2),
                            rs.getString(3),rs.getString(4),rs.getFloat(5));
                    lista.add(intervento);
                }
            }
        }catch(SQLException e){
            throw new DAOException("Errore visualizzazione lista interventi: " + e.getMessage());
        }
        return lista;
    }
}
