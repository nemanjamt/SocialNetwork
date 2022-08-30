
Vue.component("create-post", {
	data: function () {
		    return {
		      currentLoggedUser:null,
              photo:null,
              text:""
		    }
	},
	template: ` 
    <div class="userProfilBlock">
	<div class="userPostsBlock">
            <div class="userPost">
            <table>
                <input type="text" placeholder="post text" v-model="text">
                <tr>
                    <td> <input type="file" id="poster" name="poster" v-on:change="addPhoto(photo)"> </td>
                </tr>
                <tr>
                    <td align=center colspan=2>
                        <input type="button" value="add post" v-on:click="createPost()"/>
                    </td>
                </tr>
            </table>
            </div>
            
	</div>
    </div>
	
`
	, 
	methods : {
		init : function() {		
			
		},
        addPhoto:function(){


            let input = document.querySelector('input#poster');
			let file = input.files[0];
			let reader = new FileReader();

            try{
                reader.readAsDataURL(file);
          }catch(exception){
              alert("Morate unijeti sliku.");
              return;
          }
            let content = 'nesto prije';
			reader.onloadend = ( content => {
			    // Since it contains the Data URI, we should remove the prefix and keep only Base64 string
			    let b64 = reader.result.replace(/^data:.+;base64,/, '');
                this.photo = b64;
                
                
			});
            
        },
        createPost:function(){
            if(this.text == ""){
                console.log("mora postojati text");
                return;
            }
            
            
            
            let obj={
                imagePath: this.photo,
                text:this.text,
                date: new Date().getTime(),
                username: currentLoggedUser.username
            }
            
            axios.post("/posts", JSON.stringify(obj)).then(result => {
                this.$router.go(0);
            });
        }
		
		
	},
	mounted () {
        
		axios.get('/currentUser').then(response => {
			if(response.data == null){
				this.$router.push('/');//preusmjeravam ga na home, ako nije logovan
			}else{
                currentLoggedUser = response.data;
                console.log(currentLoggedUser);
            }
		} ).catch(error => {
			console.log("da");
		}
		);

        console.log("MP");
    }
});