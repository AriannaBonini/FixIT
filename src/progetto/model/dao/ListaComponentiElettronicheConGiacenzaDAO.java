package progetto.model.dao;

import progetto.exception.DAOException;
import progetto.model.domain.ComponenteElettronico;
import progetto.model.domain.DispositivoElettronico;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ListaComponentiElettronicheConGiacenzaDAO implements GenericProcedureDAO<ArrayList<ComponenteElettronico>> {
    @Override
    public ArrayList<ComponenteElettronico> execute(Object... params) throws DAOException {
        //questo metodo restituisce la lista dei componenti elettronici con la rispettiva giacenza in magazzino
        ArrayList<ComponenteElettronico> lista = new ArrayList<>();
        ComponenteElettronico componenteElettronico;
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{call lista_componenti_elettronici_conGiacenza()}");
            boolean status = cs.execute();
            if (status) {
                ResultSet rs = cs.getResultSet();
                while (rs.next()) {
                    componenteElettronico = new ComponenteElettronico(rs.getString(1),
                            rs.getString(2), rs.getInt(3));
                    lista.add(componenteElettronico);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore visualizzazione lista componenti elettronici: " + e.getMessage());
        }
        return lista;
    }
}