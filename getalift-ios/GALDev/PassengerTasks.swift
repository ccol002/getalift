//
//  PassengerTasks.swift
//  GALDev
//
//  Created by Charly Joncheray on 18/09/2018.
//  Copyright © 2018 Loan Aubergeon. All rights reserved.
//

import Foundation

class PassengerTasks {
    
    var token = Home.UserConnectedInformations.userToken
    
    var passengers: [Passenger] = []
    
    var userId = Home.UserConnectedInformations.user.id
    
    //Fonction qui renvoie un tableau de tous les passagers par rapport à un conducteur
    func passengerNames(driverId: Int, completionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        
        let url = NSURL(string: ServerAdress+":7878/api/passenger/alert/"+String(driverId))!
        
        var request = URLRequest(url: url as URL)
        
        request.setValue(token, forHTTPHeaderField: "x-access-token")
        
        request.httpMethod = "GET"
        let task = URLSession.shared.dataTask(with: request as URLRequest) {
            data, response, error in
            
            //Check for error
            if error != nil
            {
                print("error")
                return
            }
            
            do {
                if let json = try JSONSerialization.jsonObject(with: data!, options: JSONSerialization.ReadingOptions.mutableContainers) as? NSArray {
                    
                    var compteur: Int = 0
                    
                    if (json).count > 0 {
                        for index in 0...(json).count - 1 {
                            
                            let jsonObjects = (json[index]) as AnyObject
                            
                            let id = jsonObjects["id"] as! Int
                            let ride = jsonObjects["ride"] as! Int
                            let passenger = jsonObjects["passenger"] as! Int
                            let inTheCar = jsonObjects["inTheCar"] as! Int
                            let username = jsonObjects["username"] as! String
                            
                            let aPassenger = Passenger.init(
                                id : id,
                                ride : ride,
                                passenger : passenger,
                                inTheCar : inTheCar,
                                username : username
                            )
                            
                            self.passengers.append(aPassenger)
                            
                            if compteur == (json).count-1 {
                                completionHandler("Ok", true)
                            }
                            compteur += 1
                        }
                    }
                }
            } catch let error {
                print(error.localizedDescription)
                completionHandler("",false)
            }
        }
        task.resume()
    }
    
    //Fonction qui permet de supprimer un passager en fonction de l'id
    func deletePassenger(passengerID: Int, completionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        
        //let url = NSURL(string: ServerAdress+":3000/api/routes/"+String(routeId))!
        let url = NSURL(string: ServerAdress+":7878/api/passenger/"+String(passengerID))!
        
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
    
    //Fonction qui permet de passer inTheCar à la valeur 1 si le driver confirme la presence du passager
    func changeInTheCarColumn (passID : Int, completionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        //create the url with URL
        let url  = URL(string: ServerAdress+":7878/api/passenger/alert/"+String(passID))!
        
        //create the session object
        let session = URLSession.shared
        
        //now create the URLRequest object using the url object
        var request = URLRequest(url: url)
        request.httpMethod = "PUT" //set http method as POST
        
        let parameters = "inTheCar=1"
        
        request.httpBody = parameters.data(using: String.Encoding.utf8)
        
        request.addValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
        request.addValue(token, forHTTPHeaderField: "x-access-token")
        
        //create dataTask using the session object to send data to the server
        let task = session.dataTask(with: request as URLRequest, completionHandler: { data, response, error in
            if error != nil {
                print("error")
                return
            }
        })
        task.resume()
    }
    
    //Fonction qui ajoute un passager dans la base de donné passagé
    func addpass (ride: Int, passenger: Int) {
        
        let url = URL(string: ServerAdress + ":7878/api/passenger")!
        
        let session = URLSession.shared
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        
        let parameters = "ride="+String(ride)+"&passenger="+String(passenger)+"&inTheCar=0"
        
        request.httpBody = parameters.data(using: String.Encoding.utf8)
        
        request.addValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
        request.addValue(token, forHTTPHeaderField: "x-access-token")
        
        let task = session.dataTask(with: request as URLRequest, completionHandler: {data, response, error in
            if error != nil {
                print("error")
                return
            }
        })
        task.resume()
    }
    
    func addPassengerExistingRide(passengerID: Int, routeID: Int, completitionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        //create the url with URL
        let url  = URL(string: ServerAdress+":7878/api/passenger/existingRide")!
        
        //now create the URLRequest object using the url object
        var request = URLRequest(url: url)
        request.httpMethod = "POST" //set http method as POST
        
        let parameters = "passId="+String(passengerID)+"&routeId"+String(routeID)
        
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
                            completitionHandler("Ok",true)
                        } else {
                            completitionHandler("alreadyPassenger",false)
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
    
}

