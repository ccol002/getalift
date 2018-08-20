//
//  RouteTasks.swift
//  GALDev
//
//  Created by Loan Aubergeon on 29/09/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import Foundation

class RouteTasks {
    
    var token = Home.UserConnectedInformations.userToken
    
    var mapTasks = MapTasks()
    
    
    // Variable de stockage
    var routes : [Route] = []
    
    /*func route(date: String, startLat : Double, startLong : Double, endLat : Double, endLong : Double, completionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        
        //let url = NSURL(string: ServerAdress+":3000/api/search?date=")!
        
        //let url = NSURL(string: ServerAdress+":3000/api/search2?date="+date+"&startLat="+String(startLat)+"&startLng="+String(startLong)+"&endLat="+String(endLat)+"&endLng="+String(endLong))!
        //let url = NSURL(string: ServerAdress+":7878/api/findTarget="+startDate+"&startLat="+String(startLat)+"&startLng="+String(startLong)+"&endLat="+String(endLat)+"&endLng="+String(endLong))!
        
        let url  = NSURL(string: ServerAdress+":7878/api/findTarget")!
        
        let parameters = "startDate="+date+"&startLng="+String(startLong)+"&endLng="+String(endLong)+"&startLat="+String(startLat)+"&endLat="+String(endLat)
        
        var request = URLRequest(url: url as URL)
        
        //request.setValue(token, forHTTPHeaderField: "x-access-token")
        request.setValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
        
        //request.httpMethod = "GET"
        request.httpMethod = "POST"
        
        request.httpBody = parameters.data(using: String.Encoding.utf8)
        
        let task = URLSession.shared.dataTask(with: request as URLRequest) {
            data, response, error in
            
            // Check for error
            if error != nil
            {
                print("Error")
                return
            }
            
            do {
                let jsonResult = try JSONSerialization.jsonObject(with: data!, options: JSONSerialization.ReadingOptions.mutableContainers) as! NSArray
                //let json = try JSONSerialization.jsonObject(with: data!, options: .mutableContainers) as? NSDictionary
                
                var compteur = 0
                
                if (jsonResult).count > 0 {
                    for index in 0...(jsonResult).count-1 {
                        
                        let jsonObjects = (jsonResult[index]) as AnyObject
                        
                        //let driverId = (jsonObjects["driver"] as! Int)
                        let driverId = (jsonObjects["user_id"] as! Int)
                        //let routeId = (jsonObjects["route"] as! Int)
                        let routeId = (jsonObjects["id"] as! Int)
                        
                        //let startingPoint = jsonObjects["startingPoint"] as AnyObject
                        let startingPoint = jsonObjects["routePoint"] as AnyObject
                        //let endPoint = jsonObjects["endPoint"] as AnyObject
                        let endPoint = jsonObjects["endPoint"] as AnyObject
                        
                        let xStart = startingPoint["x"] as! Double
                        let yStart = startingPoint["y"] as! Double
                        let xEnd = endPoint["x"] as! Double
                        let yEnd = endPoint["y"] as! Double
                        
                        let originName = jsonObjects["originAdress"] as! String
                        let destinationName = jsonObjects["destinationAdress"] as! String
                        //let distance = jsonObjects["distance"] as! String
                        let distance = jsonObjects["totalDistance"] as! String
                        let duration = jsonObjects["duration"] as! String
                        
                        let route = Route.init(
                            id : routeId,
                            nameOfStartingPoint: originName,
                            latitudeOfStartingPoint: xStart,
                            longitudeOfStartingPoint: yStart,
                            nameOfEndpoint: destinationName,
                            latitudeOfEndPoint: xEnd,
                            longitudeOfEndPoint: yEnd,
                            driver: driverId,
                            distance: distance,
                            duration: duration
                        )
                        
                        self.routes.append(route)
                        
                        if compteur == (jsonResult).count-1 {
                            completionHandler("Ok", true)
                        }
                        compteur = compteur + 1
                        
                    }
                }
                
            } catch { // On catch les erreurs potentielles
                print(error)
                completionHandler(error as! String, false)
            }
            
        }
        task.resume()
        
    }*/
    
