Vue.component("navbar", {
	data: function () {
		    return {
		      noLoggedIn:false,
              OrdinaryUserLoggedIn:true,
              adminLoggedIn:false,
              currentLoggedUser:{}
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
<!--                    <img src="/userImages/searchIcon.png" style="width: 22px; height: 22px; padding-right: 20px">-->
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
                    <img src="/userImages/homepageIcon.png" style="width: 24px; height: 24px;">
<!--                    Homepage -->
                </a>
            </li>

            <li>
                <a href="/#/search-users">
                    <img src="/userImages/searchIcon.png" style="width: 22px; height: 22px; ">
<!--                    Search users-->
                </a>
            </li>
            
            <li>
                <div class="dropdown">
                    <img @click="openCreate()" src="/userImages/addIcon.png" class="dropbtn" style="width: 22px; height: 22px; padding-right: 20px">
                    <div id="createDropDown" class="dropdown-content">
                        <a href="#/create-post">add post</a>
                        <a href="#/create-photo">add photo</a>
                    </div>
                </div>
			</li>
			
            <li>              
            	<img src="/userImages/bellIcon.png" style="width: 22px; height: 22px; padding-right: 20px">                  
			</li>
			
			<li>
				<div class="dropdown">
				  <img @click="openProfile()" src="/userImages/userIcon.png" class="dropbtn" style="width: 22px; height: 22px; padding-right: 20px">
					  <div id="profileDropDown" class="dropdown-content">
						<a :href="'#/view-profile?user='+currentLoggedUser.username"> Profile </a>
						<a href="#/edit-profile"> Edit Profile</a>
						<a href="#/change-password"> Change Password </a>
						<hr style="width: 95%">
						<a v-on:click="logout()"> Logout </a>
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
				this.$router.go(0);
			}
			);

		},
        openCreate:function() {
            document.getElementById("createDropDown").classList.toggle("show");
        },
        openProfile:function(){
            document.getElementById("profileDropDown").classList.toggle("show");
        },
        createClick:function(){
            this.$router.push('/create-post');
        },
        myFunction:function() {
            document.getElementById("myDropdown").classList.toggle("show");
        }
          
        

        
		
	},
	mounted () {
        window.onclick = function(event) {
            if (!event.target.matches('.dropbtn')) {
          
              var dropdowns = document.getElementsByClassName("dropdown-content");
              var i;
              for (i = 0; i < dropdowns.length; i++) {
                var openDropdown = dropdowns[i];
                if (openDropdown.classList.contains('show')) {
                  openDropdown.classList.remove('show');
                }
              }
            }
          }

        axios.get('/currentUser').then(response => {
			if(response.data != null){
                this.currentLoggedUser = response.data;
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