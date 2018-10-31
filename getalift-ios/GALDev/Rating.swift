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
        
        if self.rate.rating == 0 {
            DispatchQueue.main.async() {
                let imageView = UIImageView(image: #imageLiteral(resourceName: "failed"))
                let banner = NotificationBanner(title: "No rate", subtitle: "Please field rate", leftView: imageView, style: .danger)
                banner.show()
            }
        } else {
            ratingTasks.postRating(author: self.userId!, target: driverId!, routeId: routeId!, stars: rate!, comment: comment!, postDate: todayString) { (status, success) in
                if status == "Ok" {
                    DispatchQueue.main.async {
                        print("rate add")
                        self.performSegue(withIdentifier: "unwindSegueToDriverView", sender: self)
                        let imageView = UIImageView(image:  #imageLiteral(resourceName: "success"))
                        let banner = NotificationBanner(title: "Success!", subtitle: "You have rated the route", leftView: imageView, style: .success)
                        banner.show()
                    }
                } else {
                    DispatchQueue.main.async {
                        let alert = UIAlertController(title: "You already rate this route", message: "Modify your rate?", preferredStyle: .alert)
                        alert.addAction(UIAlertAction(title: "Yes", style: .default, handler: { action in
                            self.ratingTasks.putRate(stars: rate!, comment: comment!, postDate: todayString, author: self.userId!, routeId: routeId!, completitionHandler: { (status, success) in
                                if success {
                                    DispatchQueue.main.async {
                                        print("rate modify")
                                        self.performSegue(withIdentifier: "unwindSegueToDriverView", sender: self)
                                        let imageView = UIImageView(image:  #imageLiteral(resourceName: "success"))
                                        let banner = NotificationBanner(title: "Success!", subtitle: "You rate have been modified", leftView: imageView, style: .success)
                                        banner.show()
                                    }
                                }
                            })
                        }))
                        
                        
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
        
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(hideKeyboard))
        view.addGestureRecognizer(tapGesture)
    }
    
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

extension Double {
    func toInt() -> Int? {
        if self > Double(Int.min) && self < Double(Int.max) {
            return Int(self)
        } else {
            return nil
        }
    }
}

extension Date {
    func toString( dateFormat format  : String ) -> String
    {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = format
        return dateFormatter.string(from: self)
    }
}

extension Rating: UITextFieldDelegate {
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
}

