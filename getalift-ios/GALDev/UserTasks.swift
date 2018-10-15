//
//  UserTasks.swift
//  GALDev
//
//  Created by Loan Aubergeon on 29/09/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import UIKit

class UserTasks {
    
    var token = Home.UserConnectedInformations.userToken
    
    var user : User = User.init()
    
    func user(driverId: Int!, completionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        
        let driverIdString = String(driverId)
        
        let urlString : String = ServerAdress+":7878/api/users/"+driverIdString
        
        let url = NSURL(string: urlString)!
        
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
                
                let jsonObjects = (jsonResult[0]) as AnyObject
                
                let id = jsonObjects["id"] as? Int
                let name = jsonObjects["name"] as? String
                let surname = jsonObjects["surname"] as? String
                let username = jsonObjects["username"] as? String
                let mobileNumber = jsonObjects["mobileNumber"] as? String
                let email = (jsonObjects["email"] as? String)
                
                self.user = User.init(id: id!, username : username!, name: name!, surname : surname!, email : email!, mobileNumber: mobileNumber!)
                
                completionHandler("Ok", true)
                
            } catch { // On catch les erreurs potentielles
                print(error)
                completionHandler("", false)
            }
        }
        task.resume()
    }
    
    func editUser(driverId: Int, username: String, password: String, name: String, surname: String, email: String, mobileNumber: String, completionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        
        let url = URL(string: ServerAdress+":7878/api/users/"+String(driverId))!
        
        var request = URLRequest(url: url)
        request.httpMethod = "PUT"
    
        let parameters = "username=" + username + "&password=" + password + "&name=" + name + "&surname=" + surname + "&email=" + email + "&mobileNumber=" + mobileNumber + "&isVerified=0"
        
        request.httpBody = parameters.data(using: String.Encoding.utf8)
        
        request.addValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
        request.addValue(token, forHTTPHeaderField: "x-access-token")
        
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
    
    func authentification(username: String, password: String, completionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        
        let url = URL(string: ServerAdress+":7878/api/auth")!
        
        var request = URLRequest(url: url)
        
        request.httpMethod = "POST"
        
        let parameters = "username=" + username + "&password=" + password
        
        request.httpBody = parameters.data(using: String.Encoding.utf8)
        
        request.setValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
        
        let task = URLSession.shared.dataTask(with: request as URLRequest) {
            data, response, error in
            
            /// Check for error
            if error != nil
            {
                print("Error")
                return
                
            } else {
                /// Convert server json response to NSDictionary
                do {
                    let json = try JSONSerialization.jsonObject(with: data!, options: .mutableContainers) as? NSDictionary
                    
                    if let parseJSON = json {
                        DispatchQueue.main.async {
                            /// Recovery of request state
                            let success = parseJSON["success"] as? Bool
                        
                            /// If the request has worked
                            if success == true {
                                completionHandler("",true)
                            } else {
                                completionHandler("",false)
                            }
                        }
                    }
                } catch let error as NSError {
                    print(error.localizedDescription)
                }
            }
        }
        task.resume()
    }
    
}
