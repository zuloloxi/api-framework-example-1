//@Typed(TypePolicy.MIXED)
class Status {
	
	static hasMany = [posts: Post ]
	                   
	String statName
	
    static constraints = {
		statName(size:2..255, nullable:false, blank:false,unique:true)
    }
}
