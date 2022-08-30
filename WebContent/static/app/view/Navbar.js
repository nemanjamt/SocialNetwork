Vue.component("navbar", {
	data: function () {
		    return {
		      noLoggedIn:false,
              OrdinaryUserLoggedIn:true,
              adminLoggedIn:false
		    }
	},
	template: ` 
	<header v-if="noLoggedIn">
        <ul>
            <li>
                <a href="">
                    Homepage
                </a>
            </li>

            <li>
                <a href="/#/search-users">
                    Search users
                </a>
            </li>
        </ul>
    </header>
    
    <header v-else-if="OrdinaryUserLoggedIn">
        <ul>
            <li>
                <a href="">
				<!-- prijavljenog obicnog korisnika -->
                    Homepage 
                </a>
            </li>

            <li>
                <a href="/#/search-users">
                    Search users
                </a>
            </li>
            
            <li>
            	<img src="/userImages/addIcon.png" style="width: 22px; height: 22px; padding-right: 20px">
			</li>
			
            <li>
            	<img src="/userImages/bellIcon.png" style="width: 22px; height: 22px; padding-right: 20px">
			</li>
			
			<li>
				<div class="dropdown">
				  <img src="/userImages/userIcon.png" style="width: 22px; height: 22px; padding-right: 20px">
					  <div class="dropdown-content">
						<a href="#"> Profile </a>
						<a href="#"> Edit Profile</a>
						<a href="#"> Change Password </a>
						<hr style="width: 95%">
						<a href="#"> Logout </a>
					  </div>
				</div>
			</li>
			
            
            
        </ul>
        
    </header>
    
    <header v-else>
        <ul>
            <li>
                <a href="">
                    Homepage admina
                </a>
            </li>

            <li>
                <a href="/#/search-users">
                    Search users
                </a>
            </li>
        </ul>
    </header>
	
`
	, 
	methods : {
		init : function() {
			console.log("DA?");
		},

		logout: function(){
			axios.put("/logout").then(response => {
				// this.$router.push('/');
				this.$router.go(0);
			}
			);
		},

        
		
	},
	mounted () {

        axios.get('/currentUser').then(response => {
			if(response.data != null){
				this.noLoggedIn = false;
                this.OrdinaryUserLoggedIn = true;
                this.adminLoggedIn = false;
	
			}else{
                this.noLoggedIn = true;
                this.OrdinaryUserLoggedIn = false;
                this.adminLoggedIn = false;
            }
		} )
    }
});