package daos;

import entities.CommonEntity;
import entities.Proveedor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.persistence.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CommonDao<T extends CommonEntity> {

    private static final Logger logger = LogManager.getLogger(CommonDao.class);
    protected static EntityManagerFactory emf;
    private static final String PERSISTENCE_UNIT = "gestor_database";
    private final Class<T> entityClass;

    protected CommonDao(Class<T> entityClass) {
        this.entityClass = entityClass;
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
    }

    /**
     * Recoge una entidad por Id
     * @param id
     * @return Entidades
     */
    public T get(Integer id) throws SQLException {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            T result = em.find(entityClass, id);
            em.getTransaction().commit();
            return result;
        } catch (Exception e){
            String errorMessage = String.format(
                "Error en base de datos. Error intentando recoger el elemento %s en la entidad %s", id, entityClass.getName()
            );
            logger.error(errorMessage, e);
            throw new SQLException(errorMessage, e);
        } finally {
            endTransaction(em);
        }
    }

    /**
     * Recoge una lista de entidades por petición y parámetros
     * @param query
     * @param parameters
     * @return Lista de entidades
     */
    public List<T> list(String query, Map<String,Object> parameters) throws SQLException {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            Query queryObject = em.createQuery(query);
            if (parameters != null && !parameters.isEmpty()) {
                for (Map.Entry<String,Object> parameter: parameters.entrySet()) {
                    queryObject.setParameter(parameter.getKey(), parameter.getValue());
                }
            }
            List<T> result = (List<T>)queryObject.getResultList();
            em.getTransaction().commit();
            return result;
        } catch (Exception e) {
            String errorMessage = String.format(
                "Error en base de datos. Error intentando listar elementos de la entidad %s", entityClass.getName()
            );
            logger.error(errorMessage, e);
            throw new SQLException(errorMessage, e);
        } finally {
            endTransaction(em);
        }
    }

    /**
     * Recoge una entidad por petición y parámetros
     * @param query
     * @param parameters
     * @return Entidad
     */
    public Optional<T> find(String query, Map<String,Object> parameters) throws SQLException {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            Query queryObject = em.createQuery(query);
            if (parameters != null && !parameters.isEmpty()) {
                for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                    queryObject.setParameter(parameter.getKey(), parameter.getValue());
                }
            }
            T result = (T) queryObject.getSingleResult();
            em.getTransaction().commit();
            return Optional.ofNullable(result);
        } catch (NoResultException e) {
            String message = String.format(
                "No se han encontrado resultados por la petición %s en la entidad %s", query, entityClass.getName()
            );
            logger.info(message, e);
            return Optional.empty();
        } catch (Exception e) {
            String errorMessage = String.format(
                "Error en la base de datos. Error intentando encontrar elementos de la entidad %s", entityClass.getName()
            );
            logger.error(errorMessage, e);
            throw new SQLException(errorMessage, e);
        } finally {
            endTransaction(em);
        }
    }

    /**
     * Inserta una entidad
     * @param entity
     * @return TRUE en caso de exito
     */
    public T insert(T entity) throws SQLException {
        EntityManager em = null;
        try{
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return entity;
        } catch (Exception e){
            String errorMessage = String.format(
                "Error en la base de datos. Error intentando insertar el elemento %s en la entidad %s", entity.getId(), entityClass.getName()
            );
            logger.error(errorMessage, e);
            throw new SQLException(errorMessage, e);
        } finally {
            endTransaction(em);
        }
    }

    /**
     * Actualiza una entidad
     * @param entity
     * @return TRUE en caso de exito
     */
    public boolean update(T entity) throws SQLException {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
            return true;
        } catch (Exception e){
            String errorMessage = String.format(
                "Error en la base de datos. Error intentando actualizar el elemento %s en la entidad %s", entity.getId(), entityClass.getName()
            );
            logger.error(errorMessage, e);
            throw new SQLException(errorMessage, e);
        } finally {
            endTransaction(em);
        }
    }

    /**
     * Elimina una entidad
     * @param entity
     * @return TRUE en caso de exito
     */
    public boolean remove(T entity) throws SQLException {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            entity = em.merge(entity);
            em.remove(entity);
            em.getTransaction().commit();
            return true;
        } catch (Exception e){
            String errorMessage = String.format(
                "Error en la base de datos. Error intentando eliminar el elemento %s en la entidad %s", entity.getId(), entityClass.getName()
            );
            logger.error(errorMessage, e);
            throw new SQLException(errorMessage, e);
        } finally {
            endTransaction(em);
        }
    }

    /**
     * Finaliza la transacción a la base de datos
     * @param em
     */
    private void endTransaction(EntityManager em) {
        if (em != null) {
            if (em.getTransaction() != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            if (em.isOpen()) {
                em.close();
            }
        }
    }
}