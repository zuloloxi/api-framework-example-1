//@Typed(TypePolicy.MIXED)
class PostTopics implements Serializable{
	
	static belongsTo = [Post,Topic] 
	
	Post post
	Topic topic
	
    static constraints = {
		post(nullable:false, blank:false)
		topic(nullable:false, blank:false)
    }
}
