package progetto.model.dao;

import progetto.exception.DAOException;
import java.sql.*;

public class calcoloCostoInterventoDAO implements GenericProcedureDAO<Boolean>{
    @Override
    public Boolean execute(Object... params) throws DAOException {
        int ID = (int) params[0];
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{call calcolo_costo_intervento(?)}");
            cs.setInt(1, ID);
            cs.execute();
        }catch(SQLException e){
            throw new DAOException("Errore calcolo costo intervento: " + e.getMessage());
        }
        return true;
    }

}
