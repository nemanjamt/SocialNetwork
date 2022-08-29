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
                    Homepage prijavljenog obicnog korisnika
                </a>
            </li>

            <li>
                <a href="/#/search-users">
                    Search users
                </a>
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