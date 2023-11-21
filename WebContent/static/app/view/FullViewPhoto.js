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
	
	
		
	<div v-if="photo && photoComments && currentLoggedUser" class="postBlock">
	
		<div >
			<div class="photoSection">
                <div class="picSection">
                    
                    
                    <div v-if="photo.text != 'null'" class="fullViewText">
                        <textarea :value="photo.text"></textarea>    
                    </div>
                    <div class="fullViewPhoto">						 
                        <img :src="'/userImages/'+photo.path" alt="post picture" readonly>
                    </div>
                    <div v-if="currentLoggedUser.username == photo.usernameCreate" class="fullViewButtons">
                        <table>
                            <tr>
                                <td>
                                    <button class="btn" v-on:click="deletePhoto(photo)" >delete photo</button>
                                </td>
                                <td>
                                    <button class="btn" v-on:click="setProfilePicture(photo)">set as profile picture</button>
                                </td>
                            </tr>
                        </table>
                        
                        
                    </div>
                    <div v-else-if="currentLoggedUser.role == 'ADMIN'">
                        <button class="btn" v-on:click="deletePhotoByAdmin(photo)" >delete photo</button>
                    </div>
                 </div>
		
                 
			
			
		    </div>
		
		</div>
			
		<div class="split right" style="margin-top: -850px;">
            <div class="sortUsersBlock addComment">
                <table>
                    <tr>
                        <td>
                            <textarea placeholder="Add a comment..." class="inputComment" v-model="newComment" ></textarea>
                        </td>
                        <td>
                            <button v-on:click="addComment()" class="btn">Dodaj komentar</button>
                        </td>
                    </tr>
                </table>
                
                
            </div>
            <div class="showUsersBlock" style="margin-top: 65px;">
                <div v-for="comment in this.photoComments" class="commentBlock" >
                    <div class="profilePic">
                        <img :src="'/userImages/'+comment.user.profilePicture " alt="profile picture" width="20" height="20" style="border-radius:50%">
                        <b style="font-size: 12px">
                            <a :href="'#/view-profile?user='+comment.user.username"> {{comment.user.name}} {{comment.user.lastName}} </a>
                        </b>
                    </div>
                    
                    
                    <div class="commentBtns">
                        <img v-if="currentLoggedUser.username == comment.comment.usernameCreator" v-on:click="deleteComment(comment.comment)" src="/userImages/deleteIcon.png" style="width: 24px; height: 24px; float: right;">
                        <img v-if="currentLoggedUser.username == comment.comment.usernameCreator" v-on:click="saveChanges(comment.comment)" :disabled="!comment.comment.isEdited" src="/userImages/saveIcon.png" style="width: 24px; height: 24px; margin-right: 24px;  float: right">
                    </div>
                    <div class="commentPart">
                        <textarea class="commentContent" v-model="comment.comment.content" v-on:change="handleCommentChange(comment.comment)" :readonly="currentLoggedUser.username !== comment.comment.usernameCreator"></textarea>
                    </div>

                    <div v-if="!comment.comment.edited" class="datePart"> 
                        <p style="color: #757576"> date: {{comment.comment.publishedDate | dateFormat('DD.MM.YYYY')}} </p>
                    </div>
                    <div v-else class="datePart"> 
                        <p style="color: #757576"> edit date: {{comment.comment.editDate | dateFormat('DD.MM.YYYY')}} </p>
                    </div>
                    

                </div>
            </div>
            <br>
            <hr>
            

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

        deletePhotoByAdmin:function(photo){
            
            this.$router.push("/reason-photo-delete?photoId="+photo.id);
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
            let comments = response.data;
            this.photoComments = [];
            comments.forEach(comment =>{
                let obj = {};
                obj.comment = comment;
                axios.get("/users/"+comment.usernameCreator).then(response =>{
                    obj.user = response.data;
                    this.photoComments.push(obj);
                })
                
            })
            // this.photoComments = response.data;
            console.log(response.data);
            console.log(this.photoComments);
        });
    },
    filters: {
    	dateFormat: function (value, format) {
            console.log(value.date.day);
            console.log(value.date.month);
            console.log(value.date.year);
			convertedValue = new Date(value.date.year, value.date.month-1, value.date.day);
    		var parsed = moment(convertedValue);
    		return parsed.format(format);
    	}
   	}
});