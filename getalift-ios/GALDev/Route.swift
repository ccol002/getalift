//
//  Route.swift
//  GALDev
//
//  Created by Loan Aubergeon on 05/10/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import Foundation

struct Route {
    
    var id : Int!
    
    var overviewPolyline : NSDictionary!
    
    var nameOfStartingPoint : String!
    var nameOfEndpoint : String!
    
    var latitudeOfStartigPoint : Double!
    var longitudeOfStartingPoint : Double!
    var latitudeOfEndPoint : Double!
    var longitudeOfEndPoint : Double!
    
    var driver : Int!

    var date : String!
    var time : String!
    var recurrence : Bool!
    
    var totalDistanceInMetter : Int!
    var distance : String!
    var duration : String!
    
    init(){
    }
    
    
    init(origin: String, destination: String){
        self.nameOfStartingPoint = origin
        self.nameOfEndpoint = destination
    }
    

    
    // Route pour RouteTasks
    init(id : Int, nameOfStartingPoint : String, latitudeOfStartigPoint : Double, longitudeOfStartingPoint : Double, nameOfEndpoint : String, latitudeOfEndPoint : Double, longitudeOfEndPoint : Double, driver : Int, distance : String, duration : String){
        self.id = id
        self.nameOfStartingPoint = nameOfStartingPoint
        self.latitudeOfStartigPoint = latitudeOfStartigPoint
        self.longitudeOfStartingPoint = longitudeOfStartingPoint
        self.nameOfEndpoint = nameOfEndpoint
        self.latitudeOfEndPoint = latitudeOfEndPoint
        self.longitudeOfEndPoint = longitudeOfEndPoint
        self.distance = distance
        self.duration = duration
        self.driver = driver
    }
    
    // Route pour la route recherché
    init(nameOfStartingPoint : String, latitudeOfStartigPoint : Double, longitudeOfStartingPoint : Double, nameOfEndpoint : String, latitudeOfEndPoint : Double, longitudeOfEndPoint : Double, overviewPolyline : NSDictionary, date : String, time : String, driver : Int, recurrence : Bool, distance : String, duration : String){
        self.nameOfStartingPoint = nameOfStartingPoint
        self.latitudeOfStartigPoint = latitudeOfStartigPoint
        self.longitudeOfStartingPoint = longitudeOfStartingPoint
        self.nameOfEndpoint = nameOfEndpoint
        self.latitudeOfEndPoint = latitudeOfEndPoint
        self.longitudeOfEndPoint = longitudeOfEndPoint
        self.overviewPolyline = overviewPolyline
        self.date = date
        self.time = time
        self.recurrence = recurrence
        self.distance = distance
        self.duration = duration
    }
    
    // Route pour l'affichage des routes dans RouteView
    init(nameOfStartingPoint : String, latitudeOfStartigPoint : Double, longitudeOfStartingPoint : Double, nameOfEndpoint : String, latitudeOfEndPoint : Double, longitudeOfEndPoint : Double, overviewPolyline : NSDictionary, totalDistanceInMetter : Int){
        self.nameOfStartingPoint = nameOfStartingPoint
        self.latitudeOfStartigPoint = latitudeOfStartigPoint
        self.longitudeOfStartingPoint = longitudeOfStartingPoint
        self.nameOfEndpoint = nameOfEndpoint
        self.latitudeOfEndPoint = latitudeOfEndPoint
        self.longitudeOfEndPoint = longitudeOfEndPoint
        self.overviewPolyline = overviewPolyline
        self.totalDistanceInMetter = totalDistanceInMetter
    }
    
    
       
}
