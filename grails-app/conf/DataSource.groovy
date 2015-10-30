
hibernate {
	cache {
		use_second_level_cache = true
		use_query_cache = false
		provider_class = 'org.hibernate.cache.EhCacheProvider'
		//region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
		region {
			factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory'
		}
	}
}

// environment specific settings
environments {
	development {
		dataSources {
			dataSource {
				dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
				url = "jdbc:h2:mem:coeus;MVCC=TRUE;LOCK_TIMEOUT=10000"
				properties {
					maxActive = 100
					maxIdle = 25
					minIdle = 5
					initialSize = 5
					maxWait = 10000
					minEvictableIdleTimeMillis = 1800000
					timeBetweenEvictionRunsMillis = 1800000
					numTestsPerEvictionRun = 3
					testOnBorrow = true
					testWhileIdle = true
					testOnReturn = false
					validationQuery = "SELECT 1"
				}
			}

			user {
				dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
				url = "jdbc:h2:mem:user;MVCC=TRUE;LOCK_TIMEOUT=10000"
				properties {
					maxActive = 100
					maxIdle = 5
					minIdle = 1
					initialSize = 1
					maxWait = 10000
					minEvictableIdleTimeMillis = 1800000
					timeBetweenEvictionRunsMillis = 1800000
					numTestsPerEvictionRun = 3
					testOnBorrow = true
					testWhileIdle = true
					testOnReturn = false
					validationQuery = "SELECT 1"
				}
			}
		}
	}
	
	production {
		dataSources {
			dataSource {
				dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
				url = "jdbc:h2:mem:coeus;MVCC=TRUE;LOCK_TIMEOUT=10000"
				properties {
					maxActive = 100
					maxIdle = 25
					minIdle = 5
					initialSize = 5
					maxWait = 10000
					minEvictableIdleTimeMillis = 1800000
					timeBetweenEvictionRunsMillis = 1800000
					numTestsPerEvictionRun = 3
					testOnBorrow = true
					testWhileIdle = true
					testOnReturn = false
					validationQuery = "SELECT 1"
				}
			}

			user {
				dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
				url = "jdbc:h2:mem:user;MVCC=TRUE;LOCK_TIMEOUT=10000"
				properties {
					maxActive = 100
					maxIdle = 5
					minIdle = 1
					initialSize = 1
					maxWait = 10000
					minEvictableIdleTimeMillis = 1800000
					timeBetweenEvictionRunsMillis = 1800000
					numTestsPerEvictionRun = 3
					testOnBorrow = true
					testWhileIdle = true
					testOnReturn = false
					validationQuery = "SELECT 1"
				}
			}
		}
	}
}
