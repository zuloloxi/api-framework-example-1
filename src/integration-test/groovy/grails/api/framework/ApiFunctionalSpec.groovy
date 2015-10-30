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
                accept("application/json")
                contentType("application/json")
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
            RestResponse response = rest.post("http://localhost:8080/${entryPoint}/post/create") {
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
}
