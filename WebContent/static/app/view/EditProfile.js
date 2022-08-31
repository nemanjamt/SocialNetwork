Vue.component("edit-profile", {
	data: function () {
		    return {
		      currentLoggedUser:null,
              userBirthDate:null,
              edited : false
		    }
	},
	template: ` 	
<div v-if="currentLoggedUser"  class="uploadBlock">
    <table class="registrationTable" style="margin-left: 350px; padding-top: 55px">
        <tr>
            <td><input class="userInput" type="text" id="name" placeholder="name" v-model="currentLoggedUser.name" @change="handleChange"></td>
            <td><p id="nameErrMessage" style="color:red"></p></td>
        </tr>
        
        <tr>
            <td><input class="userInput" type="text" id="lastName" placeholder="last name" v-model="currentLoggedUser.lastName" @change="handleChange"></td>
            <td><p id="lastNameErrMessage" style="color:red"></p></td>
        </tr>

        <tr>
            <td><input class="userInput" type="text" id="email" placeholder="email" v-model="currentLoggedUser.email" @change="handleChange"></td>
            <td>
                <p id="emailErrMessage" style="color:red"></p>
            </td>
        </tr>

        <tr>
            <td>
                <select class="userInput" id="gender" placeholder="gender" v-model="currentLoggedUser.gender" v-on:change="handleChange()">
                    <option value="" disabled selected hidden> Select your gender </option>
                    <option value="MALE">Male</option>
                    <option value="FEMALE">Female</option>
                </select>
            </td>
            <td>
                <p id="genderErrMessage" style="color:red"></p>
            </td>
        </tr>
        <tr>
        <td><label> Private account</label><input type="checkbox" id="privateAccount" v-model="currentLoggedUser.privateAccount" v-on:change="handleChange()"></td>
        </td>
        
        <tr>
            <td>
                <input style="float:right" type="date" placeholder="birth date" id="birthDate" v-model="userBirthDate" v-on:change="handleChange()">
            </td>
            <td>
                <p id="birthDateErrMessage" style="color:red"></p>
            </td>
        </tr>
        
        
        
        <tr>
            
            <button class="btn" v-on:click="editProfile()" :disabled='!edited'> Save changes</button>
        </tr>
        <tr>
            <td><p id="registrationErrMessage" class="errMessage">Fail.Try another username.</p></td>
            
        </tr>
        <tr>
            <td><p id="registrationSuccessMessage"  class="successMessage" >Registration successfully done</p></td>
        </tr>
    </table>
</div>
	
	
`
	, 
	methods : {
		init : function() {
			console.log("DA?");
		}, 
        handleChange : function() {
            console.log("{RPM");
			this.edited=true;
		},
		
		checkValidation: function(){
            console.log(this.currentLoggedUser);
            if(this.currentLoggedUser.name == ""){
                document.getElementById("nameErrMessage").innerHTML = "name must not be empty";
                return false;
            }else if(this.currentLoggedUser.name.length < 2){
                document.getElementById("nameErrMessage").innerHTML = "minimal length of name is 2";
                return false;
            }else{
                document.getElementById("nameErrMessage").innerHTML = "";
            }

            if(this.currentLoggedUser.lastName == ""){
                document.getElementById("lastNameErrMessage").innerHTML = "last name must not be empty";
                return false;
            }else if(this.currentLoggedUser.lastName.length < 2){
                document.getElementById("lastNameErrMessage").innerHTML = "minimal length of last name is 2";
                return false;
            }else{
                document.getElementById("lastNameErrMessage").innerHTML = "";
            }

          

            if(this.currentLoggedUser.gender == ""){
                document.getElementById("genderErrMessage").innerHTML = "choose gender";
                return false;
            }else{
                document.getElementById("genderErrMessage").innerHTML = "";
            }


           
           
            if(this.userBirthDate =="" ){
                document.getElementById("birthDateErrMessage").innerHTML = "choose birth date";
                return false;
            }else if((new Date(this.userBirthDate)).getTime() > new Date().getTime()){
                document.getElementById("birthDateErrMessage").innerHTML = "can not choose this date";
                return false;
            }
            else{
                document.getElementById("birthDateErrMessage").innerHTML = "";
            }
            console.log(this.currentLoggedUser.birthDate);

            return true;
        },
		editProfile: function(){
			
			let ok = this.checkValidation()
            if(!ok){
                return;
            }
            this.currentLoggedUser.birthDate = new Date(this.userBirthDate).getTime();
            axios.put('/users', JSON.stringify(this.currentLoggedUser)).then(result => {
                console.log(result.data);
                this.$router.go(0);
            });
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
		},
		
		
	},
	mounted () {
        axios.get('/currentUser').then(response => {
			if(response.data != null){
				this.currentLoggedUser = response.data;
                this.userBirthDate = new Date(this.currentLoggedUser.birthDate);

    		    this.userBirthDate = moment(new Date(this.currentLoggedUser.birthDate)).format("YYYY-MM-DD");
                console.log(this.userBirthDate);
			}else{
                this.$router.push('/');//preusmjeravam ga na home, ako nije logovan
            }
		} )
    }
});