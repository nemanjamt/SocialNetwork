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
                    <img src="/userImages/homepageIcon.png" style="width: 24px; height: 24px;">
                </a>
            </li>

            <li>
                <a href="/#/search-users">
                    <img src="/userImages/searchIcon.png" style="width: 22px; height: 22px; padding-right: 20px">
                </a>
            </li>
        </ul>
    </header>
    
    <header v-else-if="OrdinaryUserLoggedIn && currentLoggedUser">
        <ul>
            <li>
                <a href="">
				<!-- prijavljenog obicnog korisnika -->
                    <img src="/userImages/homepageIcon.png" style="width: 24px; height: 24px;">
                </a>
            </li>

            <li>
                <a href="/#/search-users">
                    <img src="/userImages/searchIcon.png" style="width: 22px; height: 22px; ">
                </a>
            </li>
                <a href="/#/chat">
                <img src="/userImages/chat-icon.png" style="width: 24px; height: 25px; margin-left:22px">
                </a>
            <li>

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
            	<img @click="viewRequests()" src="/userImages/bellIcon.png" style="width: 22px; height: 22px; padding-right: 20px">                  
			</li>
			
			<li>
				<div class="dropdown">
				  <img @click="openProfile()" src="/userImages/userIcon.png" class="dropbtn" style="width: 22px; height: 22px; padding-right: 20px">
					  <div id="profileDropDown" class="dropdown-content">
						<a v-on:click="viewOwnProfile()"> Profile </a>
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
                    <img src="/userImages/homepageIcon.png" style="width: 24px; height: 24px;">
                </a>
            </li>

            <li>
                <a href="/#/search-users">
                    <img src="/userImages/searchIcon.png" style="width: 22px; height: 22px; ">
                </a>
            </li>
            <li>
                <a v-on:click="logout()">
                    <img src="/userImages/logout-icon.ico" style="width: 22px; height: 22px; ">
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
                this.$router.push("/");
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
        },
        viewOwnProfile:function(){
            this.$router.push("/view-profile?user="+this.currentLoggedUser.username);
            this.$router.go(0);
        },
        viewRequests:function(){
            this.$router.push('/all-requests');
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
                if(this.currentLoggedUser.role == "ADMIN"){
                    this.adminLoggedIn = true;
                    this.OrdinaryUserLoggedIn = false;
                }else{
                    this.OrdinaryUserLoggedIn = true;
                    this.adminLoggedIn = false;
                }
                
                
			}else{
                this.noLoggedIn = true;
                this.OrdinaryUserLoggedIn = false;
                this.adminLoggedIn = false;
            }
		} )
    }
});