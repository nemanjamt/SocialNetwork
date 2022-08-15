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


	
	<div v-if="this.user" style="display: flex; margin-bottom:20px">
		<div 
		style="margin-left:5px;">
		<img :src="'/userImages/'+this.user.profilePicture " alt="profile picture" width="100" height="100" style="border-radius:50%">
		</div>
		<div style="
		margin-left:20px;
		">
			<div>
			<b><a href=""> {{this.user.name}} {{this.user.lastName}} </a></b>
			</div>

			<div>
				<font size="2">
					Birth date {{this.user.birthDate | dateFormat('DD.MM.YYYY')}}
					
				</font>
			</div>
			
			<div v-if="this.posts">
				<div v-for="post in this.posts">
				 {{post.postText}}<br>
				 <img :src="'/userImages/'+post.pictureName" alt="post picture" width="400" height="200" >
				 <button>komentari</button>
				</div>
				
			</div>
			<div v-else>
				Korisnik jos uvijek nema postova
			</div>
		</div>
		
		</div>
	</div>
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