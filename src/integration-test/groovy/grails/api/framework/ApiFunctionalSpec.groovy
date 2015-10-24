package grails.api.framework

import grails.util.Metadata

import grails.test.mixin.integration.Integration
import grails.transaction.*

import spock.lang.*
import geb.spock.*
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@Integration
@Rollback
class ApiFunctionalSpec extends GebSpec {

    String entryPoint = "v${Metadata.current.getApplicationVersion()}"


    void "test show call (GET)"() {
        given:
            RestBuilder rest = new RestBuilder()

        when:
            RestResponse response = rest.get("http://localhost:8080/${entryPoint}/post/show/1") {
                //auth System.getProperty("artifactory.user"), System.getProperty("artifactory.pass")
                contentType "application/json"
            }
        then:
            response.status == 200
            response.json.title == "test post"
    }

    /*
    void "test create call (PUT)"() {
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
}
