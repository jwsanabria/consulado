package co.gov.cancilleria.miconsulado.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableCacheEntryListenerConfiguration;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;
    
    private final javax.cache.configuration.CacheEntryListenerConfiguration<Object, Object> jcacheEntryListenerConfiguration;
    
    private final static Logger log = LoggerFactory.getLogger(CacheConfiguration.class);

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();
        
        jcacheEntryListenerConfiguration = new MutableCacheEntryListenerConfiguration<>(
                FactoryBuilder.factoryOf(CacheEventLogger.class), null, false, false);
        
        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }


    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> createCache(cm, co.gov.cancilleria.miconsulado.service.cms.GetMeshService.IMAGE_RESOURCE_CMS_BY_UUID_CACHE);
            // jhipster-needle-ehcache-add-entry;
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        
    	try {
    		javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
    		if (cache != null) {
                cm.destroyCache(cacheName);
            }
            cm.createCache(cacheName, jcacheConfiguration).registerCacheEntryListener(jcacheEntryListenerConfiguration);
    	}catch(Exception e) {
    		log.error("Error creando el cache: " + e.getMessage());
    	}
        
    }
}
