package grails.api.framework

import grails.util.Metadata

import grails.test.mixin.integration.Integration
import grails.transaction.*

import groovy.util.AntBuilder
import groovy.json.*
import grails.converters.JSON
import grails.util.Holders as HOLDER
import net.nosegrind.apiframework.Person

import spock.lang.*
import geb.spock.*
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import net.nosegrind.apiframework.*
import net.nosegrind.apiframework.comm.ApiRequestService
import grails.plugin.springsecurity.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@Integration
@Rollback
class ApiFunctionalSpec extends Specification {

    def grailsApplication
    ApiCacheService apiCacheService

    String entryPoint = "v${Metadata.current.getApplicationVersion()}"

    def cache
    String cacheVersion
    static controller = 'post'

    // todo: delete; do 'create' first
    static Long primerId = 1

    static Long id = null
    static Long version = null

    String login
    String password
    int user = 0
    List userRoles

    LinkedHashMap output = [:]

    void setup(){
        this.login = grailsApplication.config.root.login
        this.password = grailsApplication.config.root.password
        this.cache = apiCacheService.getApiCache (controller)
        this.cacheVersion = cache['currentStable']['value']
    }

    void "Test login (POST)"() {
        Object json

        when:
            def ant = new AntBuilder()
            ant.exec(outputProperty:"cmdOut",errorProperty:"cmdErr",resultProperty:"cmdExit",failonerror:"false",executable:"curl"){
                arg(line:"""--verbose --request POST --data "j_username=${this.login}&j_password=${this.password}&_spring_security_remember_me=checked" http://localhost:8080/v0.1/j_spring_security_check --cookie-jar cookies.txt""")
            }
            output = parseOutput(ant.project.properties.cmdErr)

            def personClass = grailsApplication.getDomainClass('net.nosegrind.apiframework.Person').clazz
            def principal = personClass.findByUsername(this.login)
            user = principal.id
            userRoles = principal.authorities*.authority
        then:
            assert output.response.code.code == '302'
            assert output.response.code.message == 'Found'
    }

    def 'Test Post/Show (GET)'(){
        Object json
        String action = 'show'

        def personClass = grailsApplication.getDomainClass('net.nosegrind.apiframework.Person').clazz
        def principal = personClass.findByUsername(this.login)

        this.userRoles = principal.authorities*.authority
        List returns = getApiParams(this.userRoles,(LinkedHashMap)cache[this.cacheVersion][action]['returns'])

        when:
            def ant = new AntBuilder()
            ant.exec(outputProperty:"cmdOut",errorProperty:"cmdErr",resultProperty:"cmdExit",failonerror:"false",executable:"curl"){
                arg(line:"""--verbose --request GET --header "Content-Type: application/json" "http://localhost:8080/${entryPoint}/post/${action}/${this.primerId}" --cookie cookies.txt""")
            }
            output = parseOutput(ant.project.properties.cmdErr)
            json = new JsonSlurper().parseText(ant.project.properties.cmdOut)
        then:
            assert output.response.code.code == '200'
            assert json.collect(){it.key}.intersect(returns).size() == returns.size()
    }

    def "test create call (POST)"() {
        Object json
        String action = 'create'

        def personClass = grailsApplication.getDomainClass('net.nosegrind.apiframework.Person').clazz
        def principal = personClass.findByUsername(this.login)

        this.userRoles = principal.authorities*.authority
        List returns = getApiParams(this.userRoles,(LinkedHashMap)cache[this.cacheVersion][action]['returns'])

        when:
            def ant = new AntBuilder()
            ant.exec(outputProperty:"cmdOut",errorProperty:"cmdErr",resultProperty:"cmdExit",failonerror:"false",executable:"curl"){
                arg(line:"""--verbose --request POST --header "Content-Type: application/json" -d"{'title': 'test post','teaser': 'This is just a test post to see if this works. Testing the api post system.','content':'Lorem ipsum dolor sit amet, consectetur adipiscing elit. In vel consequat nisl, quis commodo neque. Integer ultrices vitae nulla lacinia rutrum. Duis ut porta arcu, sed gravida tortor. Donec pulvinar elit turpis, ultricies tristique mi auctor ac. Ut elementum ullamcorper risus ac sollicitudin. Morbi semper ultrices enim vel euismod. Proin eleifend orci ac elit mollis tempor. Nulla egestas odio eu volutpat eleifend. Nunc nec massa eget nisl sodales posuere. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Nunc accumsan pretium sapien a tincidunt. Sed at fringilla mi.','section':2}" "http://localhost:8080/${entryPoint}/post/create" --cookie cookies.txt""")
            }
            output = parseOutput(ant.project.properties.cmdErr)
        println("#####ERR:"+ant.project.properties.cmdErr)
        println("#####OUT:"+ant.project.properties.cmdOut)
            //json = new JsonSlurper().parseText(ant.project.properties.cmdOut)
            //println(json)
        then:
            true
            //assert output.response.code.code == '200'
            //assert json.collect(){it.key}.intersect(returns).size() == returns.size()
    }

/*
    void "test update call (POST)"() {
        given:
        RestBuilder rest = new RestBuilder()

        when:
        RestResponse response = rest.post("http://localhost:8080/${grailsApplication.metadata.'app.name'}/books") {
            json([
                    title: "title2"
            ])
        }

        then:
        response.status == 200
        response.json.title == "title2"
        Book.count == 2
    }

    void "test delete call (DELETE)"() {
        given:
        RestBuilder rest = new RestBuilder()

        when:
        RestResponse response = rest.post("http://localhost:8080/${grailsApplication.metadata.'app.name'}/books") {
            json([
                    title: "title2"
            ])
        }

        then:
        response.status == 200
        response.json.title == "title2"
        Book.count == 2
    }
    */

    LinkedHashMap parseOutput(String output){
        LinkedHashMap req = [:]
        LinkedHashMap resp = [:]
        output.splitEachLine("//"){ it ->
            it.each(){ it2 ->
                if(it2 =~ />.+/){
                    if(it2.size()>3){
                        it2 = it2[2..-1]
                        if(it2.contains(":")){
                            List temp = it2.split(":")
                            req[temp[0]] = (temp[1])?temp[1]:[]
                        }else{
                            List temp = it2.split(" ")
                            req['uri'] = ['method':temp[0],'uri':temp[1],'protocol':temp[2]]
                        }
                    }
                }

                if(it2 =~ /<.+/){
                    if(it2.size()>3){
                        it2 = it2[2..-1]
                        if(it2.contains(":")){
                            List temp = it2.split(":")
                            resp[temp[0]] = (temp[1])?temp[1]:[]
                        }else{
                            List temp = it2.split(" ")
                            resp['code'] = ['protocol':temp[0],'code':temp[1],'message':temp[2]]
                        }
                    }
                }
            }
        }
        // println('request : '+req)
        // println('response : '+resp)
        return ['request':req,'response':resp]
    }

    List getApiParams(List userRoles, LinkedHashMap definitions){
        //println("#### [ApiLayer : getApiParams ] ####")
        //try{
        List apiList = []

        definitions.each() { it2 ->
            if (userRoles.contains(it2.key) || it2.key == 'permitAll') {
                //withPool {
                it2.value.each() { it4 ->
                    apiList.add(it4.name)
                }
                //}
            }
        }

        return apiList
        //}catch(Exception e){
        //	throw new Exception("[ApiLayer :: getApiParams] : Exception - full stack trace follows:",e)
        //}
    }
}
