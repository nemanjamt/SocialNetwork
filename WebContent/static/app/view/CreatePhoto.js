
Vue.component("create-photo", {
	data: function () {
		    return {
		      currentLoggedUser:null,
              photo:null,
              text:""
		    }
	},
	template: ` 
<div class="uploadBlock">



    <div class="split left"> 
        <table class="uploadContent">
            <tr>
                <td> 
                    <input type="file" id="poster" name="poster" v-on:change="addPhoto(photo)" hidden> 
                    <label for="poster" class="uploadPhoto"> <b> Choose photo </b> </label>
                </td>
            </tr>
            <tr>
                <td>
                    <input type="button" value="Add photo" v-on:click="createPost()" class="btn"/>
                </td>
            </tr>
        </table>
    </div>
    
    <div class="split right">
        <input type="text" placeholder="Photo text.." v-model="text" class="inputDescription">
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
			reader.onloadend = ( content => {
			    // Since it contains the Data URI, we should remove the prefix and keep only Base64 string
			    let b64 = reader.result.replace(/^data:.+;base64,/, '');
                this.photo = b64;
                
                
			});
            
        },
        createPost:function(){
            if(this.photo == null){
                console.log("mora postojati text");
                return;
            }
            
            
            
            let obj={
                content: this.photo,
                text:this.text,
                username: currentLoggedUser.username
            }
            
            axios.post("/photo", JSON.stringify(obj)).then(result => {
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