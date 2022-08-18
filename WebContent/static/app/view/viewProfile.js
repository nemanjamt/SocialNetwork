/*
komponenta za prikaz profila
 */
Vue.component("view-profile", {
	data: function () {
		    return {
		      user: null,
			  posts:null	
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
							<img style="width: 18px; height: 18px" src="https://cdn-icons.flaticon.com/png/512/657/premium/657570.png?token=exp=1660672671~hmac=7051437c60b7d215c8eda80a2ec13221">
							{{this.user.birthDate | dateFormat('DD.MM.YYYY')}} 
						</p>
						
						<br>
						
						<p>
							Friends: {{this.user.friends.length}}
						</p>
				</div>
		
	</div>
	
<!--  fixed photos/post line	-->
	<div class="postsPhotosLine">
	
	<button class="btn" style="margin-right: 12%"> Photos </button>
	<button class="btn"> Posts </button>
	
	<hr>
	
	</div>
			
<!-- posts-->
		<div class="userPostsBlock">
				<div v-if="this.posts">
				
					<div v-for="post in this.posts" class="userPost">
					
						<div class="postTxt">
							{{post.postText}}
						</div>
						
						<div class="postPic">						 
						 	<img :src="'/userImages/'+post.pictureName" alt="post picture" >
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

	
<!--	user does not exist-->
	<div v-else>Korisnik ne postoji</div>
	  
`
	, 
	methods : {
		init : function() {
			this.users = {};
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
			  
			  
        	  
          });
		  axios
          .get('/posts?user='+userId)
          .then(response => {
			
        	  this.posts = response.data;
			
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