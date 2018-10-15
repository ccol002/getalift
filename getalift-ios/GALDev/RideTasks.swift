//
//  RideTasks.swift
//  GALDev
//
//  Created by Charly Joncheray on 20/09/2018.
//  Copyright © 2018 Loan Aubergeon. All rights reserved.
//

import Foundation

class RideTasks {
    
    var token = Home.UserConnectedInformations.userToken
    
    var insertId: Int = 0
    
    var rides: [Route] = []
    
    //Pour afficher les rides par rapport à un passager
    func rides (passengerId : Int, completionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        
        let url = NSURL(string: ServerAdress+":7878/api/rides/route/"+String(passengerId))!
        
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
                            latitudeOfStartingPoint: xStart,
                            longitudeOfStartingPoint: yStart,
                            nameOfEndpoint: destinationName,
                            latitudeOfEndPoint: xEnd,
                            longitudeOfEndPoint: yEnd,
                            driver: driverId,
                            distance: distance,
                            duration: duration
                        )
                        
                        self.rides.append(route)
                        
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
    
    //Pour ajouter un ride
    func addRide(routeID: Int, completitionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        //create the url with URL
        let url  = URL(string: ServerAdress+":7878/api/rides/")!
        
        //now create the URLRequest object using the url object
        var request = URLRequest(url: url)
        request.httpMethod = "POST" //set http method as POST
        
        let parameters = "route="+String(routeID)
        
        request.httpBody = parameters.data(using: String.Encoding.utf8)
        
        request.addValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
        request.addValue(token, forHTTPHeaderField: "x-access-token")
        
        //create dataTask using the session object to send data to the server
        let task = URLSession.shared.dataTask(with: request as URLRequest) {
            data, response, error in
            
            //Check for error
            if error != nil
            {
                print("error")
                return
            }
            
            do {
                let json = try JSONSerialization.jsonObject(with: data!, options: .mutableContainers) as? NSDictionary
                
                if let parseJSON = json {
                    DispatchQueue.main.async() {
                        
                        let success = parseJSON["success"] as? Bool
                        
                        if success! {
                            self.insertId = (parseJSON["insertId"] as? Int)!
                            completitionHandler("Ok",true)
                        } else {
                            
                            completitionHandler("",false)
                        }
                    }
                }
            } catch let error {
                print(error.localizedDescription)
                completitionHandler("",false)
            }
        }
        task.resume()
    }
    
    //Pour supprimer un ride
    
}
