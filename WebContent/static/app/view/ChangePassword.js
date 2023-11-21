Vue.component("change-password", {
	data: function () {
		    return {
		      currentLoggedUser:null,
              edited : false,
              fields:{password:'', confirmPassword:''}
		    }
	},
	template: ` 	
<div v-if="currentLoggedUser" class="uploadBlock">
    <table class="registrationTable" style="margin-left: 350px; padding-top: 55px">
        <tr>
            <td><input class="userInput" type="password" id="password" placeholder="password" v-model="fields.password"></td>
            <td><p id="passwordErrMessage" style="color:red"></p></td>
        </tr>
         
        <tr>
            <td><input class="userInput" type="password" id="confirmPassword" placeholder="confirm password" v-model="fields.confirmPassword"></td>
            <td><p id="confirmPasswordErrMessage" style="color:red"></p></td>
        </tr>

        <tr>
            <button class="btn" v-on:click="editProfile()" > Change password </button>
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

            return true;
        },
		editProfile: function(){
			
			let ok = this.checkValidation()
            if(!ok){
                return;
            }
            this.currentLoggedUser.password = this.fields.password;
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
			}else{
                this.$router.push('/');//preusmjeravam ga na home, ako nije logovan
            }
		} )
    }
});