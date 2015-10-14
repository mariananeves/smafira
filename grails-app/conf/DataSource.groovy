dataSource {
    pooled = true
    jmxExport = true
//    driverClassName = "com.mysql.jdbc.Driver"
//    username = "root"
//    password = ""
    driverClassName = "com.mysql.jdbc.Driver"
    username = "root"
    password = "Admin4MySql"
//    driverClassName = "org.postgresql.Driver"
//    username = "assessment"
//    password = "assessment01"
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
//    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
    singleSession = true // configure OSIV singleSession mode
    flush.mode = 'manual' // OSIV session flush mode outside of transactional context
    jdbc.use_get_generated_keys = true
}

// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = "jdbc:mysql://localhost/assessment_test"
//            url = "jdbc:postgresql://fuedb.gesis.intra/assessment-test"
            logSql = true
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:mysql://localhost/assessment_test"
//            url = "jdbc:postgresql://fuedb.gesis.intra/assessment-test"
        }
    }
    production {
        dataSource {
            logSql = true
            dbCreate = "update"
            url = "jdbc:mysql://localhost/assessment"
//            url = "jdbc:postgresql://fuedb.gesis.intra/assessment"
            properties {
               // See http://grails.org/doc/latest/guide/conf.html#dataSource for documentation
               jmxEnabled = true
               initialSize = 5
               maxActive = 50
               minIdle = 5
               maxIdle = 25
               maxWait = 10000
               maxAge = 10 * 60000
               timeBetweenEvictionRunsMillis = 5000
               minEvictableIdleTimeMillis = 60000
               validationQuery = "SELECT 1"
               validationQueryTimeout = 3
               validationInterval = 15000
               testOnBorrow = true
               testWhileIdle = true
               testOnReturn = false
               jdbcInterceptors = "ConnectionState"
               defaultTransactionIsolation = java.sql.Connection.TRANSACTION_READ_COMMITTED
            }
        }
    }
}
