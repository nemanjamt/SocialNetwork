const HomePage = { template: '<home-page></home-page>' }
const Login = { template: '<login></login>' }
const SearchUser = { template: '<search-user></search-user>' }
const ViewProfile = {template: '<view-profile></view-profile>'}
const UploadPhoto = {template: '<upload-photo></upload-photo>'}
const Navbar = {template: '<navbar></navbar>'}
const Registration = {template: '<registration></registration>'}
const CreatePost = {template: '<create-post></create-post>'}
const CreatePhoto = {template:'<create-photo></create-photo>'}
const EditProfile = {template: '<edit-profile></edit-profile>'}
const ChangePassword = {template:'<change-password></change-password>'}
const router = new VueRouter({
	  mode: 'hash',
	  routes: [
	    {path: '', component: HomePage},
	    {path: '/login', component: Login },
	    {path: '/search-users', component: SearchUser},
		{path: '/view-profile', component: ViewProfile},
		{path: '/upload-photo', component: UploadPhoto},
		{path: '/registration', component: Registration},
		{path:'/create-post', component: CreatePost},
		{path:'/create-photo', component:CreatePhoto},
		{path:'/edit-profile', component:EditProfile},
		{path:'/change-password', component:ChangePassword},
	  ]
});

/*router.beforeEach((to, from, next) => {
	const publicPages = ['/login', '/register', '/'];
	const authRequired = !publicPages.includes(to.path);
	const loggedIn = localStorage.getItem('user');
  
	// trying to access a restricted page + not logged in
	// redirect to login page
	if (authRequired && !loggedIn) {
	  next('/login');
	} else {
	  next();
	}
  });*/

var app = new Vue({
	router,
	el: '#social-media'
});

