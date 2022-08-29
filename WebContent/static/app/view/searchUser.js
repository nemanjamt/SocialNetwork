/*
komponenta koja prikazuje rezultate pretrage korisnika
 */
Vue.component("search-user", {
	data: function () {
		    return {
		      users: null,
			  searchParams:{name:"", lastName:"", startDate:"", endDate:""},
			  sortParam:"",
			  orderBy:{asc:true}
		    }
	},
	template: ` 
 
		<div class="searchAllUsersBlock">
		
			<div class="split left">
				<!--		input name and last name -->
			<table class="userSortInput">
				<tr>
					<td>
						<input type="text" name="firstName" v-model="searchParams.name" placeholder="First Name" class="userInput" />
					</td>
					
					<td>
						<input type="text" name="lastName" v-model="searchParams.lastName" placeholder="Last Name" class="userInput" />
					</td>
				</tr>
				
				<tr>
					<td>
						<input type="date"  name="startDate" v-model="searchParams.startDate"/>
					</td>
	
					<td>
						<input type="date" name="endDate" v-model="searchParams.endDate"/>
					</td>
				</tr>

				<tr>
					<td>
						<span> Start date </span>
					</td>
					<td>
						<span> End date </span>
					</td>
				</tr>
			</table>
			
			<div style="text-align: center">
				<input type="button" name="search" value="Search" v-on:click="searchUser()" class="btn"/>
			</div>
	
		</div>
			
			<div class="split right">
			
<!-- sort part -->
				<div class="sortUsersBlock">
					<!--	sort by -->
					<form>
						<select v-model="sortParam"> 
							<option disabled selected value> -- select an option -- </option>
							<option id="sort1" name="sortName" value="name" > Name </option>
							<option id="sort2" name="sortLastName" value="lastName"  > Last Name </option>
							<option id="sort3" name="sortBirthDate" value="birthDate" > Birth Date </option>
						</select>
						
						<input type="button" value="Sort" v-on:click="sortUser()" class="btn"/>
					</form>
					
					<!-- sort ikonica-->
					
					<div class="order">
						<label>asceding</label>
						<input type="checkbox" id="sort4" name="sortAsceding" value="asceding" v-model="orderBy.asc" >
					</div>
					
					
				</div>
					
<!--						
						<label>name</label>
						<input type="checkbox" id="sort1" name="sortName" value="Name" v-model="sortParams.byName" >
		
						<label>last name</label>
						<input type="checkbox" id="sort2" name="sortLastName" value="Last name" v-model="sortParams.byLastName" >
		
						<label>birth date</label>
						<input type="checkbox" id="sort3" name="sortBirthDate" value="birth date" v-model="sortParams.byBirthDate" >
		
						<label>asceding</label>
						<input type="checkbox" id="sort4" name="sortAsceding" value="asceding" v-model="orderBy.asc" >
					
						<input type="button" value="Sort" v-on:click="sortUser()" class="btn"/>
-->
				
				<!-- users info -->
				<div class="showUsersBlock">
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
			
			
			console.log(this.orderBy.asc);
			console.log(this.sortParam + " mhmmm");
			let order = '';
			if(this.orderBy.asc){
				order += 'asc';
			}else{
				order += 'desc';
			}
			let startDate = (new Date(this.searchParams.startDate)).getTime();
			let endDate = (new Date(this.searchParams.endDate)).getTime();
		axios
          .get('/users-sort?name='+this.searchParams.name+'&lastName='+this.searchParams.lastName+'&startDate='+startDate+'&endDate='+endDate+'&sortBy='+this.sortParam+'&orderBy='+order)
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
			console.log(location.hostname);
			console.log(location.port);
			console.log("DA??");
        	  
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