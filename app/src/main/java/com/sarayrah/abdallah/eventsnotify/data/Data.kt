package com.sarayrah.abdallah.eventsnotify.data

class Data {

    companion object {
        /*bellow is the server ip, it will be changed every time while i'm working on the app
         development, coz i work locally and on a real server
         local ip: 10.153.70.145
         real server ip: 193.188.88.148*/
        val serverIp = "193.188.88.148"
        //bellow are variables to store url, so i don't need to edit it everywhere in the code
        //this the url for the base web system
        var baseUrl = "http://${Data.serverIp}/apps/MyApps/events"
        //this is the url for saveDeviceToken api
        var saveDeviceTokenUrl = "$baseUrl/android/apis/saveDeviceToken.php?deviceToken="
        //events fetching api url
        var getEventsUrl = "$baseUrl/android/apis/getEvents.php"
    }
}