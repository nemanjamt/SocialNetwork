/*
komponenta koja prikazuje rezultate pretrage korisnika
 */
Vue.component("search-user", {
	data: function () {
		    return {
		      users: null,
			  searchParams:{name:"", lastName:"", startDate:"", endDate:""},
			  sortParam:"",
			  orderBy:{asc:false},
			  isAsceding:false,
			  currentLoggedUser:null
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
						<div class="datepickerBlock"> 
							<span> Start date: </span>
							<input type="date"  name="startDate" v-model="searchParams.startDate"/>
						
						</div>
						
					</td>
						
					<td>
						<div class="datepickerBlock"> 
							<span> End date: </span>
							<input type="date" name="endDate" v-model="searchParams.endDate"/>
						</div>
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
					
					
					<div v-on:click="clickedArrow"  style="margin-top: -18px">
						<div v-if="isAsceding">
							<img  src="/userImages/clickedArrowIcon.png" style="width: 34px; height: 34px; ">
						</div>
						<div v-else>
							<img for="sort4"  src="/userImages/upArrowIcon.png" style="width: 34px; height: 34px; ">
						</div>
						
						
					</div>
					
					
					<!-- sort ikonica-->
					
<!--					<div class="order">-->
<!--						<label>asceding</label>-->
<!--						<input type="checkbox" id="sort4" name="sortAsceding" value="asceding" v-model="orderBy.asc" >-->
<!--					</div>-->
					
					
				</div>
				
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
						<div v-if="currentLoggedUser">
							<div v-if="currentLoggedUser.role == 'ADMIN'"style="margin-left: 10px;">
								<button v-if="!u.blocked" v-on:click="blockUser(u)" class="btn" style="width:120px ;height:40px">block user</button>
								<button v-else v-on:click="unblockUser(u)" class="btn" style="width:120px ;height:40px">unblock user</button>
							</div>
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
		blockUser:function(user){
			user.blocked = true;
			axios.put("/users", JSON.stringify(user)).then(result => {
				
			});
		},
		unblockUser:function(user){
			user.blocked = false;
			axios.put("/users", JSON.stringify(user)).then(result => {
				
			});
		},

		clickedArrow:function(){
			console.log('clicked?');
			
			this.isAsceding = this.isAsceding ? false:true;
			// if (checkBox.checked === true){
			// 	oldArrow.style.display = "none";
			// 	newArrow.style.display = "block";

			// } else {
			// 	oldArrow.style.display = "block";
			// 	newArrow.style.display = "none";
			// }
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
			console.log(this.isAsceding);
			this.orderBy.asc = this.isAsceding;
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

		  axios.get('/currentUser').then(response => {
			if(response.data != null){
                this.currentLoggedUser = response.data;
			}
		} )
        
    },
	filters: {
    	dateFormat: function (value, format) {
			convertedValue = new Date(value);
    		var parsed = moment(convertedValue);
    		return parsed.format(format);
    	}
   	}
});