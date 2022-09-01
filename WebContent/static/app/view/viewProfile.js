/*
komponenta za prikaz profila
 */
Vue.component("view-profile", {
	data: function () {
		    return {
				
		      user: null,
			  posts:null, 
			  photos:null,
			  photosClicked:false,
			  postsClicked:false,
			  currentLoggedUser:null,
			  isFriend:false,
			  isSender:false,
			  isReceiver:false
		    }
	},
	template: ` 
	
	<div v-if="this.user" class="userProfilBlock">
	
	<div class="userProfilInfo">
		<!--	pic -->
				<div style="float: left; width: 15%; margin-left: 5%; margin-top: 3%">
					<img :src="'/userImages/'+this.user.profilePicture " alt="profile picture" width="100" height="100" style="border-radius:50%">
				</div>
				
		<!--		user info -->
				<div class="userInfo">
						<b style="font-size: 24px">
							<a href=""> {{this.user.name}} {{this.user.lastName}} </a>
						</b>
						
						<br>

						<p>  
							{{this.user.birthDate | dateFormat('DD.MM.YYYY')}} 
						</p>
						
						<br>
				</div>
				<div v-if="currentLoggedUser">
					<div v-if="currentLoggedUser.username !== user.username">
					
						<button v-if="!isFriend && !isSender && !isReceiver" v-on:click="addFriend()">add friend</button>
						<button v-if="isFriend" v-on:click="removeFriendship()">remove friend</button>
						<div v-if="isReceiver">
							<button v-on:click="acceptFriendship()">accept request</button>
							<button v-on:click="declineRequest()">remove request</button>
						</div>
						<button v-if="isSender" v-on:click="removeRequest()">delete request</button>
						<div  v-if="!this.user.privateAccount || isFriend">
						<a @click="mutualFriends()">mutual friends</a>
						</div>
					</div>
					
				</div>

				<div v-if="currentLoggedUser">
					<div v-if="currentLoggedUser.username == user.username">
					<a @click="myFriends()">friends list</a>
					</div>
				</div>
				
		
	</div>
	<div v-if="!this.user.privateAccount || isFriend || currentLoggedUser.username == user.username">
<!--  fixed photos/post line	-->
	<div class="postsPhotosLine">
	
	<button class="btn" style="margin-right: 12%" v-on:click="onPhotosClicked()"> Photos </button>
	<button class="btn" v-on:click="onPostsClicked()"> Posts </button>
	
	<hr>
	
	</div>
			
<!-- posts-->
	<div v-if="postsClicked">
		<div class="userPostsBlock">
				<div v-if="this.posts">
				
					<div v-for="post in this.posts" class="userPost">
						
						<div @click="viewFullPost(post)" style="text-align: center;">

							<img src="/userImages/resize.png" style="float: right; width: 16px; height: 16px">
		
						</div>
						
						<div class="postTxt">
							{{post.postText}}
						</div>
						
						<hr>
						
						<div v-if="post.pictureName != 'null'">
							<div class="postPic">						 
								<img :src="'/userImages/'+post.pictureName" alt="post picture" >
							</div>
						</div>
					</div>
				</div>
				
				<div v-else>
				
					Korisnik jos uvijek nema postova <br>
				</div>
				
		</div>
	
	</div>

	<div v-else-if="photosClicked">
			<div class="userPostsBlock">
					<div v-if="this.photos">
					
						<div v-for="photo in this.photos" class="userPost">
						
							<div v-if="currentLoggedUser" @click="viewFullPhoto(photo)" style="text-align: center;">
							
								<img src="/userImages/resize.png" style="float: right; width: 16px; height: 16px">
							</div>
							<div v-if="photo.text != 'null'" class="postTxt">
								{{photo.text}}
							</div>
							<div>
								<div class="postPic">						 
									<img :src="'/userImages/'+photo.path" alt="post picture" >
								</div>
							</div>
							
							
							
							
						</div>

						
						
					</div>
					
					<div v-else>
					
						Korisnik jos uvijek nema postova <br>
					</div>
					
			</div>
		</div>
	
	</div>
</div>

	
<!--	user does not exist-->
	<div v-else>Korisnik ne postoji</div>
	  
`
	, 
	methods : {
		init : function() {
			this.users = {};
		},
		onPhotosClicked: function(){
			this.photosClicked = true;
			this.postsClicked = false;
		},
		onPostsClicked: function(){
			this.photosClicked = false;
			this.postsClicked = true;
		},
		viewPost: function(post){
			console.log("implementiraj gledanje postova");
			console.log(post);
		},
		viewPhoto: function(photo){
			console.log("implementiraj gledanje slika");
			console.log(photo);
		}
		,viewFullPhoto:function(photo){
			this.$router.push('/photo-full-view?photoId='+photo.id);
		},
		viewFullPost:function(post){
			this.$router.push('/post-full-view?postId='+post.id);
		},
		mutualFriends:function(){
			this.$router.push("/mutual-friends?user="+this.user.username);
		},
		myFriends:function(){
			this.$router.push("/my-friends");
		},
		addFriend:function(){
			console.log("POSALJI ZAHTJEV");
			let obj = {
				date:new Date().getTime(),
				state: "WAITING",
				sender:this.currentLoggedUser.username,
				receiver:this.user.username
			}
			console.log(obj);
			axios.post("/request", JSON.stringify(obj)).then( result => {
				
				this.$router.go(0);
				
			});
		},
		acceptFriendship:function(){
			
			axios.get('request/getBetweenUsers?firstUser='+this.currentLoggedUser.username +"&secondUser="+this.user.username).then(response => {
				console.log("SRBIJA  "+ response.data);
				console.log(response.data);
				if(response.data != null){
					console.log("PRIHVATI ZAHTJEV");
					let obj = {
						id:response.data.id,
						date:new Date().getTime(),
						state: "ACCEPTED",
						sender:this.currentLoggedUser.username,
						receiver:this.user.username
					}
					console.log(obj);
					axios.put("/request", JSON.stringify(obj)).then( result => {
						console.log(result.data);
						console.log("KRAJINA");
						this.$router.go(0);
					});
				}
			});
		},
		removeRequest:function(){


			axios.get('request/getBetweenUsers?firstUser='+this.currentLoggedUser.username +"&secondUser="+this.user.username).then(response => {
				console.log("SRBIJA  "+ response.data);
				console.log(response.data);
				if(response.data != null){
					console.log("IZBRISI ZAHTJEV");
					let obj = {
						id:response.data.id,
						date:new Date().getTime(),
						state: "DENIED",
						sender:this.currentLoggedUser.username,
						receiver:this.user.username
					}
					console.log(obj);
					axios.put("/request", JSON.stringify(obj)).then( result => {
						console.log(result.data);
						console.log("KRAJINA");
						this.$router.go(0);
					});
				}
			});
			
		},
		removeFriendship:function(){

			axios.get('friendship/getBetweenUsers?firstUser='+this.currentLoggedUser.username +"&secondUser="+this.user.username).then(response => {
				console.log("SRBIJA  "+ response.data);
				console.log(response.data);
				if(response.data != null){
					console.log("IZBRISI PRIJATELJSTVO");
					
					console.log(response.data);
					axios.delete("/friendship/"+response.data.id).then( result => {
						console.log("KRAJINA!!");
						this.$router.go(0);
					});
				}
			});
		},
		declineRequest:function(){
			
			axios.get('request/getBetweenUsers?firstUser='+this.currentLoggedUser.username +"&secondUser="+this.user.username).then(response => {
				console.log("SRBIJA  "+ response.data);
				console.log(response.data);
				if(response.data != null){
					console.log("ODBIJ ZAHTJEV");
					let obj = {
						id:response.data.id,
						date:new Date().getTime(),
						state: "DENIED",
						sender:this.currentLoggedUser.username,
						receiver:this.user.username
					}
					console.log(obj);
					axios.put("/request", JSON.stringify(obj)).then( result => {
						console.log(result.data);
						console.log("KRAJINA");
						this.$router.go(0);
					});
				}
			});
		}
	},
	

	mounted () {
		userId = this.$route.query.user

		if(userId === undefined)
			return;//ukoliko se gadja stranica bez da se proslijedi id korisnika
		console.log("PROSAO?")
        axios
          .get('/users/'+userId)
          .then(response => {
			
        	  this.user = response.data;
			  axios.get('/currentUser').then(response => {
				if(response.data != null){
					this.currentLoggedUser = response.data;
					if(this.currentLoggedUser && this.user){
						axios.get('friendship/checkFriendship?firstUser='+this.currentLoggedUser.username +"&secondUser="+this.user.username).then(response => {
							this.isFriend = response.data;
						});
		
						axios.get('request/getBetweenUsers?firstUser='+this.currentLoggedUser.username +"&secondUser="+this.user.username).then(response => {
							console.log("SRBIJA  "+ response.data);
							console.log(response.data);
							if(response.data != null){
								if(response.data.receiver == this.currentLoggedUser.username){
									this.isReceiver=true;
								}else{
									this.isSender = true;
								}
							}
						});
					}
					
				}
			} )
			  
        	  
          });

		  axios
          .get('/posts?user='+userId)
          .then(response => {
			
        	  this.posts = response.data;
				console.log(this.posts);
          });

		  axios
          .get('/photo/getByUser?username='+userId)
          .then(response => {
        	  this.photos = response.data;
			  console.log(this.photos);
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