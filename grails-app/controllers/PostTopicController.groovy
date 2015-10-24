import java.util.Date;

class PostTopicController {

	def springSecurityService
	def apiLayerService
                             
	static defaultAction = 'show'

	def showByPost(){
		List list = PostTopic.executeQuery("select PT from PostTopic PT where PT.post.id=?",[params.id.toLong()]);
		if(list){
			return ['showByPost':list]
		}
	}
	
	def showByTopic(){
		List list = PostTopic.executeQuery("select PT from PostTopic PT where PT.topic.id=?",[params.id.toLong()]);
		if(list){
			return ['showByTopic':list]
		}
	}

	def addPostTopic(){
		Post post = Post.get(params.id)
		for (String key in params.keySet()) {
			if (key=='addlist') {
				if(params.get(key).size()>1){
					List topics = params.get(key)
					topics.each{ v ->
						PostTopic postTopic = new PostTopic();
						postTopic.properties = params;
						Topic topic = Topic.get(v.toLong())
						postTopic.post = post
						postTopic.topic = topic
						postTopic.save()
					}
				}else{
					def postTopic = new PostTopic();
					postTopica.properties = params;
					Topic topic = Topic.get(params.get(key))
					postTopic.post = post
					postTopic.topic = topic
					postTopic.save()
				}
			}
		}
		List list = PostTopic.executeQuery("select T from Topic T left join T.posts P where P.post.id=?",[params.id.toLong()]);
		LinkedHashMap model = [:]
		render model as Object, [model:[list:list]]
	}

	def delPostTopic(){
		Post post = Post.get(params.id)
		for (String key in params.keySet()) {
			if (key=='dellist') {
				if(params.get(key).size()>1){
					List topics = params.get(key)
					topics.each{ v ->
						Topic topic = Topic.get(v.toLong())
						def postTopic = PostTopic.findByPostAndTopic(post,topic)
						postTopic.delete()
					}
				}else{
					Topic topic = Topic.get(params.get(key))
					def postTopic = PostTopic.findByPostAndTopic(post,topic)
					postTopic.delete()
				}
			}
		}
		List list = PostTopic.executeQuery( "select T from Topic T left join T.posts P where P.post.id=?",[params.id.toLong()]);
		LinkedHashMap model = [:]
		render model as Object, [model:[list:list]]
	}
}
