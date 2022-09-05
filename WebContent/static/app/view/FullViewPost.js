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
	
	
<div class="postBlock">

    <div class="photoSection">
        <div class="picSection">
            <div>
                <div  class="fullViewText">
                    {{post.postText}}
                </div>
                <div v-if="post.pictureName != 'null'" class="fullViewPhoto">						 
                    <img :src="'/userImages/'+post.pictureName" alt="post picture" >
                </div>
    
    
                <div v-if="currentLoggedUser.username == post.usernameCreator">
                    <button v-on:click="deletePost(post)"> Delete post </button>
                </div>
             </div>
        </div>
    
    </div>
	
	<div class="split right" style="margin-top: -850px;">
	    
	    <div class="sortUsersBlock addComment"> 
	        <textarea v-model="newComment" class="inputComment" placeholder="Add a comment..."> </textarea>
            <button v-on:click="addComment()" class="btn"> Post </button>
	    </div>
	    
	    <div class="showUsersBlock" style="margin-top: 65px;"> 
	         <div v-for="comment in this.postComments" class="commentBlock">
	            <div class="commentBtns"> 
<!--                    <button v-if="currentLoggedUser.username == comment.usernameCreator" v-on:click="deleteComment(comment)"> Delete </button>-->
<!--                    <button v-if="currentLoggedUser.username == comment.usernameCreator" v-on:click="saveChanges(comment)" :disabled="!comment.isEdited"> Save changes </button>-->
                    <img v-if="currentLoggedUser.username == comment.usernameCreator" v-on:click="deleteComment(comment)" src="/userImages/deleteIcon.png" style="width: 24px; height: 24px; float: right;">
                    <img v-if="currentLoggedUser.username == comment.usernameCreator" v-on:click="saveChanges(comment)" :disabled="!comment.isEdited" src="/userImages/saveIcon.png" style="width: 24px; height: 24px; margin-right: 24px;  float: right">
	            </div>
	            
                <div class="commentPart"> 
                    <textarea v-model="comment.content" v-on:change="handleCommentChange(comment)" :readonly="currentLoggedUser.username !== comment.usernameCreator" class="commentContent"></textarea>
                </div>
                
                <div class="datePart"> 
                    <p style="color: #757576"> Date: </p>
                </div>
	         
	         
	         
                </div>
	    </div>
	
    </div>
	
	
</div>
	
		
<!--	<div v-if="post && postComments && currentLoggedUser" class="">-->
<!--	-->
<!--        <div class="picSection">-->
<!--            <div>-->
<!--                <div  class="fullViewText">-->
<!--                    {{post.postText}}-->
<!--                </div>-->
<!--                <div v-if="post.pictureName != 'null'" class="fullViewPhoto">						 -->
<!--                    <img :src="'/userImages/'+post.pictureName" alt="post picture" >-->
<!--                </div>-->
<!--    -->
<!--    -->
<!--                <div v-if="currentLoggedUser.username == post.usernameCreator">-->
<!--                    <button v-on:click="deletePost(post)"> Delete post </button>-->
<!--                </div>-->
<!--             </div>-->
<!--        </div>-->
<!--			-->
<!--			comment section -->
<!--		<div class="">-->
<!--            -->
<!--                <div v-for="comment in this.postComments" class="postComment">-->
<!--                    <textarea rows='4' v-model="comment.content" v-on:change="handleCommentChange(comment)" :readonly="currentLoggedUser.username !== comment.usernameCreator"></textarea>-->
<!--                    <button v-if="currentLoggedUser.username == comment.usernameCreator" v-on:click="deleteComment(comment)">delete</button>-->
<!--                    <button v-if="currentLoggedUser.username == comment.usernameCreator" v-on:click="saveChanges(comment)" :disabled="!comment.isEdited">save changes</button>-->
<!--                </div>-->
<!--                -->
<!--                -->
<!--                <div class="addComment">-->
<!--                    <textarea rows='4' v-model="newComment" ></textarea>-->
<!--                    <button v-on:click="addComment()">Dodaj komentar</button>-->
<!--                </div>-->
<!--        -->
<!--        </div>-->
<!--            -->
<!--	-->
<!--	</div>  -->
	
	
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