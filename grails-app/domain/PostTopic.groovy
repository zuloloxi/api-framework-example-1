import org.bson.types.ObjectId

//@Typed(TypePolicy.MIXED)
class PostTopic implements Serializable{

	static mapWith = "mongo"
	static belongsTo = [Post,Topic]

	ObjectId id
	Post post
	Topic topic
	
    static constraints = {
		post(nullable:false, blank:false)
		topic(nullable:false, blank:false)
    }
}
