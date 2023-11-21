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
	
	
<div  v-if="post && postComments && currentLoggedUser" class="postBlock">

    <div class="photoSection">
        <div class="picSection">

            <div>
                <div  class="fullViewText">
                    <textarea :value="post.postText" readonly></textarea>
                </div>
                <div v-if="post.pictureName != 'null' && post.pictureName != undefined" class="fullViewPhoto">						 
                    <img :src="'/userImages/'+post.pictureName" alt="post picture" >
                </div>
    
    
                <div v-if="currentLoggedUser.username == post.usernameCreator">
                    <button class="btn" v-on:click="deletePost(post)" > Delete post </button>
                </div>
                <div v-else-if="currentLoggedUser.role == 'ADMIN'">
                        <button class="btn" v-on:click="deletePostByAdmin" >delete post</button>
                </div>
             </div>



        </div>
    
    </div>
	
	<div class="split right" style="margin-top: -850px;">
	    
	    <div class="sortUsersBlock addComment"> 
            <table>
                <tr>
                    <td>
                        <textarea v-model="newComment" class="inputComment" placeholder="Add a comment..."> </textarea>
                    </td>
                    <td>
                        <button v-on:click="addComment()" class="btn"> Post </button>
                    </td>
                </tr>
            </table>
	        
           
	    </div>
	    
	    <div class="showUsersBlock" style="margin-top: 65px;"> 
	         <div v-for="comment in this.postComments" class="commentBlock">
                <div class="profilePic">
                        <img :src="'/userImages/'+comment.user.profilePicture " alt="profile picture" width="20" height="20" style="border-radius:50%">
                        <b style="font-size: 12px">
                            <a :href="'#/view-profile?user='+comment.user.username"> {{comment.user.name}} {{comment.user.lastName}} </a>
                        </b>
                </div>

	            <div class="commentBtns"> 
<!--                    <button v-if="currentLoggedUser.username == comment.comment.usernameCreator" v-on:click="deleteComment(comment.comment)"> Delete </button>-->
<!--                    <button v-if="currentLoggedUser.username == comment.comment.usernameCreator" v-on:click="saveChanges(comment.comment)" :disabled="!comment.comment.isEdited"> Save changes </button>-->
                    <img v-if="currentLoggedUser.username == comment.comment.usernameCreator" v-on:click="deleteComment(comment.comment)" src="/userImages/deleteIcon.png" style="width: 24px; height: 24px; float: right;">
                    <img v- if="currentLoggedUser.username == comment.comment.usernameCreator" v-on:click="saveChanges(comment.comment)" :disabled="!comment.comment.isEdited" src="/userImages/saveIcon.png" style="width: 24px; height: 24px; margin-right: 24px;  float: right">
	            </div>
	            
                <div class="commentPart"> 
                    <textarea v-model="comment.comment.content" v-on:change="handleCommentChange(comment.comment)" :readonly="currentLoggedUser.username !== comment.comment.usernameCreator" class="commentContent"></textarea>
                </div>
                
                <div v-if="!comment.comment.edited" class="datePart"> 
                    <p style="color: #757576"> date: {{comment.comment.publishedDate | dateFormat('DD.MM.YYYY')}} </p>
                </div>
                <div v-else class="datePart"> 
                    <p style="color: #757576"> edit date: {{comment.comment.editDate | dateFormat('DD.MM.YYYY')}} </p>
                </div>
	         
	         
	         
                </div>
	    </div>
	
    </div>
	
	
</div>
	
		

	
	
`
	, 
	methods : {
		init : function() {
			console.log("DA?");
		}, 
        deletePostByAdmin:function(){
            console.log("delete post?");
            console.log(this.post.id);
            this.$router.push("/reason-post-delete?postId="+this.post.id);
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
            console.log(response.data);


            let comments = response.data;
            this.postComments = [];
            comments.forEach(comment =>{
                let obj = {};
                obj.comment = comment;
                axios.get("/users/"+comment.usernameCreator).then(response =>{
                    obj.user = response.data;
                    this.postComments.push(obj);
                })
                
            })
            // this.photoComments = response.data;
            console.log(response.data);
            console.log(this.postComments);
        });
    },
    filters: {
    	dateFormat: function (value, format) {
			convertedValue = new Date(value);
    		var parsed = moment(convertedValue);
    		return parsed.format(format);
    	}
   	}
});