package progetto.model.dao;

import progetto.Utilities;
import progetto.exception.DAOException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class ConcludiInterventoDAO  implements GenericProcedureDAO<Boolean> {
    @Override
    public Boolean execute(Object... params) throws DAOException {
        //Con questo metodo vado a modificare lo stato dell'intervento ponendolo su "Concluso"
        int IDintervento=(int) params[0];
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{call aggiorna_stato_intervento_concluso(?)}");
            cs.setInt(1, IDintervento);
            cs.executeQuery();

        } catch(SQLException e) {
            if (e.getSQLState().equals("45000")) {
                    throw new DAOException(Utilities.eccezioneConcludiIntervento);
            } else {
                throw new DAOException("Errore aggiornamento dello stato intervento: " + e.getMessage());
            }
        }
        return true;
    }
}
