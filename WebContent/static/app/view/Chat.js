Vue.component("chat", {
	data: function () {
		    return {
		      currentLoggedUser:null,
              choosedUser:null,
              myFriends:[],
			  messages:[],
              host:"",
              socket:null,
              newMessage:""
		    }
	},
	template: ` 

		
	<div v-if="currentLoggedUser" class="searchAllUsersBlock">
	
		<div class="split left">
		
            <div v-for="friend in myFriends" >
                <div v-if="friend.user.role != 'ADMIN'" class="allFriendChat">
                    <div class="searchedUserPic" >
                        <img :src="'/userImages/'+friend.user.profilePicture " alt="profile picture" width="60" height="60" style="border-radius:50%; margin-right:15px">
                    </div>
                        
                    <!-- info block -->
                    <div v-on:click="chooseUser(friend)" >
                        <b>
                            <a> 
                                {{friend.user.name}} {{friend.user.lastName}} 
                            </a>
                        </b> 
                        <b v-if="friend.messageArrived" class="newMessage">new message!</b>    
                    </div>
                </div>
                <div v-else class="adminChat">
                    <div class="searchedUserPic" >
                    <img :src="'/userImages/'+friend.user.profilePicture " alt="profile picture" width="60" height="60" style="border-radius:50%; margin-right:15px">
                    </div>
                        
                    <!-- info block -->
                    <div v-on:click="chooseUser(friend)" >
                        <b>
                            <a> 
                                {{friend.user.name}} {{friend.user.lastName}} 
                            </a>
                        </b> 
                        <b v-if="friend.messageArrived" class="newMessage">new message!</b>    
                    </div>
                </div>
            
        </div>
		</div>
			
		<div v-if="choosedUser" class="split right">
            <div class="allMessages">
                <div v-for="message in messages" class="chat-messages">
                    <div v-if="message.receiverUsername == currentLoggedUser.username && choosedUser.role == 'ADMIN'" class="message-box-holder">
                        <div class="message-partner" style="background-color:red">
                            {{message.context}}
                        </div>
                    </div>
                    <div v-else-if="message.receiverUsername == currentLoggedUser.username" class="message-box-holder">
                        <div class="message-partner">
                            {{message.context}}
                        </div>
                    </div>
                    <div v-else class="message-box-holder">
                        <div  class="message-box">
                            {{message.context}}
                        </div>
                    </div>
                </div>
            </div>
            <div v-if="choosedUser.role != 'ADMIN'" class="chat-input-holder">
                <textarea class="chat-input" v-model="newMessage"></textarea>
                <input v-on:click="send" type="submit" value="Send" class="message-send" />
            </div>
		</div>
	
	</div>  
	
	
`
	, 
	methods : {
		init : function() {
			console.log("DA?");
		},
        chooseUser:function(friend){
            friend.messageArrived = false;
            this.choosedUser = friend.user;
            this.messages = [];
            axios.get("message?receiver="+this.currentLoggedUser.username+"&sender="+this.choosedUser.username).then(result=>{
                this.messages = result.data;
                console.log(this.messages);
            });
        },

        send:function(){
            console.log(this.newMessage);
            console.log("kreiraj poruku i dodaj je u listu");
            let obj = {
                sender:this.currentLoggedUser.username,
                receiver:this.choosedUser.username,
                context:this.newMessage
            };
            this.messages.push(obj);
            console.log(obj);
            this.socket.send(JSON.stringify(obj));
            this.newMessage ="";
        },
		
		change:function(e){
			console.log(e);
			console.log("ON CHANGE??");
			console.log(this.credentials.username);
		}

		
	},
	mounted () {
        let messageUser  = this.$route.query.messageTo;
        axios.get('/currentUser').then(response => {
			if(response.data != null){
				this.currentLoggedUser = response.data;
                axios.get("/friendship/"+this.currentLoggedUser.username).then(response =>{
                    // this.myFriends = response.data;
                   

                    let friendships = response.data;
                    friendships.forEach((friendship)=>{
                        if(friendship.firstUser != this.currentLoggedUser.username){
                            axios.get("/users/"+friendship.firstUser).then(result=>{
                                let user = result.data;
                                
                                userChat = {
                                    user:user,
                                    messageArrived:false
                                }
                                if(messageUser){
                                    if(messageUser == user.username){
                                        this.chooseUser(userChat);
                                    }
                                }
                                this.myFriends.push(userChat);
                            });
                        }else{
                            axios.get("/users/"+friendship.secondUser).then(result=>{
                                let user = result.data;
                                userChat = {
                                    user:user,
                                    messageArrived:false
                                }
                                if(messageUser){
                                    if(messageUser == user.username){
                                        this.chooseUser(userChat);
                                    }
                                }
                                this.myFriends.push(userChat);
                            });
                        }
                    });

                    axios.get("/users/admin").then(result => {
                        let user = result.data;
                        userChat = {
                            user:user,
                            messageArrived:false
                        }
                        this.myFriends.push(userChat);
                    })
                   
                });

                this.host="ws://localhost:9090/ws?userId="+this.currentLoggedUser.username;

                this.socket = new WebSocket(this.host);
                this.socket.onopen = function() {
                    console.log("socket open");
                }
                let self = this;
                this.socket.onmessage = function(msg) {
                    let message = JSON.parse(msg.data);
                    console.log(message);
                    console.log(self.choosedUser);
                    if(self.choosedUser){
                        if(self.choosedUser.username == message.senderUsername){
                            console.log("JE LI USLO?");
                            console.log(self.messages);
                            self.messages.push(message);
    
                        }else {
                        
                            self.myFriends.forEach((friend) =>{
                                console.log('___________________');
                                console.log(friend.user);
                                console.log(message.senderUsername);
                                if(friend.user.username == message.senderUsername){
                                    friend.messageArrived = true;
                                    return;
                                }
                            });
                        }
                    }else {
                        
                        self.myFriends.forEach((friend) =>{
                            console.log('___________________');
                            console.log(friend.user);
                            console.log(message.senderUsername);
                            if(friend.user.username == message.senderUsername){
                                friend.messageArrived = true;
                                return;
                            }
                        });
                    }
                    
                   
                }

                this.socket.onclose = function() {
                    console.log("socket close");
                    this.socket = null;
                }
			}
		} )
    }
});