package com.sarayrah.abdallah.eventsnotify.data

class Data {

    companion object {
        /*bellow is the server ip, it will be changed every time while i'm working on the app
         development, coz i work locally and on a real server
         local ip: 10.153.37.55
         real server ip: 193.188.88.148*/
        private const val serverIp = "192.168.0.29"
        //bellow are variables to store url, so i don't need to edit it everywhere in the code
        //this the url for the base web system
        var baseUrl = "http://${Data.serverIp}/apps/MyApps/events/android"
        //this is the url for saveDeviceToken api
        var saveDeviceTokenUrl = "$baseUrl/apis/save_device_token.php?deviceToken="
        //events fetching api url, needs to be concatenated with the 'categoryId&entityId' to get
        // the right events for the specified category and entity
        var getEventsUrl = "$baseUrl/apis/get_events.php?"
        //entities fetching api url
        var getCommitteesUrl = "$baseUrl/apis/get_committees.php?category="
        //entity categories fetching api url
        var getCategoriesUrl = "$baseUrl/apis/get_categories.php"
    }
}