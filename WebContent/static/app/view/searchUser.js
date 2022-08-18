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
	<div class="searchUsersBlock">
		
		<div class="splitLeft">
<!--		input name and last name -->
			<table>
				<tr>
					<td>
						<input type="text" name="firstName" v-model="searchParams.name" placeholder="First Name" class="userInput" />
					</td>
				</tr>
				
				<tr>
					<td>
						<input type="text" name="lastName" v-model="searchParams.lastName" placeholder="Last Name" class="userInput" />
					</td>
				</tr>
				
				<tr>
				<td>
					<input type="date" name="startDate" v-model="searchParams.startDate"/>
				
					<input style="margin-left: 8%" type="date" name="endDate" v-model="searchParams.endDate"/>
				</td>
				</tr>
				
				<tr>
				<td>
					<span> Start date </span>
					<span style="margin-left: 32%"> End date </span>
				</td>
				</tr>
				
				<tr>
					<td>
						<input type="button" name="search" value="Search" v-on:click="searchUser()" class="btn" style="margin-left: 29%" />
					</td>
				</tr>
	
			</table>
	
<!--	sort by -->
			<p style="margin-left: 18%">Sort by</p>
			
			<div style="margin-left: 20%">
				<label>name</label>
				<input type="checkbox" id="sort1" name="sortName" value="Name" v-model="sortParams.byName" >

				<label>last name</label>
				<input type="checkbox" id="sort2" name="sortLastName" value="Last name" v-model="sortParams.byLastName" >

				<label>birth date</label>
				<input type="checkbox" id="sort3" name="sortBirthDate" value="birth date" v-model="sortParams.byBirthDate" >

				<label>asceding</label>
				<input type="checkbox" id="sort4" name="sortAsceding" value="asceding" v-model="orderBy.asc" >
			</div>
			
			<input type="button" value="Sort" v-on:click="sortUser()" class="btn" style="margin-top: 3%; margin-left: 36%"/>
	
	</div>
	
<!--show users -->
	<div class="splitRight">
	
		<!-- user block -->
		<div v-for="u in users" class="searchedUserBlock">
		
			<!-- pic block -->
			<div class="searchedUserPic">
				<img :src="'/userImages/'+u.profilePicture " alt="profile picture" width="60" height="60" style="border-radius:50%">
			</div>
			
			<!-- info block -->
			<div  class="searchUsersInfo">
				<b>
					<a :href="'#/view-profile?user='+u.username"> 
						{{u.name}} {{u.lastName}} 
					</a>
				</b>
		
				<p> Birth date {{u.birthDate | dateFormat('DD.MM.YYYY')}} </p>
				
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
			
			axios
          .get('/search-users?name='+this.searchParams.name+'&lastName='+this.searchParams.lastName+'&startDate='+startDate+'&endDate='+endDate)
          .then(response => {
				
        	  this.users = response.data;
			  
        	  
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
			let startDate = (new Date(this.searchParams.startDate)).getTime();
			let endDate = (new Date(this.searchParams.endDate)).getTime();
		axios
          .get('/users-sort?name='+this.searchParams.name+'&lastName='+this.searchParams.lastName+'&startDate='+startDate+'&endDate='+endDate+'&sortBy='+params+'&orderBy='+order)
          .then(response => {
				
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