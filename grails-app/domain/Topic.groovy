import java.io.Serializable;

class Topic implements Serializable{
	
	static hasMany = [ posts : PostTopic ] 
	                   
	String topicName
	
    static constraints = {
		topicName(size:2..65, nullable:false, blank:false,unique:true)
    }
}
