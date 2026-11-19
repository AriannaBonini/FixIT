package progetto.model.dao;

import progetto.exception.DAOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class NuovoOrdineDAO implements GenericProcedureDAO<Boolean> {
    @Override
    public Boolean execute(Object... params) throws DAOException {
        //questo metodo inserisce un nuovo ordine
        int ID = (int) params[0];
        int quantità = (int) params[1];
        int codiceComponenteElettronico = (int) params[2];
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{call crea_nuovo_ordine(?,?,?)}");
            cs.setInt(1, ID);
            cs.setInt(2, quantità);
            cs.setInt(3, codiceComponenteElettronico);
            cs.executeQuery();
        } catch (SQLException e) {
            throw new DAOException("Errore inserimento nuovo ordine: " + e.getMessage());
        }
        return true;
    }
}

