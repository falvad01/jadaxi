/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import java.util.List;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import modelo.User;

/**
 *
 * @author jadaxi
 */
@Stateless
public class UserFacade extends AbstractFacade<User> implements UserFacadeLocal {

    @PersistenceContext(unitName = "com.mycompany_inso3_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }

    
    /**
     * 
     * @param user
     * @return 
     * 
     * Obtenemos el usuario a partir de un nombre de usuario y una contraseña
     */
    @Override
    
    //TODO decir cuando las credenciales no son correctas
    public User getUserURL(User user) {
        List<User> results = null;
        try {
            String hql = "FROM User u WHERE u.userName=:param1 and u.password=:param2";
            Query query = em.createQuery(hql);
            query.setParameter("param1", user.getUserName());
            query.setParameter("param2", user.getPassword());
            results = query.getResultList();

            System.out.println(results.get(0).toString());
        } catch (Exception e) {
            System.out.println("Algo ha salido mal al loguearse: " + e.getMessage());
        }
        return results.get(0);

    }

}