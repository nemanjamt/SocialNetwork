Vue.component("my-friends", {
	data: function () {
		    return {
		      myFriends:[],
              currentLoggedUser:null
		    }
	},
	template: ` 
	
	
		
	<div class="searchAllUsersBlock">
	
		<div v-for="friend in myFriends" class="allRequests">
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

        axios.get('/currentUser').then(response => {
			if(response.data != null){
				this.currentLoggedUser = response.data;

                axios.get("/friendship/"+this.currentLoggedUser.username).then(response =>{
                    // this.myFriends = response.data;
                    console.log(response.data);

                    let friendships = response.data;
                    friendships.forEach((friendship)=>{
                        if(friendship.firstUser != this.currentLoggedUser.username){
                            axios.get("/users/"+friendship.firstUser).then(result=>{
                                let user = result.data;
                                this.myFriends.push(user);
                            });
                        }else{
                            axios.get("/users/"+friendship.secondUser).then(result=>{
                                let user = result.data;
                                this.myFriends.push(user);
                            });
                        }
                    });
                   
                });
			}else{
                this.$router.push("/");
            }
		} )



        



        
    }
});