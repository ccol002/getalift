//
//  CommentsTasks.swift
//  GALDev
//
//  Created by Charly Joncheray on 26/09/2018.
//  Copyright © 2018 Loan Aubergeon. All rights reserved.
//

import Foundation

class CommentsTasks {
    
    var token = Home.UserConnectedInformations.userToken
    
    var commentary: [Comment] = []
    
    //Perform GET request that returns all the comments regarding a driver id.
    func commentaries (targetId: Int, completionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        
        let url = NSURL(string: ServerAdress+":7878/api/ratings/Comment/"+String(targetId))!
        
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
                            
                            let jsonbjects = (json[index]) as AnyObject
                            
                            let username = jsonbjects["username"] as! String
                            let comment = jsonbjects["comment"] as! String
                            let postDate = jsonbjects["postDate"] as! String
                            
                            let commentary = Comment.init(
                                target: targetId,
                                nameOfAuthor: username,
                                comment: comment,
                                date: postDate
                            )
                            
                            self.commentary.append(commentary)
                            
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
}
