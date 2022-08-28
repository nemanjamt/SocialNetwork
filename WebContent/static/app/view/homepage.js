Vue.component("home-page", {
	data: function () {
		    return {
		      userLogedIn:false
		    }
	},
	template: ` 
	<div class="searchAllUsersBlock" v-if="this.userLogedIn">
			<div  v-if="this.userLogedIn">
			KORISNIK JE ULOGOVAN
			<button v-on:click="logout()">Logout</button>
			</div>
			<div v-else>
			KORISNIK NIJE ULOGOVAN
			</div>
			
			
	</div>	
	
		
	<div v-else class="searchAllUsersBlock">
	
		<div class="split left">
			<div class="loginBox">
				<table>
					<tr>
						<td>
							<input type="text" class="userInput" name="username"  placeholder="username" @change="change(this)">
						</td>
						<td>
							<p id="userNameErrMessage" class="errMessage">
								username must not be empty
							</p>
						</td>
					</tr>
					
					<tr>
						<td>
							<input class="userInput" type="password" name="password" placeholder="password">
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
							<button value="Login" class="btn" >login</button>
						</td>
					</tr>
				</table>
		
			<p>
				Don't have an account? <a href="#">Sign Up</a>
			</p>
			
		</div>
		
		</div>
			
		<div class="split right">
			<table class="registrationTable">
				<tr>
					<td><input class="userInput" type="text" id="name" placeholder="name"></td>
					<td><p id="nameErrMessage" style="color:red"></p></td>
				</tr>
				
				<tr>
					<td><input class="userInput" type="text" id="lastName" placeholder="last name"></td>
					<td><p id="lastNameErrMessage" style="color:red"></p></td>
				</tr>

				<tr>
					<td><input class="userInput" type="text" id="username" placeholder="username"></td>
					<td><p id="usernameErrMessage" style="color:red"></p></td>
				</tr>

				<tr>
					<td><input class="userInput" type="text" id="email" placeholder="email"></td>
					<td>
						<p id="emailErrMessage" style="color:red"></p>
					</td>
				</tr>

				<tr>
					<td>
						<select class="userInput" id="gender" placeholder="gender" >
							<option value="" disabled selected hidden>select your gender</option>
							<option value="MALE">Male</option>
							<option value="FEMALE">Female</option>
						</select>
					</td>
					<td>
						<p id="genderErrMessage" style="color:red"></p>
					</td>
				</tr>
				
				<tr>
					<td>
						<input style="float:right" type="date" placeholder="birth date" id="birthDate">
					</td>
					<td>
						<p id="birthDateErrMessage" style="color:red"></p>
					</td>
				</tr>
				
				<tr>
					<td><input class="userInput" type="password" id="password" placeholder="password"></td>
					<td><p id="passwordErrMessage" style="color:red"></p></td>
				</tr>
				
				<tr>
					<td><input class="userInput" type="password" id="confirmPassword" placeholder="confirm password"></td>
					<td><p id="confirmPasswordErrMessage" style="color:red"></p></td>
				</tr>
				
				<tr>
					<button class="btn">registrate</button>
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
		logout: function(){
			axios.put("/logout").then(response => {
				this.$router.push('/login');
			}
			);
		}
		
	},
	mounted () {
        axios.get('/currentUser').then(response => {
			if(response.data != null){
				this.userLogedIn = true;
	
			}
		} )
    }
});