# Budin_Chips
Budin Chips app Application budin chips is an application developed for management data on small-scale ukm. 
This app is based on android and uses a backend from google firebase for clound data management so it is expected
that data can be centralized and there is no conflict / error in the data.

#Configuration
1. You must have a google account and log in to firebase console
https://console.firebase.google.com
2. Create a new project
3. Select Add Firebase to your Android app
4. Fill in the Android package name with "com.mr.rohmani.kbnbudinchips"
5. Select Register
6. Download google-services.json and move to folder / app /

#Clound Function For Notification
1. Install the firebase tool with npm
2. create a new firebase function
3. fill index js with:


var functions = require('firebase-functions');
var admin = require('firebase-admin');
 
admin.initializeApp(functions.config().firebase);

exports.sendPemesananBaru = functions.database.ref('/pemesanan/{pemesananId}')
        .onCreate(event => {
 
        // Grab the current value of what was written to the Realtime Database.
        var eventSnapshot = event.data;
        var str1 = "Pesanan Baru: ";
        var str = str1.concat(eventSnapshot.child("username").val());
        console.log(str);
 
        var topic = "pemesananBaru";
        var payload = {
            data: {
                title: eventSnapshot.child("keterangan").val(),
                author: str
            }
        };
 
        // Send a message to devices subscribed to the provided topic.
        return admin.messaging().sendToTopic(topic, payload)
            .then(function (response) {
                // See the MessagingTopicResponse reference documentation for the
                // contents of response.
                console.log("Successfully sent message:", response);
            })
            .catch(function (error) {
                console.log("Error sending message:", error);
            });
        });
		
exports.sendPemesananUpdate = functions.database.ref('/pemesanan-data/{Uid}/{pemesananId}')
        .onUpdate(event => {
		var eventSnapshot = event.data;
		var Uid = event.params.Uid;
		var str1 = "Status Pesanan: ";
		var status = eventSnapshot.child("status").val();
		var str = str1.concat(status);
		//prepare query to get token
		var ref = admin.database().ref('/users/'+Uid+'/NotificationToken');
		//get token from database
		ref.once('value').then(snap => {
            var token = snap.val();
			// Notification details.
			var payload = {
			data: {
				title: eventSnapshot.child("tgl_pesan").val()+eventSnapshot.child("keterangan").val(),
				author: str
			}};
			
            return admin.messaging().sendToDevice(token, payload)
			.then(response => {
                // See the MessagingTopicResponse reference documentation for the
                // contents of response.
                console.log("Successfully sent message:", response);
            })
			.catch(function (error) {
                console.log("Error sending message:", error);
            });
        });
			
	});
		
		

4. deploy function to firebase

