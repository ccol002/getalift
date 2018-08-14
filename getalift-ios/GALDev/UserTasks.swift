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
        
        let urlString : String = ServerAdress+":3000/api/users/"+driverIdString
        
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
}
