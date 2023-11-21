Vue.component("requests-view", {
	data: function () {
		    return {
		      allRequests:[],
              currentLoggedUser:null
		    }
	},
	template: ` 
	
	
		
	<div class="searchAllUsersBlock">
	
		<div v-for="request in allRequests" class="allRequests">
		
		    <div class="reqUserInfo">
                <div class="reqUserPic">
                    <img :src="'/userImages/'+request.user.profilePicture " alt="profile picture" width="60" height="60" style="border-radius:50%; margin-left: 25px">
                </div>
                    
                <!-- info block -->
                <div  class="reqUsersInfo">
                    <b>
                        <a :href="'#/view-profile?user='+request.user.username"> 
                            {{request.user.name}} {{request.user.lastName}} 
                        </a>
                    </b>
                </div>
            </div>
            
            <div class="requestButtons">
                <img v-on:click="acceptRequest(request)" src="/userImages/acceptIcon.png" style="width: 46px; height: 46px;">
                <img v-on:click="declineRequest(request)" src="/userImages/declineIcon.png" style="width: 46px; height: 46px; margin-left: 80px;">
<!--                <button v-on:click="declineRequest(request)">decline request</button>-->
            </div>
        </div>
	
	</div>  
	
	
`
	, 
	methods : {
		init : function() {
		}, 
        acceptRequest:function(request){
            
            let obj = {
                id:request.request.id,
                date:new Date().getTime(),
                state: "ACCEPTED",
                sender:request.user.username,
                receiver:this.currentLoggedUser.username
            }
            
            axios.put("/request", JSON.stringify(obj)).then( result => {
                
                this.$router.go(0);
            });
        }, 
        declineRequest:function(request){
            let obj = {
                id:request.request.id,
                date:new Date().getTime(),
                state: "DENIED",
                sender:request.user.username,
                receiver:this.currentLoggedUser.username
            }
           
            axios.put("/request", JSON.stringify(obj)).then( result => {
                
                this.$router.go(0);
            });
        }
		
		
		
		
	},
	mounted () {
        
        axios.get('/currentUser').then(response => {
			if(response.data != null){
				this.currentLoggedUser = response.data;
                axios.get('/request/'+this.currentLoggedUser.username).then(response =>{
                    
                    let requests = response.data;
                    requests.forEach((item) => {

                        axios.get('/users/'+item.sender).then(response =>{
                            let user = response.data;
                            this.allRequests.push({request:item, user:user});
                        });
                    });
                });
                
	
			}else{
                this.$router.push("/");
            }
		} )

        



        
    }
});