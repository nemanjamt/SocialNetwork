
Vue.component("login", {
	data: function () {
		    return {
		      username : '',
			  password : ''
			  
		    }
	},
	template: ` 
	<div class="container">
	<div class="d-flex justify-content-center ">
		<div class="card">
			<div class="card-header">
				<h3>Sign In</h3>
			</div>
			<div class="card-body">
				<form>
					<div class="input-group form-group">
						<div class="input-group-prepend">
							<span class="input-group-text"><i class="fas fa-user"></i></span>
						</div>
						<input type="text" class="form-control" placeholder="username">
						
					</div>
					<div class="input-group form-group">
						<div class="input-group-prepend">
							<span class="input-group-text"><i class="fas fa-key"></i></span>
						</div>
						<input type="password" class="form-control" placeholder="password">
					</div>
					<div class="form-group">
						<input type="submit" value="Login" class="btn float-right login_btn">
					</div>
				</form>
			</div>
			<div class="card-footer">
				<div class="d-flex justify-content-center links">
					Don't have an account?<a href="#">Sign Up</a>
				</div>
			</div>
		</div>
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