

//import java.util.Date;

//import javax.servlet.http.HttpServletResponse


class SectionController {


	static defaultAction = 'list'

	def list(){
		respond Section.list()
	}
	
	def show(){
		def section = Section.get(params.id.toLong())
		if(section){
			respond section
		}
		return null
	}
	
	def create(){
        println("create called...")
        println(params)
		Section sectionInstance = new Section()
		sectionInstance.sectionName = params.sectionName
		sectionInstance.sectionName = (!params?.commentsAllowed?.empty)?params.sectionName:true

		if (!sectionInstance.save(flush:true)) {
            println("could not flush")
			sectionInstance.errors.allErrors.each { println it }
            //render(status:HttpServletResponse.SC_NOT_FOUND, text: 'Could not save section')
		}else{
			Section section = Section.get(sectionInstance.id)

            return ['section':section]
		}
	}

	def update(){
		Section sectionInstance = Section.get(params.id.toLong())
		long version = sectionInstance.version
		
		if(params?.version){
			if (version!=params.version.toLong()) {
				//render(status:HttpServletResponse.SC_BAD_REQUEST, text: 'Another user has updated this Section while you were editing.')
			}
		}
		sectionInstance.sectionName = params.sectionName
		sectionInstance.sectionName = (!params?.commentsAllowed?.empty)?params.sectionName:true
		
		if (sectionInstance == null){
			//render(status:HttpServletResponse.SC_BAD_REQUEST)
		}

		if (sectionInstance.hasErrors()) {
			sectionInstance.errors.allErrors.each { println it }
			//render(status:HttpServletResponse.SC_NOT_FOUND)
		}

		if(!sectionInstance.save(flush:true)){
			respond null
		}else{
			//apiToolkitService.callHook('test',testInstance,'update')
			respond Section.get(sectionInstance.id.toLong())
		}

		return null
	}

	def delete(Section sectionInstance){
		if (sectionInstance == null) {
			//render(status:HttpServletResponse.SC_NOT_FOUND)
		}

		sectionInstance.delete flush:true
		return null
	}
}
