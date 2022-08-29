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
			  postsClicked:false	
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
		
	</div>
	<div v-if="!this.user.privateAccount">
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
							<div @click="viewPost(post)">
							Pogledaj post
							</div>
							<div class="postTxt">
								{{post.postText}}
							</div>
							<div v-if="post.pictureName != 'null'">
								<div class="postPic">						 
									<img :src="'/userImages/'+post.pictureName" alt="post picture" >
								</div>
							</div>
							
							
							<div class="postComment">
								<a>
								<img style="width: 5%; height: 5%" src="https://cdn-icons.flaticon.com/png/512/1944/premium/1944489.png?token=exp=1660684747~hmac=d04ea536bcccf08ec00c633ae10ead40">
								</a>
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
						
							<div @click="viewPhoto(photo)">
								Pogledaj sliku
							</div>
							<div v-if="photo.text != 'null'" class="postTxt">
								{{photo.text}}
							</div>
							<div>
								<div class="postPic">						 
									<img :src="'/userImages/'+photo.path" alt="post picture" >
								</div>
							</div>
							
							
							<div class="postComment">
								<a>
								<img style="width: 5%; height: 5%" src="https://cdn-icons.flaticon.com/png/512/1944/premium/1944489.png?token=exp=1660684747~hmac=d04ea536bcccf08ec00c633ae10ead40">
								</a>
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
			  console.log(this.user);
			  
        	  
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