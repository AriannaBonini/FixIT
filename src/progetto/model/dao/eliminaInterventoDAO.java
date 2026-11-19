package progetto.model.dao;

import progetto.Utilities;
import progetto.exception.DAOException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class eliminaInterventoDAO implements GenericProcedureDAO<Boolean> {
    @Override
    public Boolean execute(Object... params) throws DAOException {
        //Con questo metodo vado ad eliminare l'intervento
        int IDintervento = (int) params[0];
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{call elimina_intervento(?)}");
            cs.setInt(1, IDintervento);
            cs.executeQuery();

        } catch (SQLException e) {
            if (e.getSQLState().equals("45000")) {
                throw new DAOException(Utilities.eccezioneEliminaIntervento);
            } else {
                throw new DAOException("Errore eliminazione intervento: " + e.getMessage());
            }
        }
        return true;
    }
}
