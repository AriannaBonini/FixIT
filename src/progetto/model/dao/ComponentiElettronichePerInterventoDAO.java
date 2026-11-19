package progetto.model.dao;

import progetto.exception.DAOException;
import progetto.model.domain.ComponenteElettronico;
import progetto.model.domain.Intervento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComponentiElettronichePerInterventoDAO implements GenericProcedureDAO<ArrayList<ComponenteElettronico>> {
    @Override
    public ArrayList<ComponenteElettronico> execute(Object... params) throws DAOException {
        //Questo metodo restituisce la lista dei componenti elettronici che il tecnico può scegliere per la riparazione del dispositivo.
        //La ricerca dei componenti è effettuato rispettando il tipo di dispositivo in riparazione, ossia se è un cellulare o un computer portatile.
        ArrayList<ComponenteElettronico> lista = new ArrayList<>();
        ComponenteElettronico componenteElettronico;
        String marca=(String) params[0];
        String modello= (String)params[1];
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{call visualizza_lista_componenti_elettroniche_per_intervento(?,?)}");
            cs.setString(1,marca);
            cs.setString(2,modello);
            boolean status = cs.execute();
            if(status){
                ResultSet rs = cs.getResultSet();
                while (rs.next()){
                    componenteElettronico = new ComponenteElettronico(rs.getString(1), rs.getString(2));
                    lista.add(componenteElettronico);
                }
            }
        }catch(SQLException e){
            throw new DAOException("Errore visualizzazione lista interventi: " + e.getMessage());
        }
        return lista;
    }
}
