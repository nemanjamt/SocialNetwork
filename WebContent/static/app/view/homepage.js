Vue.component("home-page", {
	data: function () {
		    return {
		      userLogedIn:false
		    }
	},
	template: ` 
	<div class="searchUsersBlock" v-if="this.userLogedIn">
			<div  v-if="this.userLogedIn">
			KORISNIK JE ULOGOVAN
			<button v-on:click="logout()">Logout</button>
			</div>
			<div v-else>
			KORISNIK NIJE ULOGOVAN
			</div>
			
			
	</div>		
	<div v-else class="searchUsersBlock">
	Korisnik nije ulogovan
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