import java.io.Serializable;

//@Typed(TypePolicy.MIXED)
class Section implements Serializable{
	
	static hasMany = [ posts : Post ] 

	String sectionName
	Boolean commentsAllowed = true
	
    static constraints = {
		sectionName(size:2..65, nullable:false, blank:false,unique:true)
		commentsAllowed(nullable:false, blank:false)
    }
}
