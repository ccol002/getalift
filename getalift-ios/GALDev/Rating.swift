//
//  Rating.swift
//  GALDev
//
//  Created by Charly Joncheray on 01/10/2018.
//  Copyright © 2018 Loan Aubergeon. All rights reserved.
//

import UIKit
import Cosmos
import NotificationBannerSwift

class Rating: UIViewController {

    //Mark: -Variables
    let userId = Home.UserConnectedInformations.user.id
    
    var routes: [Route] = []
    
    //Mark: -Outlets
    @IBOutlet weak var rate: CosmosView!
    
    @IBOutlet weak var comment: UITextField!
    
    //Mark: -Tasks
    var ratingTasks = RatingTasks()
    
    //Mark: -Functions
    override func viewDidLoad() {
        super.viewDidLoad()
        setupTextFieldManager()
    }

    private func rateFunction() {
        
        let rate = self.rate.rating.toInt()
        let driverId = self.routes[myIndex].driver
        let routeId = self.routes[myIndex].id
        let comment = self.comment.text
        
        //Convert the today's date in String
        let todayDate = Date()
        let todayString = todayDate.toString(dateFormat: "YYYY-MM-dd")
        
        //Display a banner if the user don't initialy modify the default rate
        if self.rate.rating == 0 {
            DispatchQueue.main.async() {
                let imageView = UIImageView(image: #imageLiteral(resourceName: "failed"))
                let banner = NotificationBanner(title: "No rate", subtitle: "Please field rate", leftView: imageView, style: .danger)
                banner.show()
            }
        } else {
            //Perform the post request initialy to add the rate on the database
            ratingTasks.postRating(author: self.userId!, target: driverId!, routeId: routeId!, stars: rate!, comment: comment!, postDate: todayString) { (status, success) in
                if status == "Ok" {
                    //When a rate for the concerning route don't already exist
                    DispatchQueue.main.async {
                        print("rate add")
                        self.performSegue(withIdentifier: "unwindSegueToDriverView", sender: self)
                        let imageView = UIImageView(image:  #imageLiteral(resourceName: "success"))
                        let banner = NotificationBanner(title: "Success!", subtitle: "You have rated the route", leftView: imageView, style: .success)
                        banner.show()
                    }
                } else {
                    //If an already rate exist, the rate is modify by a PUT request (putRate)
                    DispatchQueue.main.async {
                        //Display an alert message to alert the user that he will modify his rate
                        let alert = UIAlertController(title: "You already rate this route", message: "Modify your rate?", preferredStyle: .alert)
                        //If he click on "Yes", it modify his rate by performing PUT request (putRate)
                        alert.addAction(UIAlertAction(title: "Yes", style: .default, handler: { action in
                            self.ratingTasks.putRate(stars: rate!, comment: comment!, postDate: todayString, author: self.userId!, routeId: routeId!, completitionHandler: { (status, success) in
                                if success {
                                    DispatchQueue.main.async {
                                        print("rate modify")
                                        //After perform the request the application return to the previous page.
                                        self.performSegue(withIdentifier: "unwindSegueToDriverView", sender: self)
                                        let imageView = UIImageView(image:  #imageLiteral(resourceName: "success"))
                                        let banner = NotificationBanner(title: "Success!", subtitle: "You rate have been modified", leftView: imageView, style: .success)
                                        banner.show()
                                    }
                                }
                            })
                        }))
                        
                        //If the user click on "NO", it don't perform PUT request and the application returns to the previous page.
                        alert.addAction(UIAlertAction(title: "No", style: .default, handler: { action in
                            print("rate not modify")
                            self.performSegue(withIdentifier: "unwindSegueToDriverView", sender: self)
                        }))
                        
                        self.present(alert, animated: true)
                    }
                }
            }
        }
    }
    
    private func setupTextFieldManager() {
        comment.delegate = self
        
        //To hide the keyboard when the user click in an other part of the screen than th CommentTextField
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(hideKeyboard))
        view.addGestureRecognizer(tapGesture)
    }
    
    //To send data from one page to another
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "unwindSegueToDriverView" {
            if let destination = segue.destination as? DriverView {
                destination.routes = self.routes
            }
        }
    }
    
    //Mark: -Actions
    @IBAction func postButtonPressed(_ sender: UIButton) {
        self.rateFunction()
    }
    
    @IBAction func hideKeyboard() {
        comment.resignFirstResponder()
    }
    
}

//To convert doubles to Int and avoid certain number format problems
extension Double {
    func toInt() -> Int? {
        if self > Double(Int.min) && self < Double(Int.max) {
            return Int(self)
        } else {
            return nil
        }
    }
}

//Extension to have the goof date format
extension Date {
    func toString( dateFormat format  : String ) -> String
    {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = format
        return dateFormatter.string(from: self)
    }
}

//To hide the keyboard when the user click on return
extension Rating: UITextFieldDelegate {
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
}

