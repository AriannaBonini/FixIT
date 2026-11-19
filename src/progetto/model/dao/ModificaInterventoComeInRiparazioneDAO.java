package progetto.model.dao;

import progetto.exception.DAOException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class ModificaInterventoComeInRiparazioneDAO implements GenericProcedureDAO<Boolean> {
    @Override
    public Boolean execute(Object... params) throws DAOException {
        //Con questo metodo vado a modificare lo stato dell'intervento ponendolo su "In riparazione"
        int IDintervento=(int) params[0];
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{call aggiorna_stato_intervento_in_riparazione(?)}");
            cs.setInt(1, IDintervento);
            cs.executeQuery();

        } catch(SQLException e) {
            throw new DAOException("Errore aggiornamento dello stato intervento: " + e.getMessage());
        }
        return true;
    }
}
