import java.io.Serializable;
import java.util.Date;
//import groovy.sql.Sql

//@Typed(TypePolicy.MIXED)
class Post implements Serializable{
	
	static hasMany = [topics:PostTopics]
	                                      
	String title
	String teaser
	String content
	Date creationDate = new Date()
	Date modifiedDate = new Date()
	Date endCommentsDate
	Integer author
	Status stat
	Section section
	Integer currentCnt = 0
	
	static constraints = {
	    title(size:1..50, nullable:false, blank:false)
	    teaser(size:1..1000, nullable:false, blank:false)
	    content(size:0..65536, nullable:true)
	    author(nullable:false)
		stat(nullable:true)
		section(nullable:false)
		endCommentsDate(nullable:true)
		currentCnt(nullable:false,blank:false)
	}

}

