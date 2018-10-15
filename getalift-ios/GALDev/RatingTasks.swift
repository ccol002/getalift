//
//  RatingTasks.swift
//  GALDev
//
//  Created by Charly Joncheray on 19/09/2018.
//  Copyright © 2018 Loan Aubergeon. All rights reserved.
//

import Foundation

class RatingTasks {
    
    var token = Home.UserConnectedInformations.userToken
    
    var rideId: Int = 0
    
    var rate: Float = 0.0
    
    //Fonction qui permet de récupérer la note d'un driver.
    func getRating(targetId : Int, completitionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        
        let targetId = targetId
        
        let url = NSURL(string: ServerAdress+":7878/api/ratings/" + String(targetId))!
        
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
                    
                    let jsonObject = (json[0]) as AnyObject
                    
                    let average = jsonObject["AVG(stars)"] as! Float
                    
                    self.rate = average
                    
                    completitionHandler("Ok", true)
                }
            } catch let error {
                print(error.localizedDescription)
                completitionHandler("",false)
            }
        }
        task.resume()
    }
    
    func postRating(author: Int, target: Int, routeId: Int, stars: Int, comment: String, postDate: String, completitionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        //create the url with URL
        let url  = URL(string: ServerAdress+":7878/api/ratings/existingRate")!
        
        //now create the URLRequest object using the url object
        var request = URLRequest(url: url)
        request.httpMethod = "POST" //set http method as POST
        
        let parameters = "author="+String(author)+"&target="+String(target)+"&routeId="+String(routeId)+"&stars="+String(stars)+"&comment="+comment+"&postDate="+postDate
        
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
                        
                        if success == true {
                            completitionHandler("Ok",true)
                        } else {
                            completitionHandler("alreadyRated",false)
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
    
    
    func putRide(stars: Int, comment: String, postDate: String, author: Int, routeId: Int, completitionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        
        //create the url with URL
        let url  = URL(string: ServerAdress+":7878/api/ratings")!
        
        //now create the URLRequest object using the url object
        var request = URLRequest(url: url)
        request.httpMethod = "PUT" //set http method as POST
        
        let parameters = "stars="+String(stars)+"&comment="+comment+"&postDate="+postDate+"&author="+String(author)+"&routeId="+String(routeId)
        
        request.httpBody = parameters.data(using: String.Encoding.utf8)
        
        request.addValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
        request.addValue(token, forHTTPHeaderField: "x-access-token")
        
        //create dataTask using the session object to send data to the server
        let task = URLSession.shared.dataTask(with: request as URLRequest) {
            data, response, error in
            
            // Check for error
            if error != nil
            {
                print("Error")
                return
            }
            
            do{
                completitionHandler("Ok", true)
            }
        }
        task.resume()
    }
}














