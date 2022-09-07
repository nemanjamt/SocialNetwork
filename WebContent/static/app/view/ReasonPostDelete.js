Vue.component("reason-post-delete", {
	data: function () {
		    return {
		      post:null,
              reason:'',
              currentLoggedUser:null,
              socket:null
		    }
	},
	template: ` 
	
	
		
	<div v-if="post && currentLoggedUser" class="searchAllUsersBlock">
        <div class="reason">
            <table>
                <tr>
                    <td>
                        <textarea placeholder="input reason for delete" v-on:change="reasonChange" v-model="reason"></textarea><br>
                    </td>
                    <td>
                        <button v-on:click="deletePostByAdmin" class="btn">delete</button>
                    </td>
                </tr>
                <tr colspan="2">
                    <p id="errMess" style="color:red; text-align:center">
                    
                    </p>
                </tr>
                <tr colspan="2">
                    <p id="succMess" style="color:blue; text-align:center">
                   
                    </p>
                </tr>
            </table>
            
            
		</div>
	
	</div>  
	
	
`
	, 
	methods : {
		init : function() {
			console.log("DA?");
		}, 
        reasonChange:function(){
            document.getElementById("errMess").innerHTML = "";
        },
		
        deletePostByAdmin:function(){
            if(this.reason == ""){
                document.getElementById("errMess").innerHTML = "reason must not be empty";
                return;
            }else{
                document.getElementById("errMess").innerHTML = "";
                document.getElementById("errMess").innerHTML = "successful deleted";
            }
            axios.delete("/posts/"+this.post.id).then(response =>{
                this.$router.push('/view-profile?user='+this.post.usernameCreator);
            });
            let obj = {
                sender:this.currentLoggedUser.username,
                receiver:this.post.usernameCreator,
                context:"Vasa objava je obrisana. Razlog: "+this.reason
            };
            
            this.socket.send(JSON.stringify(obj));
        },
        

        
        
		
		
		
	},
	mounted () {
        postId = this.$route.query.postId;
        console.log(postId);
        if(postId == null){
            this.$router.push('/');
        }
        console.log(postId);
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



        axios.get('/post?id='+postId).then(response => {
           this.post = response.data;
        });

        
        
    }
});