    func route(date: String, startLat : Double, startLong : Double, endLat : Double, endLong : Double, completionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        
        //declare parameter as a dictionary which contains string as key and value combination. considering inputs are valid
        
        /*let parameters = ["startLat": String(startLat), "startLng": String(startLong), "endLat": String(endLat), "endLng": String(endLong), "startDate": String(date)]*/
        
        //create the url with URL
        let url  = URL(string: ServerAdress+":7878/api/findTarget")!
        
        //create the session object
        let session = URLSession.shared
        
        //now create the URLRequest object using the url object
        var request = URLRequest(url: url)
        request.httpMethod = "POST" //set http method as POST
        
        let parameters = "startDate="+date+"&startLng="+String(startLong)+"&endLng="+String(endLong)+"&startLat="+String(startLat)+"&endLat="+String(endLat)
        
        request.httpBody = parameters.data(using: String.Encoding.utf8)
        
        request.addValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
        request.addValue(token, forHTTPHeaderField: "x-access-token")
        
        //create dataTask using the session object to send data to the server
        let task = session.dataTask(with: request as URLRequest, completionHandler: { data, response, error in
            
            guard error == nil else {
                return
            }
            
            guard let data = data else {
                return
            }
            
            do {
                //create json object from data
                if let json = try JSONSerialization.jsonObject(with: data, options: .mutableContainers) as? NSDictionary {
                    var compteur = 0
                    
                    if (json).count > 0 {
                        for index in 0...(json).count - 1 {
                            let jsonObjects = (json[index]) as AnyObject
                            
                            let driverId = (jsonObjects["user_id"] as! Int)
                            let routeId = (jsonObjects["id"] as! Int)
                            
                            let startingPoint = jsonObjects["closestPointStart"] as AnyObject
                            let endPoint = jsonObjects["closestPointEnd"] as AnyObject
                            
                            let xStart = startingPoint["lng"] as! Double
                            let yStart = startingPoint["lat"] as! Double
                            
                            let xEnd = endPoint["lng"] as! Double
                            let yEnd = endPoint["lat"] as! Double
                            
                            let originName = startingPoint["id"] as! String
                            let destinationName = endPoint["id"] as! String
                            let distance = jsonObjects["totalDistance"] as! String
                            let duration = endPoint["seconds_from_start"] as! String
                            
                            let route = Route.init(
                                id : routeId,
                                nameOfStartingPoint: originName,
                                latitudeOfStartingPoint: xStart,
                                longitudeOfStartingPoint: yStart,
                                nameOfEndpoint: destinationName,
                                latitudeOfEndPoint: xEnd,
                                longitudeOfEndPoint: yEnd,
                                driver: driverId,
                                distance: distance,
                                duration: duration
                            )
                            
                            self.routes.append(route)
                            
                            if compteur == (json).count-1 {
                                completionHandler("Ok", true)
                            }
                            compteur = compteur + 1
                        }
                    }
                }
            } catch let error {
                print(error.localizedDescription)
            }
        })
        task.resume()
    }
    
    func route(date: String, completionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        
        //let url = NSURL(string: ServerAdress+":3000/api/search?date=")!
        let url = NSURL(string: ServerAdress+":7878/api/search?date=")!
        
        //let url = NSURL(string: ServerAdress+":3000/api/search2?date="+date+"&startLat="+String(startLat)+"&startLng="+String(startLong)+"&endLat="+String(endLat)+"&endLng="+String(endLong)
        
        var request = URLRequest(url: url as URL)
        
        request.setValue(token, forHTTPHeaderField: "x-access-token")
        
        request.httpMethod = "GET"
        
        let task = URLSession.shared.dataTask(with: request as URLRequest) {
            data, response, error in
            
            // Check for error
            if error != nil
            {
                print("Error")
                return
            }
            
            do {
                let jsonResult = try JSONSerialization.jsonObject(with: data!, options: JSONSerialization.ReadingOptions.mutableContainers) as! NSArray
                
                var compteur = 0
                
                if (jsonResult).count > 0 {
                    for index in 0...(jsonResult).count-1 {
                        
                        let jsonObjects = (jsonResult[index]) as AnyObject
                        
                        let driverId = (jsonObjects["driver"] as! Int)
                        let routeId = (jsonObjects["route"] as! Int)
                        
                        let startingPoint = jsonObjects["startingPoint"] as AnyObject
                        let endPoint = jsonObjects["endPoint"] as AnyObject
                        
                        let xStart = startingPoint["x"] as! Double
                        let yStart = startingPoint["y"] as! Double
                        let xEnd = endPoint["x"] as! Double
                        let yEnd = endPoint["y"] as! Double
                        
                        let originName = jsonObjects["originAdress"] as! String
                        let destinationName = jsonObjects["destinationAdress"] as! String
                        let distance = jsonObjects["distance"] as! String
                        let duration = jsonObjects["duration"] as! String
                        
                        let route = Route.init(
                            id : routeId,
                            nameOfStartingPoint: originName,
                            latitudeOfStartingPoint: xStart,
                            longitudeOfStartingPoint: yStart,
                            nameOfEndpoint: destinationName,
                            latitudeOfEndPoint: xEnd,
                            longitudeOfEndPoint: yEnd,
                            driver: driverId,
                            distance: distance,
                            duration: duration
                        )
                        
                        self.routes.append(route)
                        
                        if compteur == (jsonResult).count-1 {
                            completionHandler("Ok", true)
                        }
                        compteur = compteur + 1
                        
                    }
                }
                
            } catch { // On catch les erreurs potentielles
                print(error)
                completionHandler(error as! String, false)
            }
            
        }
        task.resume()
        
    }
    
