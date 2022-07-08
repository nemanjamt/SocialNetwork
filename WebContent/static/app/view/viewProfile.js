/*
komponenta za prikaz profila
 */
Vue.component("view-profile", {
	data: function () {
		    return {
		      users: null
		    }
	},
	template: ` 
<div >
<div v-for="u in users">
{{u.name}}, {{u.lastName}}, {{u.birthDate}} <a href="/viewProfile">Pogledaj profil</a>
</div>

		
		
	
</div>		  
`
	, 
	methods : {
		init : function() {
			this.users = {};
		}
	},
	mounted () {
		
        axios
          .get('/users')
          .then(response => {
			
        	  this.users = response.data;
        	  
          });
        
    }
});