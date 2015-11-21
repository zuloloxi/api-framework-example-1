import java.io.Serializable;
import org.bson.types.ObjectId

//@Typed(TypePolicy.MIXED)
class Section implements Serializable{

	static mapWith = "mongo"
	static hasMany = [ posts : Post ]

	ObjectId id
	String sectionName
	Boolean commentsAllowed = true
	
    static constraints = {
		sectionName(size:2..65, nullable:false, blank:false,unique:true)
		commentsAllowed(nullable:false, blank:false)
    }
}
