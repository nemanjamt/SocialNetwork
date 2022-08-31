Vue.component("post-full-view", {
	data: function () {
		    return {
		      post:null,
              postComments:null,
              currentLoggedUser:null,
              newComment:''
		    }
	},
	template: ` 
	
	
		
	<div v-if="post && postComments && currentLoggedUser" class="searchAllUsersBlock">
	
		<div class="split left">
			<div class="fullViewBox">
                <div>
                    
                    
                    <div  class="fullViewText">
                        {{post.postText}}
                    </div>
                    <div v-if="post.pictureName != 'null'" class="fullViewPhoto">						 
                        <img :src="'/userImages/'+post.pictureName" alt="post picture" >
                    </div>


                    <div v-if="currentLoggedUser.username == post.usernameCreator">
                        <button v-on:click="deletePost(post)" >delete post</button>
                        
                    </div>
                 </div>
		
                 
			
			
		    </div>
		
		</div>
			
		<div class="split right">
            <div>
                <div v-for="comment in this.postComments" class="postComment">
                    <textarea rows='4' v-model="comment.content" v-on:change="handleCommentChange(comment)" :readonly="currentLoggedUser.username !== comment.usernameCreator"></textarea>
                    <button v-if="currentLoggedUser.username == comment.usernameCreator" v-on:click="deleteComment(comment)">delete</button>
                    <button v-if="currentLoggedUser.username == comment.usernameCreator" v-on:click="saveChanges(comment)" :disabled="!comment.isEdited">save changes</button>
                </div>
            </div>
            <br>
            <hr>
            <div class="postComment">
                <textarea rows='4' v-model="newComment" ></textarea>
                <button v-on:click="addComment()">Dodaj komentar</button>
            </div>

		</div>
	
	</div>  
	
	
`
	, 
	methods : {
		init : function() {
			console.log("DA?");
		}, 
		addComment:function(){
            if(this.newComment == '')
                return;
            console.log("DODAJ KOMENTAR");
            console.log(this.newComment);
            let obj= {
                content:this.newComment,
                username:this.currentLoggedUser.username,
                postId:this.post.id
            };
            axios.post("/comments", JSON.stringify(obj)).then(response => {
                this.$router.go(0);
            });
        },
        deleteComment:function(comment){
            axios.delete("/comments/"+comment.id).then(response =>{
                this.$router.go(0);
            });
        },
        handleCommentChange:function(comment){
            comment.isEdited = true;
        },

        saveChanges: function(comment){
            let obj= {
                content:comment.content,
                username:this.currentLoggedUser.username,
                postId:this.post.id
            };
            axios.put("/comments/"+comment.id, JSON.stringify(obj)).then(result =>{
                this.$router.go(0);
            });
        },

        deletePost: function(post){
            axios.delete("/posts/"+post.id).then(response =>{
                this.$router.push('/view-profile?user='+post.usernameCreator);
            });
        }
		
		
		
	},
	mounted () {
        postId = this.$route.query.postId;
        if(postId == null){
            this.$router.push('/');
        }
        console.log(postId);
        axios.get('/currentUser').then(response => {
			if(response.data != null){
				this.currentLoggedUser = response.data;
	
			}else{
                this.$router.push("/");
            }
		} )



        axios.get('/post?id='+postId).then(response => {
           this.post = response.data;
        });
        this.postComments=[];
        axios.get('/comments/findByPost?postId='+postId).then(response => {
            this.postComments = response.data;
            console.log(response.data);
        });
    }
});