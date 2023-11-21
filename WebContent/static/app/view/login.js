
Vue.component("login", {
	data: function () {
		    return {
		      credentials:{username:"", password:""}
		    }
	},
	template: ` 
	<div class="searchUsersBlock">
		<div class="loginBox">
			<table>
				<tr>
				<td>
					<input type="text" class="userInput" name="username"  v-model="credentials.username" placeholder="username" @change="change(this)">
				</td>
				<td>
				<p id="userNameErrMessage" class="errMessage">
					username must not be empty
				</p>
				</td>
				</tr>
				<tr>
				<td>
				<input class="userInput" type="password" name="password"  v-model="credentials.password" placeholder="password">
				</td>
				<td>
				<p id="passwordErrMessage" class="errMessage">
					password must not be empty
				</p>
				</td>
				</tr>
				<tr>
				<td colspan="2">
				<p id="errorMessage" class="errMessage">Wrong username/password</p>
				</td>
				</tr>
				<tr>
				<td colspan="2">
				<button value="Login" class="btn" v-on:click="login()" >login</button>
				</td>
				</tr>
			</table>
			
			<p>
				Don't have an account? <a href="#">Sign Up</a>
			</p>
			
			
		</div>
	
		<div class="splitRight">
			<img style=" height: 60%; margin-left: 20%; margin-top: 15%" src="https://cdn-icons-png.flaticon.com/512/3039/3039635.png">
		</div>
		
	</div>
	
`
	, 
	methods : {
		init : function() {		
			document.getElementById("errorMessage").style.visibility = "hidden";
		}, 
		change:function(e){
			console.log(e);
			console.log("ON CHANGE??");
			console.log(this.credentials.username);
		}
		,
		login : function(){
			console.log("UISAOO?");
			if(this.credentials.username == ""){
				document.getElementById("userNameErrMessage").style.visibility = "visible";
				return;
			}else{
				document.getElementById("userNameErrMessage").style.visibility = "hidden";
				
			}

			if(this.credentials.password == ""){
				document.getElementById("passwordErrMessage").style.visibility = "visible";
				
				return;
			}else{
				document.getElementById("passwordErrMessage").style.visibility = "hidden";
				
			}
			
			axios.post('/login', {"username":this.credentials.username, "password":this.credentials.password})
			.then(response => { 
				
				this.$router.push('/');
				
			})
			.catch(function (error) {
				document.getElementById("errorMessage").style.visibility = "visible";
				// $("#errorMessage").css("visibility", "visible");
			  });
			 
		}
		
	},
	mounted () {
        
		axios.get('/currentUser').then(response => {
			if(response.data != null){
				this.$router.push('/');//preusmjeravam ga za sad na homepage, ako je logovan
			}
		} ).catch(error => {
			console.log("da");
		}
		);
    }
});