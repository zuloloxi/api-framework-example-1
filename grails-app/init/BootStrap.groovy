import net.nosegrind.apiframework.ApiCacheService;
import net.nosegrind.apiframework.ApiObjectService;

//import grails.plugins.GrailsPluginManager
//import grails.plugins.GrailsPlugin

class BootStrap {

	//def grailsApplication
	ApiObjectService apiObjectService
	ApiCacheService apiCacheService
	
    def init = { servletContext ->
		/*
		def plugins = pluginMngr.getAllPlugins()
		plugins.each{
			println(it)
		}
		*/
		apiObjectService.initialize()
		def test = apiCacheService.getCacheNames()
    }
    def destroy = {
    }
}
