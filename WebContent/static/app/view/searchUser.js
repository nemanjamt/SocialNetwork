/*
komponenta koja prikazuje rezultate pretrage korisnika
 */
Vue.component("search-user", {
	data: function () {
		    return {
		      users: null,
			  searchParams:{name:"", lastName:"", startDate:"", endDate:""},
			  sortParams:{byName:false,byLastName:false,byBirthDate:false},
			  orderBy:{asc:true}
		    }
	},
	template: ` 
	<div>
		
	<table>
	<tr>
		<td align="left">First Name</td>
		<td align="left"><input type="text" name="firstName" v-model="searchParams.name" /></td>
	</tr>
	<tr>
	<td align="left">Last Name</td>
	<td align="left"><input type="text" name="lastName" v-model="searchParams.lastName" /></td>
	</tr>
	<tr>
	<td align="left">Start date</td>
	<td align="left"><input type="date" name="startDate" v-model="searchParams.startDate"/></td>
	</tr>
	<tr>
	<td align="left">End date</td>
	<td align="left"><input type="date" name="endDate" v-model="searchParams.endDate"/></td>
	</tr>
	<tr>
		<td colspan="2" align="left">
			<input type="button" name="search" value="search" v-on:click="searchUser()" />
		</td>
	</tr>
	</table>
	<div style="margin-bottom:20px;">
	<label>Sort by</label>
	<div>
	<label>name</label>
	<input type="checkbox" id="sort1" name="sortName" value="Name" v-model="sortParams.byName" >
	</div>
	
	<div>
	<label>last name</label>
	<input type="checkbox" id="sort2" name="sortLastName" value="Last name" v-model="sortParams.byLastName" >
	</div>
	
	<div>
	<label>birth date</label>
	<input type="checkbox" id="sort3" name="sortBirthDate" value="birth date" v-model="sortParams.byBirthDate" >
	</div>

	<div>
	<label>asceding</label>
	<input type="checkbox" id="sort4" name="sortAsceding" value="asceding" v-model="orderBy.asc" >
	</div>
	<input type="button" value="sort" v-on:click="sortUser()"/>
	</div>
	

<div >
	<div v-for="u in users" style="display: flex; margin-bottom:20px" >
	<div 
	style="margin-left:5px;
		
">
	<img :src="'/userImages/'+u.profilePicture +'.jpg'" alt="profile picture" width="60" height="60" style="border-radius:50%">
	</div>
	<div style="
	margin-left:20px;
	">
		<div>
		<b><a href=""> {{u.name}} {{u.lastName}} </a></b>
		</div>

		<div>
			<font size="2">
				Birth date {{u.birthDate | dateFormat('DD.MM.YYYY')}}
				
			</font>
		</div>
		
		
	</div>
	 
	</div>
</div>		  
</div>
</div>
`
	, 
	methods : {
		init : function() {
			this.users = {};
			
		},
		searchUser:function(){
			let startDate = (new Date(this.searchParams.startDate)).getTime();
			let endDate = (new Date(this.searchParams.endDate)).getTime();
			console.log(startDate);
			console.log(this.searchParams.startDate);
			axios
          .get('/search-users?name='+this.searchParams.name+'&lastName='+this.searchParams.lastName+'&startDate='+this.searchParams.startDate+'&endDate='+this.searchParams.endDate)
          .then(response => {
				console.log("USER??");
        	  this.users = response.data;
			  console.log(response.data);
        	  
          }).catch(function(error){
			console.log("ERROR?");
		  });
		},
		sortUser: function(){
			
			console.log(this.sortParams.byName);
			console.log(this.sortParams.byLastName);
			console.log(this.sortParams.byBirthDate);
			console.log(this.orderBy.asc);
			let params= '';
			if(this.sortParams.byName){
				if(params === ''){
					params += "name";
				}else{
					params += ",name";
				}
					
			}
			
			if(this.sortParams.byLastName){
				if(params === ''){
					params += "lastName";
				}else{
					params += ",lastName";
				}

			}
			
			if(this.sortParams.byBirthDate){
				if(params === ''){
					params += "birthDate";
				}else{
					params += ",birthDate";
				}
			}
			let order = '';
			if(this.orderBy.asc){
				order += 'asc';
			}else{
				order += 'desc';
			}
		axios
          .get('/users-sort?name='+this.searchParams.name+'&lastName='+this.searchParams.lastName+'&startDate='+this.searchParams.startDate+'&endDate='+this.searchParams.endDate+'&sortBy='+params+'&orderBy='+order)
          .then(response => {
				console.log("USER??");
        	  this.users = response.data;
			  console.log(response.data);
        	  
          }).catch(function(error){
			console.log("ERROR?");
		  });
		}
	},
	mounted () {
		
        axios
          .get('/users')
          .then(response => {
			
        	  this.users = response.data;

        	  
          });
        
    },
	filters: {
    	dateFormat: function (value, format) {
			convertedValue = new Date(value);
    		var parsed = moment(convertedValue);
    		return parsed.format(format);
    	}
   	}
});