const HomePage = { template: '<home-page></home-page>' }
const Login = { template: '<login></login>' }
const SearchUser = { template: '<search-user></search-user>' }
const router = new VueRouter({
	  mode: 'hash',
	  routes: [
	    { path: '/', component: HomePage},
	    { path: '/login', component: Login },
	    {path: '/search-user', component: SearchUser}
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

