package progetto.model.dao;

import progetto.exception.DAOException;
import progetto.model.domain.Ordine;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ListaOrdiniDAO implements GenericProcedureDAO<ArrayList<Ordine>>{
    @Override
    public ArrayList<Ordine> execute(Object ... params) throws DAOException {
        ArrayList<Ordine> lista=new ArrayList<>();
        Ordine ordine;
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{call lista_ordini()}");
            boolean status = cs.execute();
            if(status) {
                ResultSet rs = cs.getResultSet();
                while (rs.next()) {
                    ordine = new Ordine(rs.getInt(1),
                            rs.getInt(2),rs.getString(3));
                    lista.add(ordine);
                }
            }
        } catch(SQLException e) {
            throw new DAOException("Errore visualizzazione lista ordini: " + e.getMessage());
        }
        return lista;
    }
}
