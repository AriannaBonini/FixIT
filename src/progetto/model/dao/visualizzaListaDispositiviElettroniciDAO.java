package progetto.model.dao;

import progetto.exception.DAOException;
import progetto.model.domain.DispositivoElettronico;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class visualizzaListaDispositiviElettroniciDAO implements GenericProcedureDAO<ArrayList<DispositivoElettronico>> {
    @Override
    public ArrayList<DispositivoElettronico> execute(Object... params) throws DAOException {
        //questo metodo restituisce la lista dei dispositivi elettronici
        ArrayList<DispositivoElettronico> lista = new ArrayList<>();
        DispositivoElettronico dispositivoElettronico;
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{call visualizza_lista_dispositivi_elettronici()}");
            boolean status = cs.execute();
            if(status){
                ResultSet rs = cs.getResultSet();
                while (rs.next()){
                    dispositivoElettronico = new DispositivoElettronico(rs.getString(1),
                            rs.getString(2));
                    lista.add(dispositivoElettronico);
                }
            }
        }catch(SQLException e){
            throw new DAOException("Errore visualizzazione lista dispositivi elettronici: " + e.getMessage());
        }
        return lista;
    }
}
