

import javax.servlet.http.HttpServletResponse
import org.springframework.web.servlet.ModelAndView

class PostController {

	def apiLayerService
                             
	static defaultAction = 'list'
	
	
	def list(){
		params.max = (params.max) ? params.max?.toInteger() : 10
		params.offset = (params.offset) ? params.offset?.toInteger() : 0
		
		def post =  Post.withCriteria() {
			maxResults(params.max?.toInteger())
			firstResult(params.offset?.toInteger())
			and {order('creationDate','desc')}
		}
		
		def rowCount = Post.createCriteria().get() {
			projections {
				rowCount()
			}
		}
		return ['posts':post, 'total':rowCount]
	}
	
	def listByTopic(){
		def topic = Topic.get(params.id);
		
		params.max = (params.max) ? params.max : 10
		params.offset = (params.offset) ? params.offset : 0

		def post =  Post.withCriteria() {
			maxResults(params.max?.toInteger())
			firstResult(params.offset?.toInteger())
			and {
				order('modifiedDate','desc')
				topics{eq("topic", topic)}
			}
		}
		
		def rowCount = Post.createCriteria().get() {
			topics{eq("topic", topic)}
			projections {rowCount()}
		}

		return ['topic':topic, 'posts':post, 'params':params, 'total':rowCount]
	}

	def listBySection(){
		def section = Section.get(params.id.toLong());
		
		def post =  Post.withCriteria() {
			order('modifiedDate','desc')
			eq("section", section)
		}
		
		def rowCount = Post.createCriteria().get() {
			eq("section", section)
			projections {rowCount()}
		}
		return ['section':section, 'posts':post, 'params':params, 'total':rowCount]
	}
	
	def show(){
		def post = Post.get(params.id.toLong())
		if(post){
            /*
            ModelAndView mav=new ModelAndView()
            mav.addObject("post", post)
            return mav
            */
            // return ['post':post]
		}
		return null
	}
	
	def create(){
		Person person = springSecurityService.currentUser
		Post post = new Post();

		post.title = params.title
		post.teaser = params.teaser
		post.content = params.content
		post.section = Section.get(params.section.toLong())
		post.stat = Status.get(5)
		
		Date now = new Date();
		post.creationDate = now;
		post.modifiedDate = now;
		post.endCommentsDate = (params.endCommentsDate) ? params.endCommentsDate : null
		
		post.author = person.id

		if (post.hasErrors()) {
			render(status:HttpServletResponse.SC_NOT_FOUND, text: 'Could not save Post. Check your data and try again')
		}
		
		if (!post.save(flush:true)) {
			render(status:HttpServletResponse.SC_NOT_FOUND, text: 'Could not save Post. Check your data and try again')
		}else{
            def newPost = Post.get(post.id.toLong())
			return ['post':newPost]
		}
		return null
	}
	
	def update(){
		Post postInstance = Post.get(params.id.toLong())
		Person person = springSecurityService.currentUser
		List roles = springSecurityService.getPrincipal().getAuthorities() as List
		if(postInstance.author==person.id || roles.contains('ROLE_ADMIN')){
			long version = postInstance.version
			
			postInstance.title = (params.title)?params.title:postInstance.title
			postInstance.teaser = (params.teaser)?params.teaser:postInstance.teaser
			postInstance.content = (params.content)?params.content:postInstance.content
			postInstance.section = (params.section)?Section.get(params.section.toLong()):postInstance.section
			postInstance.stat = (params.statId)?Status.get(params.statId.toLong()):postInstance.stat
			
			Date now = new Date();
			postInstance.modifiedDate = now;
			
			if (postInstance == null){
				render(status:HttpServletResponse.SC_BAD_REQUEST)
			}
	
			if (postInstance.hasErrors()) {
				postInstance.errors.allErrors.each { println it }
				render(status:HttpServletResponse.SC_NOT_FOUND)
			}
	
			if(params?.version){
				if (version!=params?.version?.toLong()) {
					render(status:HttpServletResponse.SC_BAD_REQUEST, text: 'Another user has updated this Post while you were editing.')
				}
			}
			
			if(!postInstance.save(flush:true)){
				respond null
			}else{
				//apiToolkitService.callHook('test',testInstance,'update')
				def newPost = Post.get(postInstance.id.toLong())
                return ['post':newPost]
			}
		}else{
			render(status:HttpServletResponse.SC_NOT_FOUND)
		}
		return null
	}
	
	def delete(){
		Post postInstance = Post.get(params.id.toLong())
		if (postInstance == null) {
			render(status:HttpServletResponse.SC_NOT_FOUND)
		}
		
		if(!postInstance.delete(flush:true)){
			return ['id':params.id]
		}else{
			return null
		}
	}
}
