package progetto.model.dao;

import progetto.exception.DAOException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class aggiungiDispositivoDAO implements GenericProcedureDAO<Boolean>{
    @Override
    public Boolean execute(Object ... params) throws DAOException {
        String marca= (String) params[0];
        String modello = (String) params[1];
        String tipo= (String) params[2];
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{call aggiungi_dispositivo_elettronico(?,?,?)}");
            cs.setString(1, marca);
            cs.setString(2, modello);
            cs.setString(3, tipo);
            cs.executeQuery();

        } catch(SQLException e) {
            throw new DAOException("Errore inserimento nuovo dispositivo elettronico: " + e.getMessage());
        }
        return true;
    }
}
