package progetto.model.dao;


import progetto.exception.DAOException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class inserisciCoppiaInterventoComponenteElettronicoDAO implements GenericProcedureDAO<Boolean> {
    @Override
    public Boolean execute(Object... params) throws DAOException {
        //Con questo metodo vado ad inserire la coppia intervento-componente elettronico nella tabella Sostituisce
        int IDintervento=(int) params[0];
        String codiceComponente=(String) params[1];
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{call inserisci_coppia_intervento_componenteElettronico(?,?)}");
            cs.setInt(1, IDintervento);
            cs.setString(2, codiceComponente);
            cs.executeQuery();

        } catch(SQLException e) {
            throw new DAOException("Errore inserimento coppia intervento-componenteElettronico: " + e.getMessage());
        }
        return true;
    }

}
