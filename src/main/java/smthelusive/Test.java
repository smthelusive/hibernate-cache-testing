package smthelusive;

import net.sf.ehcache.CacheManager;
import org.hibernate.jpa.QueryHints;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class Test {
    private static EntityManagerFactory emf = Persistence
            .createEntityManagerFactory("smthelusive");

    public static void main(String[] args) {
        saveEntity("test1");
        saveEntity("test2");
        saveEntity("test3");

        // test query caching:
        System.out.println("query first time:");
        for (Entity e: getAllEntities()) {
            System.out.println(e.getName());
        }
        System.out.println("query second time:");
        for (Entity e: getAllEntities()) {
            System.out.println(e.getName());
        }

        // test entity caching:
        System.out.println("test entity caching:");
        Entity en = getEntity(1);
        System.out.println(en.getName());
        en = getEntity(1);
        System.out.println(en.getName());


        getAllEntities();
        for (CacheManager m: CacheManager.ALL_CACHE_MANAGERS) {
            System.out.println("cachedEntities statistics:\n" + m.getCache("cachedEntities").getStatistics().getSize());
            System.out.println("queryCache statistics:\n" + m.getCache("queryCache").getStatistics().getSize());
        }
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void saveEntity(String name) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Entity entity = new Entity();
        entity.setName(name);
        em.persist(entity);
        em.getTransaction().commit();
    }

    public static Entity getEntity(int id) {
        EntityManager em = getEntityManager();
        Entity entity = em.find(Entity.class, id);
        em.detach(entity);
        return entity;
    }

    @SuppressWarnings("unchecked")
    public static List<Entity> getAllEntities() {
        EntityManager em = getEntityManager();
        return em.createQuery("SELECT entity from Entity entity")
                .setHint(QueryHints.HINT_CACHEABLE, "true")
                .setHint(QueryHints.HINT_CACHE_REGION, "queryCache")
                .getResultList();
    }
}
