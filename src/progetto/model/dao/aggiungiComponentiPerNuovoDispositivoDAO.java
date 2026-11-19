package progetto.model.dao;

import progetto.exception.DAOException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class aggiungiComponentiPerNuovoDispositivoDAO implements GenericProcedureDAO<Boolean>{
    @Override
    public Boolean execute(Object ... params) throws DAOException {
        String marca= (String) params[0];
        String modello = (String) params[1];
        String codice= (String) params[2];
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{call aggiungi_componenti_elettroniche_al_NuovoDispositivo(?,?,?)}");
            cs.setString(1, marca);
            cs.setString(2, modello);
            cs.setString(3, codice);
            cs.executeQuery();

        } catch(SQLException e) {
            throw new DAOException("Errore inserimento componente elettronico per il nuovo dispositivo: " + e.getMessage());
        }
        return true;
    }
}

