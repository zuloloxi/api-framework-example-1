

//import java.util.Date;

//import javax.servlet.http.HttpServletResponse


class TopicController {

	def springSecurityService
	def apiLayerService
                             
	static defaultAction = 'list'

	def list(){
		respond Topic.list()
	}
	
	def show(){
		respond Topic.get(params.id)
	}
	
	def save(Topic topicInstance){
		topicInstance.topicName = params.topicName
		
		if (!topicInstance.save(flush:true)) {
			topicInstance.errors.allErrors.each {
				println it
			}
			//render(status:HttpServletResponse.SC_NOT_FOUND, text: 'Could not save topic')
		}else{
			respond Topic.get(topicInstance.id)
		}
		return null
	}

	def update(Topic topicInstance){
		long version = topicInstance.version
		
		if (version!=params.version.toLong()) {
			//render(status:HttpServletResponse.SC_BAD_REQUEST, text: 'Another user has updated this Topic while you were editing.')
		}

		topicInstance.properties = params
		
		if (topicInstance.hasErrors()) {
			//render(status:HttpServletResponse.SC_NOT_FOUND, text: 'Could not update topic')
		}
		
		topicInstance.save()
		respond Topic.get(topicInstance.id)
	}
	
	def delete(){
		Topic topicInstance = Topic.get(params.id.toLong())
		if (topicInstance == null) {
			//render(status:HttpServletResponse.SC_NOT_FOUND)
		}

		topicInstance.delete(flush:true)
		//render(status:HttpServletResponse.SC_OK, text: "Topic ${params.id} was deleted")
		return null
	}
}
