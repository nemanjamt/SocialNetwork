Vue.component("registration", {
	data: function () {
		    return {
		      userLogedIn:false,
              fields:{name:"", lastName:"", username:"", email:"", password:"", confirmPassword:"", birthDate:"", gender:""}
		    }
	},
	template: ` 
	<div class="searchUsersBlock">
			
			<table>
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
            </table>
			
	</div>		
	
`
	, 
	methods : {
		init : function() {
			console.log("DA?");
		}, 
        checkValidation: function(){
            
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
                "email":this.fields.email
            }).then(response => {
                console.log(response);
            });
            console.log("SALJI 1 zahtjev za registraciju");
        },
        validateEmail:function(email) {
            if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})$/.test(email)) {
              return true;
            }
        
            return false;
          }
		
	},
	mounted () {
        axios.get('/currentUser').then(response => {
			if(response.data != null){
				this.$router.push('/');
	
			}
		} )
    }
});