Vue.component("home-page", {
	data: function () {
		    return {
		      userLogedIn:false,
			  fields:{name:"", lastName:"", username:"", email:"", password:"", confirmPassword:"", birthDate:"", gender:""},
			  credentials:{username:"", password:""}
		    }
	},
	template: ` 
	<div class="searchAllUsersBlock" v-if="this.userLogedIn">
			<div><img src="/userImages/welcome.jpg" style="height: 100%; width: 100%;"></div>		
	</div>	
	
		
	<div v-else class="searchAllUsersBlock">
	
		<div class="split left">
			<div class="loginBox">
				<table>
					<tr>
						<td>
							<input type="text" class="userInput" name="username"  placeholder="username" @change="change(this)" v-model="credentials.username">
						</td>
						<td>
							<p id="loginUserNameErrMessage" class="errMessage">
								username must not be empty
							</p>
						</td>
					</tr>
					
					<tr>
						<td>
							<input class="userInput" type="password" name="password" placeholder="password" v-model="credentials.password">
						</td>
						
						<td>
							<p id="loginPasswordErrMessage" class="errMessage">
								password must not be empty
							</p>
						</td>
					</tr>
					
					<tr>
						<td colspan="2">
							<p id="loginErrorMessage" class="errMessage">Wrong username/password</p>
						</td>
					</tr>
					
					<tr>
						<td colspan="2">
							<button value="Login" class="btn" v-on:click="login()" >login</button>
						</td>
					</tr>
				</table>
		
			
			
		</div>
		
		</div>
			
		<div class="split right">
			<p>
				Don't have an account? Sign up below
			</p>
			<table class="registrationTable">
				<tr>
					<td><input class="userInput" type="text" id="name" placeholder="name" v-model="fields.name"></td>
					<td><p id="nameErrMessage" style="color:red"></p></td>
				</tr>
				
				<tr>
					<td><input class="userInput" type="text" id="lastName" placeholder="last name" v-model="fields.lastName"></td>
					<td><p id="lastNameErrMessage" style="color:red"></p></td>
				</tr>

				<tr>
					<td><input class="userInput" type="text" id="username" placeholder="username" v-model="fields.username"></td>
					<td><p id="usernameErrMessage" style="color:red"></p></td>
				</tr>

				<tr>
					<td><input class="userInput" type="text" id="email" placeholder="email" v-model="fields.email"></td>
					<td>
						<p id="emailErrMessage" style="color:red"></p>
					</td>
				</tr>

				<tr>
					<td>
						<select class="userInput" id="gender" placeholder="gender" v-model="fields.gender" >
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
						<input style="float:right" type="date" placeholder="birth date" id="birthDate" v-model="fields.birthDate">
					</td>
					<td>
						<p id="birthDateErrMessage" style="color:red"></p>
					</td>
				</tr>
				
				<tr>
					<td><input class="userInput" type="password" id="password" placeholder="password" v-model="fields.password"></td>
					<td><p id="passwordErrMessage" style="color:red"></p></td>
				</tr>
				
				<tr>
					<td><input class="userInput" type="password" id="confirmPassword" placeholder="confirm password" v-model="fields.confirmPassword"></td>
					<td><p id="confirmPasswordErrMessage" style="color:red"></p></td>
				</tr>
				
				<tr>
					
					<button class="btn" v-on:click="registration()">registrate</button>
				</tr>
				<tr>
					<td><p id="registrationErrMessage" class="errMessage">Fail.Try another username.</p></td>
					
				</tr>
				<tr>
					<td><p id="registrationSuccessMessage"  class="successMessage" >Registration successfully done</p></td>
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
		login : function(){
			console.log("UISAOO?");
			if(this.credentials.username == ""){
				document.getElementById("loginUserNameErrMessage").style.visibility = "visible";
				return;
			}else{
				document.getElementById("loginUserNameErrMessage").style.visibility = "hidden";
				
			}

			if(this.credentials.password == ""){
				document.getElementById("loginPasswordErrMessage").style.visibility = "visible";
				
				return;
			}else{
				document.getElementById("loginPasswordErrMessage").style.visibility = "hidden";
				
			}
			
			axios.post('/login', {"username":this.credentials.username, "password":this.credentials.password})
			.then(response => { 
				document.getElementById("loginErrorMessage").style.visibility = "hidden";
				// this.$router.push('/');
				this.$router.go(0);
				
			})
			.catch(function (error) {
				document.getElementById("loginErrorMessage").style.visibility = "visible";
				// $("#errorMessage").css("visibility", "visible");
			  });
			 
		},
		checkValidation: function(){
            console.log(this.fields);
            if(this.fields.name == ""){
                document.getElementById("nameErrMessage").innerHTML = "name must not be empty";
                return false;
            }else if(this.fields.name.length < 2){
                document.getElementById("nameErrMessage").innerHTML = "minimal length of name is 2";
                return false;
            }else{
                document.getElementById("nameErrMessage").innerHTML = "";
            }

            if(this.fields.lastName == ""){
                document.getElementById("lastNameErrMessage").innerHTML = "last name must not be empty";
                return false;
            }else if(this.fields.lastName.length < 2){
                document.getElementById("lastNameErrMessage").innerHTML = "minimal length of last name is 2";
                return false;
            }else{
                document.getElementById("lastNameErrMessage").innerHTML = "";
            }

            if(this.fields.username == ""){
                document.getElementById("usernameErrMessage").innerHTML = "username must not be empty";
                return false;
            }else if(this.fields.username.length < 6){
                document.getElementById("usernameErrMessage").innerHTML = "minimal length of username is 6";
                return false;
            }else{
                document.getElementById("usernameErrMessage").innerHTML = "";
            }

            if(this.fields.gender == ""){
                document.getElementById("genderErrMessage").innerHTML = "choose gender";
                return false;
            }else{
                document.getElementById("genderErrMessage").innerHTML = "";
            }


            if(this.fields.password == ""){
                document.getElementById("passwordErrMessage").innerHTML = "password must not be empty";
				console.log("PW NE VALJA");
                return false;
            }else if(this.fields.password.length < 6){
                document.getElementById("passwordErrMessage").innerHTML = "minimal length of password is 6";
                return false;
            }else{
                document.getElementById("passwordErrMessage").innerHTML = "";
            }

            if(this.fields.confirmPassword != this.fields.password ){
                document.getElementById("confirmPasswordErrMessage").innerHTML = "passwords are not same";
                return false;
            }else{
                document.getElementById("confirmPasswordErrMessage").innerHTML = "";
            }
            if(!this.validateEmail(this.fields.email)){
                document.getElementById("emailErrMessage").innerHTML = "not correct email";
                return false;
            }else{
                document.getElementById("emailErrMessage").innerHTML = "";
            }

            if(this.fields.birthDate =="" ){
                document.getElementById("birthDateErrMessage").innerHTML = "choose birth date";
                return false;
            }else if((new Date(this.fields.birthDate)).getTime() > new Date().getTime()){
                document.getElementById("birthDateErrMessage").innerHTML = "can not choose this date";
                return false;
            }
            else{
                document.getElementById("birthDateErrMessage").innerHTML = "";
            }
            console.log(this.fields.birthDate);

            return true;
        },
		registration: function(){
			console.log("USLO U REGISTRACIJu");
            console.log(this.fields.gender);
			let ok = this.checkValidation()
            if(!ok){
                return;
            }
            axios.post("/users", {
                "name":this.fields.name,
                "lastName":this.fields.lastName,
                "username":this.fields.username,
                "password":this.fields.password,
                "birthDate":new Date(this.fields.birthDate).getTime(),
                "email":this.fields.email,
				"gender":this.fields.gender
            }).then(response => {
				console.log("USPJESNA REGISTRACIJA");
                document.getElementById("registrationErrMessage").style.visibility = "hidden";
				document.getElementById("registrationSuccessMessage").style.visibility = "visible";
            }).catch(function(error){
				console.log("NEUSPJESNA REGISTRACIJA");
				document.getElementById("registrationErrMessage").style.visibility = "visible";
				document.getElementById("registrationSuccessMessage").style.visibility = "hidden";
			});
            console.log("SALJI 1 zahtjev za registraciju");
        },
        validateEmail:function(email) {
            if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})$/.test(email)) {
              return true;
            }
        
            return false;
          },
		  change:function(e){
			console.log(e);
			console.log("ON CHANGE??");
			console.log(this.credentials.username);
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