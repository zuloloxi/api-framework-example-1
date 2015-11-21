import org.bson.types.ObjectId

//@Typed(TypePolicy.MIXED)
class Status {
	
	static hasMany = [posts: Post ]

	static mapWith = "mongo"

	ObjectId id
	String statName
	
    static constraints = {
		statName(size:2..255, nullable:false, blank:false,unique:true)
    }
}
