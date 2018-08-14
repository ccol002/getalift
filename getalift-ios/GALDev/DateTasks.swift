//
//  DateTasks.swift
//  GALDev
//
//  Created by Loan Aubergeon on 30/09/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import Foundation

class DateTasks {
    
    var token = Home.UserConnectedInformations.userToken
    
    /// Variables de stockage 
    
    var date : String = ""
    var weeklyReccurence : Bool = false
    
    
    func date(routeId : Int!, completionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
    
        let routeID = String(routeId)
        
        let urlString : String = ServerAdress+":3000/api/routedate/"+routeID
        
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
                
                    if ((jsonResult).count-1) >= 0{
                        for index in 0...(jsonResult).count-1 {
                        
                            let jsonObjects = (jsonResult[index]) as AnyObject
                            let stringDate = jsonObjects["route_date"] as? String
                            let dateP = (stringDate?.replacingOccurrences(of:"T", with: " ").replacingOccurrences(of:"Z", with: " "))!
                            self.date = String(dateP.characters.prefix(17).dropLast())
                        
                            let intRec = jsonObjects["weekly_repeat"] as? Int
                            if intRec == 1 {
                                self.weeklyReccurence = true
                            } else {
                                self.weeklyReccurence = false
                            }
                        

                            completionHandler("Ok", true)
                        }
                    }
            } catch { // On catch les erreurs potentielles
                print(error)
                completionHandler(error as! String, false)
            }
            
        }
        task.resume()
    }
}
