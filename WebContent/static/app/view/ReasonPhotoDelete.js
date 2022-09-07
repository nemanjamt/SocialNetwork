Vue.component("reason-photo-delete", {
	data: function () {
		    return {
		      photo:null,
              reason:'',
              currentLoggedUser:null,
              socket:null
		    }
	},
	template: ` 
	
	
		
	<div v-if="photo && currentLoggedUser" class="searchAllUsersBlock">
        <div class="reason">
            <textarea v-model="reason"></textarea><br>
            <button v-on:click="deletePhotoByAdmin">delete</button>
		</div>
	
	</div>  
	
	
`
	, 
	methods : {
		init : function() {
			console.log("DA?");
		}, 
		
        deletePhotoByAdmin:function(){
            console.log("BRISANJE BY ADMIN");
            console.log(this.reason);
            console.log(this.photo);
            axios.delete("/photo/"+this.photo.id).then(response =>{
                this.$router.push('/view-profile?user='+this.photo.usernameCreate);
            });
            let obj = {
                sender:this.currentLoggedUser.username,
                receiver:this.photo.usernameCreate,
                context:"Vasa slika je obrisana. Razlog: "+this.reason
            };
            
            this.socket.send(JSON.stringify(obj));
        },
        

        
        
		
		
		
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
                if(this.currentLoggedUser.role != 'ADMIN'){
                    this.$router.push("/");
                }
                this.host="ws://localhost:9090/ws?userId="+this.currentLoggedUser.username;

                this.socket = new WebSocket(this.host);
                this.socket.onopen = function() {
                    console.log("socket open");
                }


                this.socket.onclose = function() {
                    console.log("socket close");
                    this.socket = null;
                }
			}else{
                this.$router.push("/");
            }
		} )



        axios.get('/photo?id='+photoId).then(response => {
           this.photo = response.data;
        });

        
        
    }
});