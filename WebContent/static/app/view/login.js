
Vue.component("login", {
	data: function () {
		    return {
		      username : '',
			  password : ''
			  
		    }
	},
	template: ` 
	<div class="searchUsersBlock">
		<div class="loginBox">
			<form>
				<input type="text" class="userInput" placeholder="username">
				<br>
				<input class="userInput" type="password" placeholder="password">
				<br>
				<input type="submit" value="Login" class="btn">
			</form>
			
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
		}, 
		login : function(){
			
			
			axios.post('/login', {"username":''+this.username, "password":''+this.password})
			.then(response => { console.log(response.data.jwt);
				console.log(response.data.username);
				console.log(response.data.password); })
			.catch(function (error) {
				$("#errorMessage").css("visibility", "visible");
			  });
		}
		
	},
	mounted () {
        let user = JSON.parse(localStorage.getItem('user'));
		if(user && user.accessToken){
			this.$router.push('/');//preusmjeravam ga za sad na homepage, ako je logovan
		}
    }
});