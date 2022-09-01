Vue.component("mutual-friends", {
	data: function () {
		    return {
		      mutualFriends:[],
              currentLoggedUser:null
		    }
	},
	template: ` 
	
	
		
	<div class="searchAllUsersBlock">
	
		<div v-for="friend in mutualFriends" class="allRequests">
            <div class="searchedUserPic">
				<img :src="'/userImages/'+friend.profilePicture " alt="profile picture" width="60" height="60" style="border-radius:50%">
			</div>
				
            <!-- info block -->
            <div  class="searchUsersInfo">
                <b>
                    <a :href="'#/view-profile?user='+friend.username"> 
                        {{friend.name}} {{friend.lastName}} 
                    </a>
                </b>     
            </div>
            
        </div>
	
	</div>  
	
	
`
	, 
	methods : {
		init : function() {
		}, 
        
		
		
		
		
	},
	mounted () {
        userId = this.$route.query.user
        console.log("USLO?");
		if(userId === undefined)
			return;//ukoliko se gadja stranica bez da se proslijedi id korisnika

        axios.get('/currentUser').then(response => {
			if(response.data != null){
				this.currentLoggedUser = response.data;
                axios.get("/mutualFriends?firstUser="+this.currentLoggedUser.username+"&secondUser="+userId).then(response =>{
                    this.mutualFriends = response.data;
                    console.log(response.data);
                });
			}else{
                this.$router.push("/");
            }
		} )



        



        
    }
});