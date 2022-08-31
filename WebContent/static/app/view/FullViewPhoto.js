Vue.component("photo-full-view", {
	data: function () {
		    return {
		      photo:null,
              photoComments:null,
              currentLoggedUser:null,
              newComment:''
		    }
	},
	template: ` 
	
	
		
	<div v-if="photo && photoComments && currentLoggedUser" class="searchAllUsersBlock">
	
		<div class="split left">
			<div class="fullViewBox">
                <div>
                    
                    
                    <div v-if="photo.text != 'null'" class="fullViewText">
                        {{photo.text}}
                    </div>
                    <div class="fullViewPhoto">						 
                        <img :src="'/userImages/'+photo.path" alt="post picture" >
                    </div>


                    <div v-if="currentLoggedUser.username == photo.usernameCreate">
                        <button v-on:click="deletePhoto(photo)" >delete photo</button>
                        <button v-on:click="setProfilePicture(photo)" >set as profile picture</button>
                    </div>
                 </div>
		
                 
			
			
		    </div>
		
		</div>
			
		<div class="split right">
            <div>
                <div v-for="comment in this.photoComments" class="photoComment">
                    <textarea rows='4' v-model="comment.content" v-on:change="handleCommentChange(comment)" :readonly="currentLoggedUser.username !== comment.usernameCreator"></textarea>
                    <button v-if="currentLoggedUser.username == comment.usernameCreator" v-on:click="deleteComment(comment)">delete</button>
                    <button v-if="currentLoggedUser.username == comment.usernameCreator" v-on:click="saveChanges(comment)" :disabled="!comment.isEdited">save changes</button>
                </div>
            </div>
            <br>
            <hr>
            <div class="photoComment">
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
                photoId:this.photo.id
            };
            axios.post("/photoComments", JSON.stringify(obj)).then(response => {
                this.$router.go(0);
            });
        },
        deleteComment:function(comment){
            console.log("IZBRISI KOMENTAR");
            console.log(comment.id);
            axios.delete("/photoComments/"+comment.id).then(response =>{
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
                photoId:this.photo.id
            };
            axios.put("/photoComments/"+comment.id, JSON.stringify(obj)).then(result =>{
                this.$router.go(0);
            });
        },

        deletePhoto: function(photo){
            axios.delete("/photo/"+photo.id).then(response =>{
                this.$router.push('/view-profile?user='+photo.usernameCreate);
            });
        },
        setProfilePicture: function(photo){
            console.log("Postavi kao profilnu sliku");
            console.log(photo);
            this.currentLoggedUser.profilePicture = photo.path;
            axios.put("/users", JSON.stringify(this.currentLoggedUser)).then(result => {
                this.$router.push('/view-profile?user='+photo.usernameCreate);
            });
            // this.currentLoggedUser;
        }
		
		
		
	},
	mounted () {
        photoId = this.$route.query.photoId;
        if(photoId == null){
            this.$router.push('/');
        }
        console.log(photoId);
        axios.get('/currentUser').then(response => {
			if(response.data != null){
				this.currentLoggedUser = response.data;
	
			}else{
                this.$router.push("/");
            }
		} )



        axios.get('/photo?id='+photoId).then(response => {
           this.photo = response.data;
        });
        axios.get('/photoComments/findByPhoto?photoId='+photoId).then(response => {
            this.photoComments = response.data;
            console.log(response.data);
        });
    }
});