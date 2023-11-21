/*
komponenta za prikaz profila
 */
Vue.component("comments-modal", {
	data: function () {
		    return {

		      user: null,
			  posts:null,
			  photos:null,
			  photosClicked:false,
			  postsClicked:false
		    }
	},
	template: ` 


	
<!--	user does not exist-->
	<div v-else>Korisnik ne postoji</div>
	  
`
	,
	methods : {

	},

});