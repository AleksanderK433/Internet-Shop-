package com.shop.internetshop.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(CacheConfiguration.class);

    @Bean
    public CacheManager cacheManager() {
        logger.info("[CACHE] Konfiguracja Cache Manager - ConcurrentMapCache");

        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();

        cacheManager.setCacheNames(java.util.Arrays.asList(
                "products", "featuredProducts", "categories",
                "categoryList", "categoryTree", "productSearch", "productStats"
        ));

        logger.info("[CACHE] ✅ Cache Manager skonfigurowany z cache: {}", cacheManager.getCacheNames());
        return cacheManager;
    }
}