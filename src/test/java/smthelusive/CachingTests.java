package smthelusive;

import net.sf.ehcache.CacheManager;
import org.junit.Assert;
import org.junit.Test;

public class CachingTests {
    @Test
    public void queryCacheTest() {
        smthelusive.Test.saveEntity("superTest1");
        smthelusive.Test.saveEntity("superTest2");
        smthelusive.Test.saveEntity("superTest3");
        smthelusive.Test.getAllEntities();
        CacheManager m = CacheManager.getInstance();
        Assert.assertEquals(m.getCache("cachedEntities").getStatistics().getSize(), 3); // 3 entities
        Assert.assertEquals(m.getCache("queryCache").getStatistics().getSize(), 1); // 1 query
    }
}
