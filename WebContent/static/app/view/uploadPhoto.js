/*
komponenta za uplodovanje slike
 */
Vue.component("upload-photo", {
	data: function () {
		    return {
		      user: null,
			  posts:null	
		    }
	},
	template: ` 

	<div style="margin-top: 12%">
	<table>
			<tr>
				<td> <h2>Slika:</h2> </td> <td> <input type="file" id="poster" name="poster"> </td>
			</tr>
			<tr>
				<td align=center colspan=2>
					<input type="button" value="Posalji" v-on:click="sendPhoto()"/>
				</td>
			</tr>
		</table>
	</div>
	  
`
	, 
	methods : {
		init : function() {
			this.users = {};
		},
        sendPhoto(){
            let input = document.querySelector('input#poster');
			let file = input.files[0];
			let reader = new FileReader();
			console.log("POGADJA?");
            try{
                reader.readAsDataURL(file);
          }catch(exception){
              alert("Morate unijeti sliku.");
              return;
          }
			reader.onloadend = function () {
                console.log(2);
			    // Since it contains the Data URI, we should remove the prefix and keep only Base64 string
			    let b64 = reader.result.replace(/^data:.+;base64,/, '');
			    console.log(b64); //-> "R0lGODdhAQABAPAAAP8AAAAAACwAAAAAAQABAAACAkQBADs="
			    
			    let obj = {content : b64};
                console.log(obj);
               
              axios.post("/photo", JSON.stringify(obj));	
				
			    
			};
    }
	},
	mounted () {
		
		//   axios
        //   .get('/posts?user='+userId)
        //   .then(response => {
			
        // 	  this.posts = response.data;
			
        //   });
        
    },
	
});