    // Route par rapport a un driver.
    func route(driverId: Int, completionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        
        //let url = NSURL(string: ServerAdress+":3000/api/driverroutes/"+String(driverId))!
        let url = NSURL(string: ServerAdress+":7878/api/driverroutes/"+String(driverId))!
        
        //let url = NSURL(string: ServerAdress+":3000/api/search2?date="+date+"&startLat="+String(startLat)+"&startLng="+String(startLong)+"&endLat="+String(endLat)+"&endLng="+String(endLong)
        
        var request = URLRequest(url: url as URL)
        
        request.setValue(token, forHTTPHeaderField: "x-access-token")
        
        request.httpMethod = "GET"
        
        let task = URLSession.shared.dataTask(with: request as URLRequest) {
            data, response, error in
            
            // Check for error
            if error != nil
            {
                print("Error")
                return
            }
            
            do {
                let jsonResult = try JSONSerialization.jsonObject(with: data!, options: JSONSerialization.ReadingOptions.mutableContainers) as! NSArray
                
                var compteur = 0
                
                if (jsonResult).count > 0 {
                    for index in 0...(jsonResult).count-1 {
                        
                        let jsonObjects = (jsonResult[index]) as AnyObject
                        
                        let driverId = (jsonObjects["driver"] as! Int)
                        let routeId = (jsonObjects["route"] as! Int)
                        
                        let startingPoint = jsonObjects["startingPoint"] as AnyObject
                        let endPoint = jsonObjects["endPoint"] as AnyObject
                        
                        let xStart = startingPoint["x"] as! Double
                        let yStart = startingPoint["y"] as! Double
                        let xEnd = endPoint["x"] as! Double
                        let yEnd = endPoint["y"] as! Double
                        
                        let originName = jsonObjects["originAdress"] as! String
                        let destinationName = jsonObjects["destinationAdress"] as! String
                        let distance = jsonObjects["distance"] as! String
                        let duration = jsonObjects["duration"] as! String
                        
                        let route = Route.init(
                            id : routeId,
                            nameOfStartingPoint: originName,
                            latitudeOfStartingPoint: xStart,
                            longitudeOfStartingPoint: yStart,
                            nameOfEndpoint: destinationName,
                            latitudeOfEndPoint: xEnd,
                            longitudeOfEndPoint: yEnd,
                            driver: driverId,
                            distance: distance,
                            duration: duration
                        )
                        
                        self.routes.append(route)
                        
                        if compteur == (jsonResult).count-1 {
                            completionHandler("Ok", true)
                        }
                        compteur = compteur + 1
                        
                    }
                }
                
            } catch { // On catch les erreurs potentielles
                print(error)
                completionHandler(error as! String, false)
            }
            
        }
        task.resume()
        
    }
    
    
    
    // Pour supprimer une route à un identifiant donné
    func deleteRoute(routeId: Int, completionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        
        //let url = NSURL(string: ServerAdress+":3000/api/routes/"+String(routeId))!
        let url = NSURL(string: ServerAdress+":7878/api/routes/"+String(routeId))!
        
        //let url = NSURL(string: ServerAdress+":3000/api/search2?date="+date+"&startLat="+String(startLat)+"&startLng="+String(startLong)+"&endLat="+String(endLat)+"&endLng="+String(endLong)
        
        var request = URLRequest(url: url as URL)
        
        request.setValue(token, forHTTPHeaderField: "x-access-token")
        
        request.httpMethod = "DELETE"
        
        let task = URLSession.shared.dataTask(with: request as URLRequest) {
            data, response, error in
            
            // Check for error
            if error != nil
            {
                print("Error")
                return
            }
        }
        task.resume()
        
    }
}
