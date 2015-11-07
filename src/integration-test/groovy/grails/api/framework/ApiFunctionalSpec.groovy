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

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@Integration
@Rollback
class ApiFunctionalSpec extends Specification {

    def grailsApplication

    String entryPoint = "v${Metadata.current.getApplicationVersion()}"

    static Long primerId = 1

    static Long id = null
    static Long version = null
    static int user = 0

    // foreign key holders
    static LinkedHashMap fkeys = ['sectionId':0,'authorId':0]

    def output
    def errOutput
    
    void "test login (POST)"() {
        // set these variables in your config or external properties file (preferable)
        String login = HOLDER.config.root.login
        String password = HOLDER.config.root.password
        Object json
        LinkedHashMap output = [:]

        when:
            // ### ADD: delete cookie.txt prior to running test

            def ant = new AntBuilder()
            ant.exec(outputProperty:"cmdOut",errorProperty:"cmdErr",resultProperty:"cmdExit",failonerror:"false",executable:"curl"){
                arg(line:"""--verbose --request POST --data "j_username=${login}&j_password=${password}&_spring_security_remember_me=checked" http://localhost:8080/api_v0.1/j_spring_security_check --cookie-jar cookies.txt""")
            }
            output = parseOutput(ant.project.properties.cmdErr)
            def personClass = HOLDER.getGrailsApplication().getDomainClass('net.nosegrind.apiframework.Person').clazz
            def principal = personClass.findByUsername(login)
            this.user = principal.id

        then:
            assert output.response.code.code == '302'
            assert output.response.code.message == 'Found'
    }

    def 'Check expected input'(){
        Object json
        LinkedHashMap errOutput = [:]
        def output = ['id', 'sectionId', 'statId', 'title','version']

        when:
            def ant = new AntBuilder()
            ant.exec(outputProperty:"cmdOut",errorProperty:"cmdErr",resultProperty:"cmdExit",failonerror:"false",executable:"curl"){
                arg(line:"""--verbose --request GET --header "Content-Type: application/json" "http://localhost:8080/${entryPoint}/post/show/${this.primerId}" --cookie cookies.txt""")
            }
            errOutput = parseOutput(ant.project.properties.cmdErr)
            json = new JsonSlurper().parseText(ant.project.properties.cmdOut)
            this.fkeys.sectionId = (json.sectionId)?json.sectionId.toLong():null
            this.fkeys.statId = (json.statId)?json.statId.toLong():null
        then:
            assert errOutput.response.code.code == '200'
            assert json.collect(){it.key} == output
    }
/*
    void "test show call (GET)"() {
        given:
            RestBuilder rest = new RestBuilder()
        when:
            RestResponse response = rest.get("http://localhost:8080/${entryPoint}/post/show/1") {
                accept("application/json")
                contentType("application/json")
            }
        then:
            response.status == 200
            response.json.title == "test post"
    }

    void "test create call (PUT)"() {
        given:
            RestBuilder rest = new RestBuilder()
        when:
            RestResponse response = rest.put("http://localhost:8080/${entryPoint}/post/create") {
                accept("application/json")
                contentType("application/json")
                json {
                    "{'title': 'test post','teaser': 'This is just a test post to see if this works. Testing the api post system.','content':'Lorem ipsum dolor sit amet, consectetur adipiscing elit. In vel consequat nisl, quis commodo neque. Integer ultrices vitae nulla lacinia rutrum. Duis ut porta arcu, sed gravida tortor. Donec pulvinar elit turpis, ultricies tristique mi auctor ac. Ut elementum ullamcorper risus ac sollicitudin. Morbi semper ultrices enim vel euismod. Proin eleifend orci ac elit mollis tempor. Nulla egestas odio eu volutpat eleifend. Nunc nec massa eget nisl sodales posuere. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Nunc accumsan pretium sapien a tincidunt. Sed at fringilla mi.','section':2}"
                }
            }

        then:
        assert response.status == 200
    }


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
        println('request : '+req)
        println('response : '+resp)
        return ['request':req,'response':resp]
    }
}
