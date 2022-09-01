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
const PhotoFullView = {template:'<photo-full-view></photo-full-view>'}
const PostFullView = {template:'<post-full-view></post-full-view>'}
const AllRequestsView = {template:'<requests-view></requests-view>'}
const MutualFriends = {template:'<mutual-friends></mutual-friends>'}
const MyFriends = {template:'<my-friends></my-friends>'}
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
		{path:'/photo-full-view', component:PhotoFullView},
		{path:'/post-full-view', component:PostFullView},
		{path:'/all-requests', component: AllRequestsView},
		{path:"/mutual-friends", component:MutualFriends},
		{path:"/my-friends", component:MyFriends}
	  ]
});



var app = new Vue({
	router,
	el: '#social-media'
});

