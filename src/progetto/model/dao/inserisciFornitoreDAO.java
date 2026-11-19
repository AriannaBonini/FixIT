package progetto.model.dao;

import progetto.exception.DAOException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class inserisciFornitoreDAO  implements GenericProcedureDAO<Boolean>{
    @Override
    public Boolean execute(Object ... params) throws DAOException {
        String  partitaIva = (String) params[0];
        String nome= (String) params[1];
        String indirizzo = (String) params[2];
        String recapito= (String) params[3];
        String tipoContatto=(String) params[4];
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{call inserisci_fornitore(?,?,?,?,?)}");
            cs.setString(1, partitaIva);
            cs.setString(2, nome);
            cs.setString(3, indirizzo);
            cs.setString(4, recapito);
            cs.setString(5,tipoContatto);
            cs.executeQuery();

        } catch(SQLException e) {
            throw new DAOException("Errore inserimento nuovo fornitore: " + e.getMessage());
        }
        return true;
    }
}
