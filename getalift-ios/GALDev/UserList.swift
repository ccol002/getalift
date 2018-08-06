//
//  UserList.swift
//  GALDev
//
//  Created by Loan Aubergeon on 25/08/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import UIKit

class UserList : UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    @IBOutlet weak var menuButton:UIBarButtonItem!
    
    @IBOutlet var userTableView : UITableView!
    
    var token = Home.UserConnectedInformations.userToken
    
    // The list of Users
    var users : [User] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.userList()
        
        if self.revealViewController() != nil {
            menuButton.target = self.revealViewController()
            menuButton.action = #selector(SWRevealViewController.revealToggle(_:))
            self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
        }
        
        userTableView.dataSource = self
        userTableView.delegate = self
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    //Nombre de sections en tout
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
        case 0: return self.users.count
        default: return 0
        }
    }
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        switch section {
        case 0: return "Users : "
        default: return ""
        }
    }
    
    //Cellule à l'index concerné
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = UITableViewCell(style: .default, reuseIdentifier: "basic")
        for i in 0...self.users.count-1 {
            if (indexPath.row == i) {
                cell.textLabel?.text = self.users[i].username+" : "+self.users[i].name+" "+self.users[i].surname
            }
        }
        return cell
    }
    
    
    // Requete pour avoir la liste des utilisateur
    func userList(){
        
        //let tokenString = "token="+token
        
        let url = NSURL(string: ServerAdress+":3000/api/users/")!
        
        var request = URLRequest(url: url as URL)
        
        //request.setValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
        request.setValue(token, forHTTPHeaderField: "x-access-token")
        
        request.httpMethod = "GET"
        
        //request.httpBody = tokenString.data(using: String.Encoding.utf8)
        
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
                
                DispatchQueue.main.async(execute: {
                    for index in 0...(jsonResult).count-1 {
                        let jsonObjects = (jsonResult[index]) as AnyObject
                        
                        let id = jsonObjects["id"] as! Int
                        let username = jsonObjects["username"] as! String
                        let name = jsonObjects["name"] as! String
                        let surname = (jsonObjects["surname"] as! String)
                        
                        let user = User.init(id: id,username: username, name: name, surname: surname, email: "", mobileNumber: "")
                        
                        self.users.append(user)
                    }
                    self.userTableView.reloadData()
                })
                
            } catch { // On catch les erreurs potentielles
                print(error)
            }
            
        }
        task.resume()
        
    }




}
