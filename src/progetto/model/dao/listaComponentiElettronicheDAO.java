package progetto.model.dao;

import progetto.exception.DAOException;
import progetto.model.domain.ComponenteElettronico;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class listaComponentiElettronicheDAO implements GenericProcedureDAO<ArrayList<ComponenteElettronico>> {
    @Override
    public ArrayList<ComponenteElettronico> execute(Object... params) throws DAOException {
        //questo metodo restituisce la lista dei componenti elettronici
        String tipoDispositivo=(String) params[0];
        ArrayList<ComponenteElettronico> lista = new ArrayList<>();
        ComponenteElettronico componenteElettronico;
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{call lista_componenti_elettronici(?)}");
            cs.setString(1,tipoDispositivo);
            boolean status = cs.execute();
            if (status) {
                ResultSet rs = cs.getResultSet();
                while (rs.next()) {
                    componenteElettronico = new ComponenteElettronico(rs.getString(1),
                            rs.getString(2));
                    lista.add(componenteElettronico);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore visualizzazione lista componenti elettronici: " + e.getMessage());
        }
        return lista;
    }
}
