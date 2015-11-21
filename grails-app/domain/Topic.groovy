import java.io.Serializable;
import org.bson.types.ObjectId

class Topic implements Serializable{

	static mapWith = "mongo"
	static hasMany = [ posts : PostTopic ]

	ObjectId id
	String topicName
	
    static constraints = {
		topicName(size:2..65, nullable:false, blank:false,unique:true)
    }
}
