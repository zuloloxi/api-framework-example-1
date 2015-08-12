import java.util.Date;

class PostTopicController {

	def springSecurityService
	def apiLayerService
                             
	static defaultAction = 'show'

	def show(){
		List list = PostTopics.executeQuery( "select T.* from Topic T left join T.posts P where P.post.id=?",[params.id.toLong()]);
		LinkedHashMap model = [:]
		render model as Object, [model:[list:list]]
	}
	
	def addPostTopics(){
		Post post = Post.get(params.id)
		for (String key in params.keySet()) {
			if (key=='addlist') {
				if(params.get(key).size()>1){
					List topics = params.get(key)
					topics.each{ v ->
						PostTopics postTopics = new PostTopics();
						postTopics.properties = params;
						Topic topic = Topic.get(v.toLong())
						postTopics.post = post
						postTopics.topic = topic
						postTopics.save()
					}
				}else{
					def postTopics = new PostTopics();
					postTopics.properties = params;
					Topic topic = Topic.get(params.get(key))
					postTopics.post = post
					postTopics.topic = topic
					postTopics.save()
				}
			}
		}
		List list = PostTopics.executeQuery( "select T.* from Topic T left join T.posts P where P.post.id=?",[params.id.toLong()]);
		LinkedHashMap model = [:]
		render model as Object, [model:[list:list]]
	}

	def delPostTopics(){
		Post post = Post.get(params.id)
		for (String key in params.keySet()) {
			if (key=='dellist') {
				if(params.get(key).size()>1){
					List topics = params.get(key)
					topics.each{ v ->
						Topic topic = Topic.get(v.toLong())
						def postTopics = PostTopics.findByPostAndTopic(post,topic)
						postTopics.delete()
					}
				}else{
					Topic topic = Topic.get(params.get(key))
					def postTopics = PostTopics.findByPostAndTopic(post,topic)
					postTopics.delete()
				}
			}
		}
		List list = PostTopics.executeQuery( "select T.* from Topic T left join T.posts P where P.post.id=?",[params.id.toLong()]);
		LinkedHashMap model = [:]
		render model as Object, [model:[list:list]]
	}
}
