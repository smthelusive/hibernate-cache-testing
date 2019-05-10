package smthelusive;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Table;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "cachedEntities")
@javax.persistence.Entity
@Table(name = "entity")
public class Entity extends BaseEntity {
    Entity() {}
}
