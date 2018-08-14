//
//  FavoriteRouteTasks.swift
//  GALDev
//
//  Created by administrator on 26/10/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import Foundation

class FavoriteRouteTasks {
    
    var token = Home.UserConnectedInformations.userToken

    // Variable de stockage
    var routes : [Route] = []
    
    // Route par rapport a un driver.
    func favoriteRoute(userId: Int, completionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        
        let url = NSURL(string: ServerAdress+":3000/api/favoriteRoute/"+String(userId))!
        
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
                        let routeId = (jsonObjects["id"] as! Int)
                        
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
                            latitudeOfStartigPoint: xStart,
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
    
    // Pour aviter d'ajouter des doublons
    func favoriteRoute(routeId : Int, userId: Int, completionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)){
        
        
        
        let url = NSURL(string: ServerAdress+":3000/api/favoriteRoute/?userId="+String(userId)+"&routeId="+String(routeId))!
        
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
                if (jsonResult).count == 1 {
                    completionHandler("Existing", true)
                } else {
                    completionHandler("No existing", false)
                }
                
            } catch { // On catch les erreurs potentielles
                print(error)
                completionHandler(error as! String, false)
            }
        }
        task.resume()
    }
    
    // Pour supprimer une favoriteRoute à un identifiant donné
    func deleteFavoriteRoute(routeId: Int, completionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        
        let url = NSURL(string: ServerAdress+":3000/api/favoriteRoute/"+String(routeId))!
        
        //let url = NSURL(string: ServerAdress+":3000/api/search2?date="+date+"&startLat="+String(startLat)+"&startLng="+String(startLong)+"&endLat="+String(endLat)+"&endLng="+String(endLong)
        
        var request = URLRequest(url: url as URL)
        
        request.setValue(token, forHTTPHeaderField: "x-access-token")
        
        request.httpMethod = "DELETE"
        
        let task = URLSession.shared.dataTask(with: request as URLRequest) {
            data, response, error in
            
            do{
                completionHandler("Ok", true)
            }
            
            // Check for error
            if error != nil
            {
                print("Error")
            }
        }
        task.resume()
        
    }
    
    // Pour ajouter une route favorite
    func addFavoriteRoute(userId: Int, routeId: Int, completionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        
        let routeInformations = "userId="+String(userId)+"&routeId="+String(routeId)
        
        let url = NSURL(string: ServerAdress+":3000/api/favoriteRoute/")!
        
        var request = URLRequest(url: url as URL)
        
        request.setValue(token, forHTTPHeaderField: "x-access-token")
        
        request.httpMethod = "POST"
        
        request.httpBody = routeInformations.data(using: String.Encoding.utf8)
        
        let task = URLSession.shared.dataTask(with: request as URLRequest) {
            data, response, error in
            
            // Check for error
            if error != nil
            {
                print("Error")
                return
            }
            
            do{
                completionHandler("Ok", true)
            }
        }
        task.resume()
        
    }
}
