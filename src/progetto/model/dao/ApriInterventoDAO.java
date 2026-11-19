package progetto.model.dao;

import progetto.exception.DAOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class ApriInterventoDAO implements GenericProcedureDAO<Boolean>{
    @Override
    public Boolean execute(Object ... params) throws DAOException {
        int ID = (int) params[0];
        String malfunzionamento= (String) params[1];
        String marca = (String) params[2];
        String modello= (String) params[3];
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{call apri_Intervento(?,?,?,?)}");
            cs.setInt(1, ID);
            cs.setString(2, malfunzionamento);
            cs.setString(3, marca);
            cs.setString(4, modello);
            cs.executeQuery();

        } catch(SQLException e) {
            throw new DAOException("Errore apertura intervento: " + e.getMessage());
        }
        return true;

    }

}
