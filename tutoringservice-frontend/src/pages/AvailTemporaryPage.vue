	<template>
		<div id="availTemporaryPage" class="wrapper">
			<img src="../assets/321tutor.png">
			<div class="tab">
				<button class="tablinks" @click="toMainPage">Profile</button>	
				<button class="tablinks" @click="toCoursePage">Courses</button>
				<button class="tablinks" @click="toAvailabilityPage">Availabilities</button>
				<button class="tablinks" @click="toSessionPage">Sessions</button>
				<button class="tablinks" style="float:right" v-on:click="toLoginPage">Logout</button>
				<button class="tablinks" @click="toTutorReviewsPage">Received Reviews</button>
				<button class="tablinks" @click="toAllTutorsPage">All Tutors</button>	
			</div>
			&nbsp;
			<h1>AVAILABILITIES</h1>
			&nbsp;
			<div>
				<input type="date" v-model="date" v-on:change="displayAvailability" placeholder="Date (YYYY-MM-DD)">
				&nbsp;
				<button class="glow-on-hover" v-on:click="addAvailability">ADD AVAILABILITY</button>
			</div>
			&nbsp;
			<table class="table table-light table-hover" align="center" style="width:25%">
				<thead class="thead-dark">
					<tr>
						<th scope="col" style="width:40%">Time</th>
						<th scope="col"></th>
					</tr>
					<tr>
						<td align="right"> 9:00 am </td>
						<td id="button-9" class='notavailable' v-on:click="toggle"></td>
					</tr>
					<tr>
						<td align="right"> 10:00 am </td>
						<td id="button-10" class='notavailable' v-on:click="toggle"></td>
					</tr>
					<tr>
						<td align="right"> 11:00 am </td>
						<td id="button-11" class='notavailable' v-on:click="toggle"></td>
					</tr>
					<tr>
						<td align="right"> 12:00 pm </td>
						<td id="button-12" class='notavailable' v-on:click="toggle"></td>
					</tr>
					<tr>
						<td align="right"> 13:00 pm </td>
						<td id="button-13" class='notavailable' v-on:click="toggle"></td>
					</tr>
					<tr>
						<td align="right"> 14:00 pm </td>
						<td id="button-14" class='notavailable' v-on:click="toggle"></td>
					</tr>
					<tr>
						<td align="right"> 15:00 pm </td>
						<td id="button-15" class='notavailable' v-on:click="toggle"></td>
					</tr>
					<tr>
						<td align="right"> 16:00 pm </td>
						<td id="button-16" class='notavailable' v-on:click="toggle"></td>
					</tr>
					<tr>
						<td align="right"> 17:00 pm </td>
						<td id="button-17" class='notavailable' v-on:click="toggle"></td>
					</tr>
					<tr>
						<td align="right"> 18:00 pm </td>
						<td id="button-18" class='notavailable' v-on:click="toggle"></td>
					</tr>
					<tr>
						<td align="right"> 19:00 pm </td>
						<td id="button-19" class='notavailable' v-on:click="toggle"></td>
					</tr>
					<tr>
						<td align="right"> 20:00 pm </td>
						<td id="button-20" class='notavailable' v-on:click="toggle"></td>
					</tr>
					
				</thead>
				<tbody>
					
				</tbody>
			</table>

			<h1></h1>
			<table class="table table-light table-hover" align="center">
				<thead class="thead-dark">
					<tr>
						<th scope="col">Date</th>
						<th scope="col">Start Time</th>
						<th scope="col">End Time</th>
						<th scope="col"></th>
						<th scope="col"></th>
					</tr>
				</thead>
				<tbody>
					<tr v-for="availability in availabilities" :key="availability">
						<td >{{availability.date}}</td>
						<td >{{availability.startTime}}</td>
						<td >{{availability.endTime}}</td>
						<td><button @click="deleteAvailability(availability)" class="btn btn-danger" >Delete</button></td>
						<td><button @click="updateAvailability(availability)" class="btn btn-warning" >Update</button></td>
					</tr>
				</tbody>
			</table>
	</div>

	</template>

	<script>
	import axios from 'axios'
	var config = require('../../config')

	var frontendUrl = 'https://' + config.build.host + ':' + config.build.port
	var backendUrl = 'https://' + config.build.backendHost + ':' + config.build.backendPort

	var AXIOS = axios.create({
	baseURL: backendUrl,
	headers: { 'Access-Control-Allow-Origin': frontendUrl }
	})

	export default {
	name: 'AvailTemporaryPage',

		data() {
			return {
				availabilities: [],
				date: ''
			}
		},

		created: function() {
			AXIOS.get(`/user`)
			.then(response => {
					this.email = response.data.email
					AXIOS.get(`/availabilities/`+this.email, {}, {})
					.then(response => {
						this.availabilities = response.data
					})
					.catch(e => {
						var errorMsg = e.response.data.message
						window.alert(errorMsg)
					})
			})
			.catch(e => {
				var errorMsg = e.response.data.message
				window.alert(errorMsg)
			})
		},

		methods: {
			displayAvailability() {
				var availWithDateStartTime = []
				var availWithDateEndTime = []

				for(var i = 9; i < 21; i++) {
					var cell = document.getElementById("button-"+i)
					cell.className = "notavailable"
				}

				for(var i = 0; i < this.availabilities.length; i++) {
					if(this.availabilities[i].date == this.date) {
						availWithDateStartTime.push(this.availabilities[i].startTime)
						availWithDateEndTime.push(this.availabilities[i].endTime)
					}
				}

				for(var i = 0; i < availWithDateStartTime.length; i++) {
					var startTime = availWithDateStartTime[i].slice(0,2)
					var endTime = availWithDateEndTime[i].slice(0,2)
					if(startTime == '09') {
						startTime = startTime.slice(1,2)
					}

					for(var j = parseInt(startTime, 10); j < parseInt(endTime, 10); j++) {
						var cell = document.getElementById("button-"+j)
						cell.className = "available"
					}
				}
			},
			
			toggle(event){
				var cell=event.target;			 
				if (cell.className == "available")
					cell.className = "notavailable";
				else if(cell.className == "notavailable")
					cell.className = "available";
			},
			
		toMainPage(){
		this.$router.push('MainPage')
		},
		toSessionPage(){
		this.$router.push('SessionPage')
		},
		toAllTutorsPage() {
			this.$router.push('AllTutorsPage')
		},

		toAvailabilityPage() {
		this.$router.push('AvailTemporaryPage')
		},

		toTutorReviewsPage() {
			this.$router.push('TutorReviewsPage')
		},

		toCoursePage() {
			this.$router.push('CoursePage2')
		},

		toLoginPage() {
		AXIOS.put(`/logout`)
			.then(response => {
			this.$router.push({ path: '/' })
			})
			.catch(e => {
			var errorMsg = e.response.data.message
			window.alert(errorMsg)
		})
		},

			addAvailability() {
				var startTimes = []

				for(var i = 9; i <= 20; i++) {
					var cell = document.getElementById("button-"+i)
					if(cell.className == 'available') {
						startTimes.push(i)
					}
				}

				for(var i = 0; i < startTimes.length; i++) {
					var j = i;
					var startTime = ''

					if(startTimes[i] == '9') {
						startTime = '09:00'
					}
					else {
						startTime = startTimes[i]+':00'
					}

					var increment = 1
					while(startTimes.includes(startTimes[i]+1)) {
						i++
						increment++
					}

					var endTime = (startTimes[j]+increment)+':00'

					AXIOS.post(`/availabilities/`+this.email+`?date=`+this.date+`&startTime=`+startTime+`&endTime=`+endTime, {}, {})
					.then(response => {
						AXIOS.get(`/availabilities/`+this.email, {}, {})
						.then(response => {
							this.availabilities = response.data
							this.displayAvailability()
						})
						.catch(e => {
							var errorMsg = e.response.data.message
							window.alert(errorMsg)
						})
					})
					.catch(e => {
						var errorMsg = e.response.data.message
						window.alert(errorMsg)
					})
				}
			},

			deleteAvailability(avail) {
				AXIOS.delete(`/availabilities/`+this.email+`?date=`+avail.date+`&startTime=`+avail.startTime.slice(0,5)+`&endTime=`+avail.endTime.slice(0,5), {}, {})
				.then(response => {
					AXIOS.get(`/availabilities/`+this.email, {}, {})
					.then(response => {
						this.availabilities = response.data
						this.displayAvailability()
					})
					.catch(e => {
						var errorMsg = e.response.data.message
						window.alert(errorMsg)
					})
				})
				.catch(e => {
					var errorMsg = e.response.data.message
					window.alert(errorMsg)
				})
			},

			updateAvailability(avail) {
				var date = window.prompt('Date (YYYY-MM-DD)')
				var startTime = window.prompt('Start Time (HH:MM)')
				var endTime = window.prompt('End Time (HH:MM)')

				AXIOS.put(`/availabilities/`+this.email+`?oldDate=`+avail.date+`&oldStartTime=`+avail.startTime.slice(0,5)+`&oldEndTime=`+avail.endTime.slice(0,5)+`&newDate=`+date+`&newStartTime=`+startTime+`&newEndTime=`+endTime, {}, {})
				.then(response => {
					AXIOS.get(`/availabilities/`+this.email, {}, {})
					.then(response => {
						this.availabilities = response.data
						this.displayAvailability()
					})
					.catch(e => {
						var errorMsg = e.response.data.message
						window.alert(errorMsg)
					})
				})
				.catch(e => {
					var errorMsg = e.response.data.message
					window.alert(errorMsg)
				})
			}
			}
	}
	</script>

	<style scoped>
	@import '../style/stylesheet.css';
	@import '../style/blackandwhitebb.css';
	#availTemporaryPage {
	font-family: 'Avenir', Helvetica, Arial, sans-serif;
	-webkit-font-smoothing: antialiased;
	-moz-osx-font-smoothing: grayscale;
	text-align: center;
	color: #F8F9F9;
	margin-top: 0px;
	}


	td {
		height: 10px;
	}

	.available{
		background-color: greenyellow;
	}

	.notavailable{
		background-color: #ff6961;
	}
	</style>
