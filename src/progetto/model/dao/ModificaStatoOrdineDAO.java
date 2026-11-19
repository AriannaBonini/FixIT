package progetto.model.dao;

import progetto.exception.DAOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class ModificaStatoOrdineDAO implements GenericProcedureDAO<Boolean>{
    @Override
    public Boolean execute(Object... params) throws DAOException {
        int ID = (int) params[0];
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{call aggiorna_stato_ordine(?)}");
            cs.setInt(1, ID);
            cs.execute();
        }catch(SQLException e){
            throw new DAOException("Errore nella modifica dello stato dell'ordine: " + e.getMessage());
        }
        return true;
    }
}
